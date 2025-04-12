package ru.vinogradiya.models.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product")
public class Product {

    public static final String TABLE_NAME = "product";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String TIME = "time";
    public static final String STRENGTH = "strength";
    public static final String CLUSTER = "cluster";
    public static final String BERRY = "berry";
    public static final String TASTE = "taste";
    public static final String RESISTANCE_COLD = "resistance_cold";
    public static final String PRICE_SEED = "price_seed";
    public static final String PRICE_CUT = "price_cut";
    public static final String IMAGE = "image";
    public static final String DESCRIPTION = "description";
    public static final String SELECTION_MINI = "selection_mini";
    public static final String AVAILABLE_SEED = "available_seed";
    public static final String AVAILABLE_CUT = "available_cut";
    public static final String SOLD_SEED = "sold_seed";
    public static final String SOLD_CUT = "sold_cut";
    public static final String SELECTION_ID = "selection_id";

    @Id
    @Column(name = ID)
    private UUID id;

    @Column(name = NAME, unique = true)
    private String name;

    @Column(name = TIME)
    private String time;

    @Column(name = STRENGTH)
    private String strength;

    @Column(name = CLUSTER)
    private String cluster;

    @Column(name = BERRY)
    private String berry;

    @Column(name = TASTE)
    private String taste;

    @Column(name = RESISTANCE_COLD)
    private Integer resistanceCold;

    @Column(name = PRICE_SEED)
    private Integer priceSeed;

    @Column(name = PRICE_CUT)
    private Integer priceCut;

    @Column(name = IMAGE)
    private String image;

    @Column(name = DESCRIPTION)
    private String description;

    @Column(name = SELECTION_MINI)
    private String selectionMini;

    @Column(name = AVAILABLE_SEED)
    private Integer availableSeed;

    @Column(name = AVAILABLE_CUT)
    private Integer availableCut;

    @Column(name = SOLD_SEED)
    private Integer soldSeed;

    @Column(name = SOLD_CUT)
    private Integer soldCut;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = SELECTION_ID, referencedColumnName = Selection.ID)
    Selection selection;
}