package ru.vinogradiya.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Builder
public class ProductFilter {

    @Schema(description = "Название селекции")
    List<String> selections;

    public List<String> getSelections() {
        return Optional.ofNullable(selections)
                .map(selectionsList -> selectionsList.stream()
                        .filter(selection -> Objects.nonNull(selection) && !selection.isBlank())
                        .toList())
                .orElse(Collections.emptyList());
    }
}