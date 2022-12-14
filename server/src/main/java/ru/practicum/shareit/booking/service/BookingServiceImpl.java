package ru.practicum.shareit.booking.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.*;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.error.BadRequestException;
import ru.practicum.shareit.error.NotFoundException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.model.ItemEntity;
import ru.practicum.shareit.item.model.ShortItemDto;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.ShortUserDto;
import ru.practicum.shareit.user.model.UserEntity;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service("BookingRepository")
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public BookingServiceImpl(BookingRepository bookingRepository,
                              ItemRepository itemRepository,
                              UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    @Override
    public BookingOutputDto create(Long userId, BookingInputDto bookingInputDto) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Can't found user with id " + userId));
        Optional<ItemEntity> itemEntityOp = itemRepository.findById(bookingInputDto.getItemId());
        if (itemEntityOp.isEmpty() || itemEntityOp.get().getOwnerId().equals(userId)) {
            throw new NotFoundException("Can't found item with id " + bookingInputDto.getItemId());
        }
        if (!itemEntityOp.get().getAvailable()) {
            throw new BadRequestException("item is unavailable");
        }
        if (bookingInputDto.getEnd().isBefore(LocalDateTime.now())
                || bookingInputDto.getEnd().isBefore(bookingInputDto.getStart())
                || bookingInputDto.getStart().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("end time can't be in past");
        }
        BookingEntity bookingEntity = BookingMapper.toBooking(bookingInputDto, userId);
        bookingEntity.setStatus(Status.WAITING);
        bookingEntity = bookingRepository.save(bookingEntity);
        BookingOutputDto bookingOutputDto = BookingMapper.toBookingDto(bookingEntity);
        ShortUserDto shortUserDto = new ShortUserDto(userEntity.getId());
        ShortItemDto shortItemDto = new ShortItemDto(itemEntityOp.get().getId(), itemEntityOp.get().getName());
        bookingOutputDto.setBooker(shortUserDto);
        bookingOutputDto.setItem(shortItemDto);
        return bookingOutputDto;
    }

    @Override
    public BookingOutputDto update(Long userId, Long bookingId, Boolean approved) {
        if (approved == null) {
            throw new BadRequestException("missing action");
        }
        BookingEntity bookingEntity = new BookingEntity();
        bookingEntity.setId(bookingId);
        bookingEntity = bookingRepository.findOne(Example.of(bookingEntity)).orElseThrow();
        ItemEntity itemEntity = itemRepository.findById(bookingEntity.getItemId()).orElseThrow();
        if (!itemEntity.getOwnerId().equals(userId)) {
            throw new NotFoundException("u must me owner");
        }
        if (bookingEntity.getStatus() != Status.WAITING) {
            throw new BadRequestException("booking already approved");
        }
        if (approved) {
            bookingEntity.setStatus(Status.APPROVED);
        } else {
            bookingEntity.setStatus(Status.REJECTED);
        }
        bookingEntity = bookingRepository.save(bookingEntity);
        ShortUserDto shortUserDto = new ShortUserDto(bookingEntity.getBookerId());
        BookingOutputDto bookingOutputDto = new BookingOutputDto();
        bookingOutputDto.setId(bookingEntity.getId());
        bookingOutputDto.setStart(bookingEntity.getStart());
        bookingOutputDto.setEnd(bookingEntity.getEnd());
        bookingOutputDto.setBooker(shortUserDto);
        bookingOutputDto.setStatus(bookingEntity.getStatus());
        ItemDto itemDto = ItemMapper.mapToItemDto(itemEntity);
        if (!itemDto.getOwner().equals(userId)) {
            throw new NotFoundException("Booker can't approved book");
        }
        ShortItemDto shortItemDto = new ShortItemDto(itemDto.getId(), itemDto.getName());
        bookingOutputDto.setItem(shortItemDto);
        return bookingOutputDto;
    }

    @Override
    public BookingOutputDto getById(Long userId, Long bookingId) {
        if (userRepository.findById(userId).isEmpty() || bookingRepository.findById(bookingId).isEmpty()) {
            throw new NotFoundException("Can't found booking with id ");
        }
        BookingEntity bookingEntity = bookingRepository.findById(bookingId).orElseThrow();
        ItemEntity itemEntity = itemRepository.findById(bookingEntity.getItemId())
                .orElseThrow(() -> new NotFoundException("booking not found"));
        ShortUserDto shortUserDto = new ShortUserDto(bookingEntity.getBookerId());
        BookingOutputDto bookingOutputDto = new BookingOutputDto();
        bookingOutputDto.setId(bookingEntity.getId());
        bookingOutputDto.setStart(bookingEntity.getStart());
        bookingOutputDto.setEnd(bookingEntity.getEnd());
        bookingOutputDto.setBooker(shortUserDto);
        bookingOutputDto.setStatus(bookingEntity.getStatus());
        ItemDto itemDto = ItemMapper.mapToItemDto(itemEntity);
        ShortItemDto shortItemDto = new ShortItemDto(itemDto.getId(), itemDto.getName());
        bookingOutputDto.setItem(shortItemDto);
        if (Objects.equals(bookingOutputDto.getBooker().getId(), userId) ||
                Objects.equals(itemDto.getOwner(), userId)) {
            return bookingOutputDto;
        }
        throw new NotFoundException("wrong user");
    }

    @Override
    public List<BookingOutputDto> getAllByBooker(Long userId, State state, Integer from, Integer size) {
        if (userRepository.findById(userId).isEmpty() || bookingRepository.findByUserId(userId).isEmpty()) {
            throw new NotFoundException("Can't found user with id " + userId);
        }
        UserEntity userEntity = userRepository.findById(userId).orElseThrow();
        if (from > 0) {
            from = from - 1;
        }
        Pageable pageable = PageRequest.of(from, size, Sort.by(Sort.Direction.DESC, "start"));
        List<BookingEntity> bookings = bookingRepository.findAllByBookerId(userEntity.getId(), pageable);
        List<BookingEntity> bookingEntityList = getBookingsByState(state, bookings);
        ArrayList<BookingOutputDto> bookingOutputDtos = new ArrayList<>();

        for (BookingEntity bookingEntity : bookingEntityList) {
            BookingOutputDto bookingOutputDto = new BookingOutputDto();
            ShortUserDto shortUserDto = new ShortUserDto(bookingEntity.getBookerId());
            ItemEntity itemEntity = itemRepository.findById(bookingEntity.getItemId())
                    .orElseThrow(() -> new NotFoundException("booking not found"));
            bookingOutputDto.setId(bookingEntity.getId());
            bookingOutputDto.setStart(bookingEntity.getStart());
            bookingOutputDto.setEnd(bookingEntity.getEnd());
            bookingOutputDto.setBooker(shortUserDto);
            bookingOutputDto.setStatus(bookingEntity.getStatus());
            ItemDto itemDto = ItemMapper.mapToItemDto(itemEntity);
            ShortItemDto shortItemDto = new ShortItemDto(itemDto.getId(), itemDto.getName());
            bookingOutputDto.setItem(shortItemDto);
            ((List<BookingOutputDto>) bookingOutputDtos).add(bookingOutputDto);
        }
        return bookingOutputDtos;
    }

    @Override
    public List<BookingOutputDto> getAllByOwner(Long userId, State state, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from, size, Sort.by(Sort.Direction.DESC, "start"));
        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException("Can't found user with id " + userId);
        }
        List<ItemEntity> itemEntitySet = itemRepository.findAllByOwnerId(userId);
        Set<BookingEntity> bookingEntitySet = new HashSet<>();
        for (ItemEntity itemEntity : itemEntitySet) {
            bookingEntitySet.addAll(bookingRepository.findAllByItemId(itemEntity.getId(), pageable));
        }
        List<BookingEntity> bookingEntities = new ArrayList<>(bookingEntitySet);
        bookingEntities = bookingEntities.stream()
                .sorted((b1, b2) -> b2.getStart().compareTo(b1.getStart()))
                .collect(Collectors.toList());

        List<BookingEntity> bookings = getBookingsByState(state, bookingEntities);
        List<BookingOutputDto> result = new ArrayList<>();

        for (BookingEntity bookingEntity : bookings) {
            BookingOutputDto bookingOutputDto = new BookingOutputDto();
            ShortUserDto shortUserDto = new ShortUserDto(bookingEntity.getBookerId());
            ItemEntity itemEntity = itemRepository.findById(bookingEntity.getItemId())
                    .orElseThrow(() -> new NotFoundException("booking not found"));
            bookingOutputDto.setId(bookingEntity.getId());
            bookingOutputDto.setStart(bookingEntity.getStart());
            bookingOutputDto.setEnd(bookingEntity.getEnd());
            bookingOutputDto.setBooker(shortUserDto);
            bookingOutputDto.setStatus(bookingEntity.getStatus());
            ItemDto itemDto = ItemMapper.mapToItemDto(itemEntity);
            ShortItemDto shortItemDto = new ShortItemDto(itemDto.getId(), itemDto.getName());
            bookingOutputDto.setItem(shortItemDto);
            result.add(bookingOutputDto);
        }
        return result;
    }


    public List<BookingEntity> getBookingsByState(State state, List<BookingEntity> bookings) {
        LocalDateTime currentTime = LocalDateTime.now();

        switch (state != null ? state : State.ALL) {
            case CURRENT:
                return bookingRepository.findByStartIsBeforeAndEndIsAfter(currentTime, currentTime);
            case PAST:
                return bookingRepository.findByEndIsBefore(currentTime);
            case FUTURE:
                return bookings;
            case WAITING:
                return bookingRepository.findByStatus(Status.WAITING);
            case REJECTED:
                return bookings.stream()
                        .filter(bookingEntity -> Status.REJECTED.equals(bookingEntity.getStatus()))
                        .collect(Collectors.toList());
            case ALL:
                return bookings;
            default:
                throw new BadRequestException("Unknown state: UNSUPPORTED_STATUS");
        }
    }
}
