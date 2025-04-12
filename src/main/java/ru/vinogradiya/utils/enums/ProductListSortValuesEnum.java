package ru.vinogradiya.utils.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductListSortValuesEnum {

    ID("id", "id"),
    NAME("name", "name"),
    TIME("time", "time"),
    STRENGTH("strength", "strength"),
    CLUSTER("cluster", "cluster"),
    BERRY("berry", "berry"),
    TASTE("taste", "taste"),
    RESISTANCE_COLD("resistanceCold", "resistance_cold"),
    PRICE_SEED("priceSeed", "price_seed"),
    PRICE_CUT("priceCut", "price_cut"),
    IMAGE("image", "image"),
    DESCRIPTION("description", "description"),
    SELECTION_MINI("selectionMini", "selection_mini"),
    AVAILABLE_SEED("availableSeed", "available_seed"),
    AVAILABLE_CUT("availableCut", "available_cut"),
    SOLD_SEED("soldSeed", "sold_seed"),
    SOLD_CUT("soldCut", "sold_cut"),
    SELECTION("selectionName", "selection_name");

    private final String sort;
    private final String sqlName;
}