package ru.vinogradiya.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.Generated;

import java.util.Objects;
import java.util.Optional;

@Data
public class ProductFilterRequest {

    @Schema(description = "Поисковая строка")
    String search;

    @Schema(description = "Параметры фильтрации")
    @Valid
    ProductFilter filterParams;

    @Generated
    public String getSearch() {
        return Optional.ofNullable(search)
                .filter(s -> !Objects.equals(s.trim(), ""))
                .orElse(null);
    }
}