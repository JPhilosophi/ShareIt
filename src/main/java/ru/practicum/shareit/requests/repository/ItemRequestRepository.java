package ru.practicum.shareit.requests.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.requests.model.ItemRequestEntity;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequestEntity, Long> {


    @Query("select i from ItemRequestEntity i where i.requesterId <> ?1")
    List<ItemRequestEntity> findAllByRequesterIdNot(Long id, Pageable pageable);

}
