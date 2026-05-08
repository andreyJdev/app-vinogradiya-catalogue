package ru.vinogradiya.models.dto.db;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class SelectionCreateData extends SelectionRepository {

    private UUID id;

    public SelectionCreateData() {
        this.id = UUID.randomUUID();
    }
}