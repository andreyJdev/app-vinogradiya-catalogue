package ru.vinogradiya.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vinogradiya.models.Selection;

@Repository
public interface SelectionsRepository extends JpaRepository<Selection, Long> { }
