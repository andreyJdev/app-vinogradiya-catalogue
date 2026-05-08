package ru.vinogradiya.models.dto.db;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public abstract class SelectionRepository {

    private String name;
}