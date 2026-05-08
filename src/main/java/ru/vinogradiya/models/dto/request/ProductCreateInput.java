package ru.vinogradiya.models.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.vinogradiya.models.entity.Product;
import ru.vinogradiya.utils.validation.annotation.UniqueNameConstraint;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Запрос на добавление сорта винограда")
public class ProductCreateInput extends ProductInput {

    @Schema(description = "Название сорта")
    @UniqueNameConstraint(table = Product.TABLE_NAME, message = "{vinogradiya.catalogue.product.unique_name}")
    @NotNull(message = "{vinogradiya.catalogue.base.not_null}")
    @NotBlank(message = "{vinogradiya.catalogue.base.not_empty}")
    @Size(min = 2, max = 32, message = "{vinogradiya.catalogue.base.size}")
    private String name;
}