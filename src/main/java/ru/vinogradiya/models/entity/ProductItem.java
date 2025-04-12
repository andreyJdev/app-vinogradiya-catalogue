package ru.vinogradiya.models.entity;

import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.SqlResultSetMapping;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@SqlResultSetMapping(name = "ProductItemMapping",
        classes = {@ConstructorResult(targetClass = ProductItem.class,
                columns = {
                        @ColumnResult(name = "id", type = UUID.class),
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "time", type = String.class),
                        @ColumnResult(name = "strength", type = String.class),
                        @ColumnResult(name = "cluster", type = String.class),
                        @ColumnResult(name = "berry", type = String.class),
                        @ColumnResult(name = "taste", type = String.class),
                        @ColumnResult(name = "resistance_cold", type = Integer.class),
                        @ColumnResult(name = "price_seed", type = Integer.class),
                        @ColumnResult(name = "price_cut", type = Integer.class),
                        @ColumnResult(name = "image", type = String.class),
                        @ColumnResult(name = "description", type = String.class),
                        @ColumnResult(name = "selection_mini", type = String.class),
                        @ColumnResult(name = "available_seed", type = Integer.class),
                        @ColumnResult(name = "available_cut", type = Integer.class),
                        @ColumnResult(name = "sold_seed", type = Integer.class),
                        @ColumnResult(name = "sold_cut", type = Integer.class),
                        @ColumnResult(name = "selection_id", type = UUID.class),
                })})
public class ProductItem {

    @Id
    UUID id;
    String name;
    String time;
    String strength;
    String cluster;
    String berry;
    String taste;
    Integer resistanceCold;
    Integer priceSeed;
    Integer priceCut;
    String image;
    String description;
    String selectionMini;
    Integer availableSeed;
    Integer availableCut;
    Integer soldSeed;
    Integer soldCut;
    UUID selectionId;
}