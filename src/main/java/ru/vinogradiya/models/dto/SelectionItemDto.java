package ru.vinogradiya.models.dto;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import ru.vinogradiya.models.entity.Product;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@Schema(description = "Элемент получения селекции")
public class SelectionItemDto {

    @JsonView(ItemViews.Private.class)
    @Schema(description = "Идентификатор селекции")
    private UUID id;

    @JsonView(ItemViews.UserAccess.class)
    @Schema(description = "Название селекции")
    private String name;

    @Schema(description = "Названия продуктов, пренадлежащих селекции")
    private Products products;

    public record Products(@JsonView(ItemViews.UserAccess.class)
                           List<String> names) {

        public static SelectionItemDto.Products of(List<Product> products) {
            List<String> names = (products == null || products.isEmpty())
                    ? Collections.emptyList()
                    : products.stream().map(Product::getName).toList();
            return new SelectionItemDto.Products(names);
        }
    }
}