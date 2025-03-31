package ru.vinogradiya.utils.mapping;

import org.springframework.stereotype.Component;
import ru.vinogradiya.models.dto.ProductItemDto;
import ru.vinogradiya.models.entity.Product;

import java.util.Objects;

@Component
public class ProductsMapper implements CatalogueMapper<ProductItemDto> {

    @Override
    public ProductItemDto toDomain(Product p) {
        if (p == null) {
            return null;
        }

        ProductItemDto.Selection selection = Objects.nonNull(p.getSelection()) && Objects.nonNull(p.getSelection().getName()) ?
                ProductItemDto.Selection.of(p.getSelection().getName()) :
                null;
        return ProductItemDto.builder()
                .id(p.getId())
                .name(p.getName())
                .selection(selection)
                .time(p.getTime())
                .strength(p.getStrength())
                .cluster(p.getCluster())
                .berry(p.getBerry())
                .taste(p.getTaste())
                .resistanceCold(p.getResistanceCold())
                .priceSeed(p.getPriceSeed())
                .priceCut(p.getPriceCut())
                .image(p.getImage())
                .description(p.getDescription())
                .selectionMini(p.getSelectionMini())
                .availableSeed(p.getAvailableSeed())
                .availableCut(p.getAvailableCut())
                .soldCut(p.getSoldCut())
                .soldSeed(p.getSoldSeed())
                .build();
    }
}