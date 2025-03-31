package ru.vinogradiya.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Generated;
import ru.vinogradiya.models.entity.Product_;
import ru.vinogradiya.utils.validation.annotation.UniqueNameConstraint;

@Schema(description = "Запрос на добавление сорта винограда")
public class ProductCreateDto extends ProductInput {

    @UniqueNameConstraint(table = "product", column = Product_.NAME, message = "Сорт с именем: {0} уже существует")
    @NotNull
    @Size(min = 2, max = 32)
    @Schema(description = "Название сорта")
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