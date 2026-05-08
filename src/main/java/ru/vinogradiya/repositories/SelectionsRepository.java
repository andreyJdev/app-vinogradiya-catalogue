package ru.vinogradiya.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.vinogradiya.models.dto.db.SelectionCreateData;
import ru.vinogradiya.models.entity.Selection;

import java.util.UUID;

@Repository
public interface SelectionsRepository extends JpaRepository<Selection, UUID> {

    @Query(value = """
            INSERT INTO selection (id, name)
                    VALUES (:#{#data.id},
                            :#{#data.name})
            """, nativeQuery = true)
    @Modifying
    void create(SelectionCreateData data);
}