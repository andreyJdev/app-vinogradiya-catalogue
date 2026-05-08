package ru.vinogradiya.utils.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import ru.vinogradiya.models.dto.db.ProductCreateData;
import ru.vinogradiya.models.dto.db.ProductUpdateData;
import ru.vinogradiya.models.dto.db.SelectionCreateData;
import ru.vinogradiya.models.dto.request.ProductCreateInput;
import ru.vinogradiya.models.dto.request.ProductUpdateInput;
import ru.vinogradiya.models.dto.request.SelectionCreateInput;
import ru.vinogradiya.models.dto.response.ProductItem;
import ru.vinogradiya.models.dto.response.SelectionItem;
import ru.vinogradiya.models.entity.Product;
import ru.vinogradiya.models.entity.Selection;
import ru.vinogradiya.utils.dto.InputDtoMethods;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static ru.vinogradiya.utils.dto.InputDtoMethods.blankToNull;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ItemMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "time", target = "time")
    @Mapping(source = "strength", target = "strength")
    @Mapping(source = "cluster", target = "cluster")
    @Mapping(source = "berry", target = "berry")
    @Mapping(source = "taste", target = "taste")
    @Mapping(source = "resistanceCold", target = "resistanceCold")
    @Mapping(source = "priceSeed", target = "priceSeed")
    @Mapping(source = "priceCut", target = "priceCut")
    @Mapping(source = "image", target = "image")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "selectionMini", target = "selectionMini")
    @Mapping(source = "availableSeed", target = "availableSeed")
    @Mapping(source = "availableCut", target = "availableCut")
    @Mapping(source = "soldSeed", target = "soldSeed")
    @Mapping(source = "soldCut", target = "soldCut")
    @Mapping(source = "selection", target = "selection", qualifiedByName = "toSelectionDtoField")
    ProductItem toDomain(Product product);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "products", target = "products", qualifiedByName = "toProductsDtoField")
    SelectionItem toDomain(Selection selection);

    @Mapping(source = "name", target = "name", qualifiedByName = "toUpperFirst")
    @Mapping(source = "time", target = "time", qualifiedByName = "toUpperFirst")
    @Mapping(source = "strength", target = "strength", qualifiedByName = "toUpperFirst")
    @Mapping(source = "cluster", target = "cluster", qualifiedByName = "toUpperFirst")
    @Mapping(source = "berry", target = "berry", qualifiedByName = "toUpperFirst")
    @Mapping(source = "taste", target = "taste", qualifiedByName = "toUpperFirst")
    @Mapping(source = "resistanceCold", target = "resistanceCold", qualifiedByName = "toValidatedNumber")
    @Mapping(source = "priceSeed", target = "priceSeed", qualifiedByName = "toValidatedFinance")
    @Mapping(source = "priceCut", target = "priceCut", qualifiedByName = "toValidatedFinance")
    @Mapping(source = "image", target = "image", qualifiedByName = "toUpperFirst")
    @Mapping(source = "description", target = "description", qualifiedByName = "toUpperFirst")
    @Mapping(source = "selectionMini", target = "selectionMini", qualifiedByName = "toUpperFirst")
    @Mapping(source = "availableSeed", target = "availableSeed", qualifiedByName = "toValidatedNumber")
    @Mapping(source = "availableCut", target = "availableCut", qualifiedByName = "toValidatedNumber")
    @Mapping(source = "soldSeed", target = "soldSeed", qualifiedByName = "toValidatedNumber")
    @Mapping(source = "soldCut", target = "soldCut", qualifiedByName = "toValidatedNumber")
    @Mapping(source = "selectionId", target = "selectionId", qualifiedByName = "toSelectionId")
    ProductCreateData toCreate(ProductCreateInput product);

    @Mapping(source = "name", target = "name", qualifiedByName = "toUpperFirst")
    @Mapping(source = "time", target = "time", qualifiedByName = "toUpperFirst")
    @Mapping(source = "strength", target = "strength", qualifiedByName = "toUpperFirst")
    @Mapping(source = "cluster", target = "cluster", qualifiedByName = "toUpperFirst")
    @Mapping(source = "berry", target = "berry", qualifiedByName = "toUpperFirst")
    @Mapping(source = "taste", target = "taste", qualifiedByName = "toUpperFirst")
    @Mapping(source = "resistanceCold", target = "resistanceCold", qualifiedByName = "toValidatedNumber")
    @Mapping(source = "priceSeed", target = "priceSeed", qualifiedByName = "toValidatedFinance")
    @Mapping(source = "priceCut", target = "priceCut", qualifiedByName = "toValidatedFinance")
    @Mapping(source = "image", target = "image", qualifiedByName = "toUpperFirst")
    @Mapping(source = "description", target = "description", qualifiedByName = "toUpperFirst")
    @Mapping(source = "selectionMini", target = "selectionMini", qualifiedByName = "toUpperFirst")
    @Mapping(source = "availableSeed", target = "availableSeed", qualifiedByName = "toValidatedNumber")
    @Mapping(source = "availableCut", target = "availableCut", qualifiedByName = "toValidatedNumber")
    @Mapping(source = "soldSeed", target = "soldSeed", qualifiedByName = "toValidatedNumber")
    @Mapping(source = "soldCut", target = "soldCut", qualifiedByName = "toValidatedNumber")
    @Mapping(source = "selectionId", target = "selectionId", qualifiedByName = "toSelectionId")
    ProductUpdateData toUpdate(ProductUpdateInput product);

    SelectionCreateData toCreate(SelectionCreateInput product);

    @Named("toSelectionDtoField")
    default ProductItem.SelectionDto toSelectionDtoField(Selection selection) {
        return ProductItem.SelectionDto.of(selection);
    }

    @Named("toProductsDtoField")
    default SelectionItem.ProductsDto toProductsDtoField(List<Product> products) {
        return SelectionItem.ProductsDto.of(products);
    }

    @Named("toUpperFirst")
    default String toUpperFirst(String text) {
        return InputDtoMethods.upperFirst(InputDtoMethods.blankToNull(text));
    }

    @Named("toValidatedNumber")
    default Integer toValidatedNumber(String number) {
        return InputDtoMethods.getNumber(number);
    }

    @Named("toValidatedFinance")
    default BigDecimal toValidatedFinance(String bigDecimal) {
        return InputDtoMethods.getFinance(bigDecimal);
    }

    @Named("toSelectionId")
    default UUID toSelectionId(String uuid) {
        return Optional.ofNullable(blankToNull(uuid))
                .map(UUID::fromString).orElse(null);
    }
}