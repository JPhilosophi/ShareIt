package ru.practicum.shareit.booking.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "bookings")
public class BookingEntity implements Comparable<BookingEntity> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "start_date")
    private LocalDateTime start;
    @Column(name = "end_date")
    private LocalDateTime end;
    @Enumerated(value = EnumType.STRING)
    private Status status;
    @Column(name = "booker_id")
    private Long bookerId;
    @Column(name = "item_id")
    private Long itemId;


    @Override
    public int compareTo(BookingEntity o) {
        return this.start.compareTo(o.start) * (-1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        BookingEntity that = (BookingEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
