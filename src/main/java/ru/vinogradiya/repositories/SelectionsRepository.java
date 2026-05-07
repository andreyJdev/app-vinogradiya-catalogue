package ru.vinogradiya.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.vinogradiya.models.dto.SelectionCreateDto;
import ru.vinogradiya.models.entity.Selection;

import java.util.UUID;

@Repository
public interface SelectionsRepository extends JpaRepository<Selection, UUID> {

    @Query(value = """
            INSERT INTO selection (id, name)
                    VALUES (:#{#dto.id},
                            :#{#dto.name})
            """, nativeQuery = true)
    @Modifying
    void create(SelectionCreateDto dto);
}