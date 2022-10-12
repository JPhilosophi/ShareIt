package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<BookingEntity, Long> {

    @Query("select b from BookingEntity b where b.booker_id = ?1 order by b.start DESC")
    List<BookingEntity> findAllByBooker_idOrderByStartDesc (Long booker_id);

    @Query("select b from BookingEntity b where b.item_id = ?1")
    List<BookingEntity> findAllByItem_id(Long item);

    @Query(" select i from BookingEntity i where i.id = :userId")
    Optional<BookingEntity> findByUserId(Long userId);

    @Query("select b from BookingEntity b where b.item_id = ?1")
    Optional<BookingEntity> findByItem_id(Long itemId);

    @Query("select b from BookingEntity b where b.item_id in ?1")
    List<BookingEntity> findByItem_idIn(List<Long> itemIdList);

    List<BookingEntity> findByStartIsBeforeAndEndIsAfter(LocalDateTime start, LocalDateTime end);

    List<BookingEntity> findByEndIsBefore(LocalDateTime end);

    @Query("select b from BookingEntity b where b.start > ?1")
    List<BookingEntity> findByStartIsAfter(LocalDateTime start);

    List<BookingEntity> findByStatus(Status status);

    @Query(nativeQuery = true, value = "select b.* from bookings as b where b.item_id = ?1 order by b.end_date DESC LIMIT 1")
    Optional<BookingEntity> findByItem_idOrderByEndDesc(Long itemId);

    @Query(nativeQuery = true, value = "select b.* from bookings as b where b.item_id = ?1 order by b.end_date DESC LIMIT 1")
    Optional<BookingEntity> findByItem_idOrderByStartDesc(Long itemId);

}
