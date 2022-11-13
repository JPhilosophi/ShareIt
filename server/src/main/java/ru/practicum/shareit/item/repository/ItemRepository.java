package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.ItemEntity;

import java.util.List;

public interface ItemRepository extends JpaRepository<ItemEntity, Long> {
    @Query(" select i from ItemEntity i " +
            "where i.available = TRUE and (upper(i.name) like upper(concat('%', ?1, '%')) " +
            " or upper(i.description) like upper(concat('%', ?1, '%')) )" +
            " order by i.id")
    List<ItemEntity> search(String text);

    @Query("select i from ItemEntity i where i.ownerId = ?1")
    List<ItemEntity> findAllByOwnerId(Long userId);

    @Query("select i from ItemEntity i where i.requestId = ?1")
    List<ItemEntity> findAllByRequestId(Long requestId);
}
