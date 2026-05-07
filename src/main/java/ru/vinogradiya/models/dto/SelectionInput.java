package ru.vinogradiya.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Generated;
import ru.vinogradiya.models.entity.Selection;
import ru.vinogradiya.utils.validation.annotation.UniqueNameConstraint;

import static ru.vinogradiya.utils.dto.InputDtoMethods.upperFirst;

@Data
public abstract class SelectionInput {

    @Schema(description = "Название селекции")
    @UniqueNameConstraint(table = Selection.TABLE_NAME, message = "{vinogradiya.catalogue.selection.unique_name}")
    @NotNull(message = "{vinogradiya.catalogue.base.not_null}")
    @NotBlank(message = "{vinogradiya.catalogue.base.not_empty}")
    @Size(min = 2, max = 64, message = "{vinogradiya.catalogue.base.size}")
    private String name;

    @Generated
    public String getName() {
        return upperFirst(this.name);
    }

    @Generated
    public void setName(String name) {
        this.name = name;
    }
}
