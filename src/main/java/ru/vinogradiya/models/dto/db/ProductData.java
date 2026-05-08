package ru.vinogradiya.models.dto.db;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode
public abstract class ProductData {

    private String time;
    private String strength;
    private String cluster;
    private String berry;
    private String taste;
    private Integer resistanceCold;
    private BigDecimal priceSeed;
    private BigDecimal priceCut;
    private String image;
    private String description;
    private String selectionMini;
    private Integer availableSeed;
    private Integer availableCut;
    private Integer soldSeed;
    private Integer soldCut;
    private UUID selectionId;
}