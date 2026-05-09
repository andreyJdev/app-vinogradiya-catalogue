package ru.vinogradiya.models.dto.response;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.vinogradiya.models.entity.Product;
import ru.vinogradiya.utils.mapping.ItemViews;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Элемент получения селекции")
public class SelectionItem {

    @JsonView(ItemViews.Private.class)
    @Schema(description = "Идентификатор селекции")
    private UUID id;

    @JsonView(ItemViews.UserAccess.class)
    @Schema(description = "Название селекции")
    private String name;

    @Schema(description = "Названия продуктов, пренадлежащих селекции")
    private ProductsDto products;

    public record ProductsDto(@JsonView(ItemViews.UserAccess.class)
                              List<String> names) {

        public static SelectionItem.ProductsDto of(List<Product> products) {
            List<String> names = (products == null || products.isEmpty())
                    ? Collections.emptyList()
                    : products.stream().map(Product::getName).toList();
            return new SelectionItem.ProductsDto(names);
        }
    }
}