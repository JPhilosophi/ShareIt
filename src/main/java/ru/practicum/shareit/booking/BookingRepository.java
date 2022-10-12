package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<BookingEntity, Long> {

    @Query("select b from BookingEntity b where b.bookerId = ?1 order by b.start DESC")
    List<BookingEntity> findAllByBookerIdOrderByStartDesc(Long bookerId);

    @Query("select b from BookingEntity b where b.itemId = ?1")
    List<BookingEntity> findAllByItemId(Long item);

    @Query(" select i from BookingEntity i where i.id = :userId")
    Optional<BookingEntity> findByUserId(Long userId);

    @Query("select b from BookingEntity b where b.itemId = ?1")
    Optional<BookingEntity> findByItemId(Long itemId);

    @Query("select b from BookingEntity b where b.itemId in ?1")
    List<BookingEntity> findByItemIdIn(List<Long> itemIdList);

    List<BookingEntity> findByStartIsBeforeAndEndIsAfter(LocalDateTime start, LocalDateTime end);

    List<BookingEntity> findByEndIsBefore(LocalDateTime end);

    @Query("select b from BookingEntity b where b.start > ?1")
    List<BookingEntity> findByStartIsAfter(LocalDateTime start);

    List<BookingEntity> findByStatus(Status status);

    @Query(nativeQuery = true, value = "select b.* from bookings as b where b.item_id = ?1 order by b.end_date DESC LIMIT 1")
    Optional<BookingEntity> findByItemIdOrderByEndDesc(Long itemId);

    @Query(nativeQuery = true, value = "select b.* from bookings as b where b.item_id = ?1 order by b.end_date DESC LIMIT 1")
    Optional<BookingEntity> findByItemIdOrderByStartDesc(Long itemId);

}
