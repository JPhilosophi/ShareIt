package ru.practicum.shareit.item.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Data
@Entity
@Table(name = "items")
public class ItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = 0L;
    @NotNull(message = "Name can't be null")
    @NotEmpty(message = "Name can't be empty")
    private String name;
    @NotNull(message = "Description can't be null")
    @NotEmpty(message = "Description can't be empty")
    private String description;
    @Column(name = "is_available")
    private Boolean available;
    @Column(name = "owner_id")
    private Long ownerId;
    @Column(name = "request_id")
    private Long requestId;
    @Transient
    private Long lastBookingId;
    @Transient
    private Long nextBookingId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ItemEntity that = (ItemEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
