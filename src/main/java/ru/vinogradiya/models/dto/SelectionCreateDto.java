package ru.vinogradiya.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Generated;
import ru.vinogradiya.utils.common.exception.ApiException;
import ru.vinogradiya.utils.enums.GlobalErrorMessage;

import java.util.Optional;
import java.util.UUID;

import static ru.vinogradiya.utils.dto.InputDtoMethods.blankToNull;

@Schema(description = "Запрос на добавление селекции")
public class SelectionCreateDto extends SelectionInput {

    @JsonIgnore
    @Schema(description = "Идентификатор селекции")
    private final String id;

    public SelectionCreateDto() {
        this.id = UUID.randomUUID().toString();
    }

    @Generated
    public UUID getId() {
        return Optional.ofNullable(blankToNull(this.id))
                .map(UUID::fromString)
                .orElseThrow(() -> new ApiException(GlobalErrorMessage.INTERNAL_ERROR));
    }
}