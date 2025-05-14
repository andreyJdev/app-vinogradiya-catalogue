package ru.vinogradiya.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Generated;
import ru.vinogradiya.models.entity.Product_;
import ru.vinogradiya.utils.common.exception.ApiException;
import ru.vinogradiya.utils.enums.GlobalErrorMessage;
import ru.vinogradiya.utils.validation.annotation.UniqueNameUpdateConstraint;

import java.util.Optional;
import java.util.UUID;

@UniqueNameUpdateConstraint(table = "product", column = Product_.NAME)
public class ProductUpdateDto extends ProductInput {

    @JsonIgnore
    @NotNull(message = "vinogradiya.catalogue.base.not_null")
    @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-4[0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$", message = "{vinogradiya.catalogue.base.uuid}")
    @Schema(description = "Идентификатор сорта")
    private final String id;

    @NotNull(message = "{vinogradiya.catalogue.base.not_null}")
    @Size(min = 2, max = 32, message = "{vinogradiya.catalogue.base.size}")
    @Schema(description = "Название сорта")
    private String name;

    public ProductUpdateDto(String id) {
        this.id = id;
    }

    @Generated
    public UUID getId() {
        return Optional.ofNullable(blankToNull(this.id)).map(UUID::fromString)
                .orElseThrow(() -> new ApiException(GlobalErrorMessage.INTERNAL_ERROR));
    }

    @Generated
    public String getName() {
        return upperFirst(this.name);
    }

    @Generated
    public void setName(String name) {
        this.name = name;
    }
}