package ru.vinogradiya.utils.mapping;

import org.springframework.stereotype.Component;
import ru.vinogradiya.models.dto.SelectionItemDto;
import ru.vinogradiya.models.entity.Selection;

@Component
public class SelectionItemMapper implements ItemMapper<Selection, SelectionItemDto> {

    @Override
    public SelectionItemDto toDomain(Selection s) {
        if (s == null) {
            return null;
        }

        SelectionItemDto.Products products = SelectionItemDto.Products.of(s.getProducts());

        return SelectionItemDto.builder()
                .id(s.getId())
                .name(s.getName())
                .products(products)
                .build();
    }
}