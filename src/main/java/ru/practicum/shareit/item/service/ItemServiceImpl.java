package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.practicum.shareit.booking.model.BookingEntity;
import ru.practicum.shareit.booking.model.ShortBookingDto;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.error.BadRequestException;
import ru.practicum.shareit.error.NotFoundException;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.*;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.UserEntity;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service("ItemRepository")
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    public ItemServiceImpl(ItemRepository itemRepository,
                           UserRepository userRepository,
                           BookingRepository bookingRepository,
                           CommentRepository commentRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public ItemDto create(Long userId, ItemDto item) {
        UserEntity userEntity = userRepository
                .findById(userId)
                .orElseThrow(() -> new NotFoundException("Can't found user with id " + userId));
        item.setOwner(userEntity.getId());
        item.setRequestId(item.getRequestId());
        ItemEntity itemEntity = itemRepository.save(ItemMapper.mapToItemEntity(item));
        return ItemMapper.mapToItemDto(itemEntity);
    }

    @Override
    public ItemDto update(Long userId, Long itemId, ItemDto item) {
        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setOwnerId(userId);
        itemEntity.setId(itemId);
        itemEntity = itemRepository.findOne(Example.of(itemEntity))
                .orElseThrow(() -> new NotFoundException("You try change not your item"));
        ItemDto itemDto = ItemMapper.mapToItemDto(itemEntity);
        if (!StringUtils.hasText(item.getName()) && !StringUtils.hasText(item.getDescription())) {
            itemDto.setAvailable(item.getAvailable());
            itemRepository.saveAndFlush(ItemMapper.mapToItemEntity(itemDto));
            return itemDto;
        } else if (!StringUtils.hasText(item.getName()) && item.getAvailable() == null) {
            itemDto.setDescription(item.getDescription());
            itemRepository.saveAndFlush(ItemMapper.mapToItemEntity(itemDto));
            return itemDto;
        } else if (!StringUtils.hasText(item.getDescription()) && item.getAvailable() == null) {
            itemDto.setName(item.getName());
            itemRepository.saveAndFlush(ItemMapper.mapToItemEntity(itemDto));
            return itemDto;
        }

        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        itemRepository.saveAndFlush(ItemMapper.mapToItemEntity(itemDto));
        return itemDto;
    }

    @Override
    public List<ItemDto> get(Long userId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException("can't found items for user" + userId);
        }
        List<ItemEntity> itemEntityList = itemRepository.findAllByOwnerId(userId);
        List<Long> items = new ArrayList<>();
        for (ItemEntity itemEntity : itemEntityList) {
            items.add(itemEntity.getId());
        }
        List<BookingEntity> bookingEntities = bookingRepository.findByItemIdIn(items);
        List<ItemDto> result = new ArrayList<>();
        if (bookingEntities.isEmpty()) {
            return ItemMapper.mapToListItemDto(itemEntityList);
        } else {
            for (ItemEntity item : itemEntityList) {
                ItemDto itemDto = ItemMapper.mapToItemDto(item);
                ShortBookingDto lastShort = new ShortBookingDto();
                ShortBookingDto nextShort = new ShortBookingDto();
                List<BookingEntity> bookingEntityList = bookingRepository.findAllByItemId(item.getId());
                if (!bookingEntityList.isEmpty()) {
                    BookingEntity last = bookingRepository.findById(getLastBooking(bookingEntityList)
                            .getId()).orElse(null);
                    BookingEntity next = bookingRepository.findById(getNextBooking(bookingEntityList)
                            .getId()).orElse(null);
                    lastShort.setId(last.getId());
                    lastShort.setBookerId(last.getBookerId());
                    nextShort.setId(next.getId());
                    nextShort.setBookerId(next.getBookerId());
                    itemDto.setLastBooking(lastShort);
                    itemDto.setNextBooking(nextShort);
                } else {
                    itemDto.setLastBooking(null);
                    itemDto.setNextBooking(null);
                }

                result.add(itemDto);
            }
        }
        return result;
    }

    @Override
    public ItemDto getById(Long userId, Long itemId) {
        ItemEntity item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found"));
        List<BookingEntity> bookings = bookingRepository.findAllByItemId(itemId);
        ItemDto itemDto = ItemMapper.mapToItemDto(item);
        if (bookings.isEmpty()) {
            return itemDto;
        } else if (item.getOwnerId().equals(userId)) {
            ShortBookingDto lastShort = new ShortBookingDto();
            ShortBookingDto nextShort = new ShortBookingDto();
            Optional<BookingEntity> lastBookOp = Optional.ofNullable(getLastBooking(bookings));
            Optional<BookingEntity> nextBookOp = Optional.ofNullable(getNextBooking(bookings));
            if (lastBookOp.isPresent()) {
                lastShort.setId(lastBookOp.get().getId());
                lastShort.setBookerId(lastBookOp.get().getBookerId());
                itemDto.setLastBooking(lastShort);
            }
            if (nextBookOp.isPresent()) {
                nextShort.setId(nextBookOp.get().getId());
                nextShort.setBookerId(nextBookOp.get().getBookerId());
                itemDto.setNextBooking(nextShort);
            } else {
                itemDto.setLastBooking(null);
                itemDto.setNextBooking(null);
            }
        }
        List<CommentEntity> commentEntities = commentRepository.findAllByItemId(itemId);
        List<CommentDto> commentDtos = new ArrayList<>();
        for (CommentEntity comment : commentEntities) {
            commentDtos.add(CommentMapper.toCommentDto(comment));
        }
        itemDto.setComments(commentDtos);
        return itemDto;
    }

    @Override
    public List<ItemDto> search(Long userId, String text) {
        if (StringUtils.hasText(text)) {
            List<ItemEntity> itemEntities = itemRepository.search(text);
            return ItemMapper.mapToListItemDto(itemEntities);
        }
        return List.of();
    }

    @Override
    public CommentDto createComment(CommentInto comment, Long userId, Long itemId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("booking not found"));
        ItemEntity itemEntity = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("booking not found"));
        BookingEntity bookingEntity = bookingRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("booking not found"));
        if (!bookingEntity.getBookerId().equals(userId)) {
            throw new BadRequestException("Add comment to item 1 without booking failed");
        }
        if (Status.APPROVED.equals(bookingEntity.getStatus()) && bookingEntity.getEnd().isAfter(LocalDateTime.now())) {
            CommentEntity commentEntity = new CommentEntity();
            commentEntity.setItem(itemEntity);
            commentEntity.setAuthor(userEntity);
            commentEntity.setText(comment.getText());
            commentEntity.setCreated(LocalDateTime.now());
            commentEntity = commentRepository.save(commentEntity);
            CommentDto commentDto = new CommentDto();
            commentDto.setId(commentEntity.getId());
            commentDto.setAuthorName(commentEntity.getAuthor().getName());
            commentDto.setText(commentEntity.getText());
            commentDto.setCreated(commentEntity.getCreated());
            return commentDto;
        }
        throw new BadRequestException("Only bookmakers can create comments");
    }

    private BookingEntity getNextBooking(List<BookingEntity> bookings) {
        LocalDateTime current = LocalDateTime.now();

        return bookings.stream()
                .sorted()
                .filter(booking -> booking.getStart().isAfter(current))
                .findFirst().orElse(null);
    }

    private BookingEntity getLastBooking(List<BookingEntity> bookings) {
        LocalDateTime current = LocalDateTime.now();

        return bookings.stream()
                .sorted(Comparator.comparing(BookingEntity::getEnd).reversed())
                .filter(booking -> booking.getEnd().isBefore(current))
                .findFirst().orElse(null);
    }
}
