package ru.vinogradiya.models.dto.db;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class ProductCreateData extends ProductData {

    private UUID id;
    private String name;

    public ProductCreateData() {
        this.id = UUID.randomUUID();
    }
}