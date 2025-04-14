package ru.vinogradiya.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterValue {

    @Schema(description = "Значение фильтра")
    String value;

    @Schema(description = "Представление для отображения")
    String title;
}