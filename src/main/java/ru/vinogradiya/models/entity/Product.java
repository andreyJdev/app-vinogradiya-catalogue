package ru.vinogradiya.models.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "product")
public class Product {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "time")
    private String time;

    @Column(name = "strength")
    private String strength;

    @Column(name = "cluster")
    private String cluster;

    @Column(name = "berry")
    private String berry;

    @Column(name = "taste")
    private String taste;

    @Column(name = "resistance_cold")
    private Integer resistanceCold;

    @Column(name = "price_seed")
    private Integer priceSeed;

    @Column(name = "price_cut")
    private Integer priceCut;

    @Column(name = "image")
    private String image;

    @Column(name = "description")
    private String description;

    @Column(name = "selection_mini")
    private String selectionMini;

    @Column(name = "available_seed")
    private Integer availableSeed;

    @Column(name = "available_cut")
    private Integer availableCut;

    @Column(name = "sold_seed")
    private Integer soldSeed;

    @Column(name = "sold_cut")
    private Integer soldCut;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "selection_id", referencedColumnName = "id")
    Selection selection;

    public Product(String strength, String name, String time,
                   String cluster, String berry,
                   String taste, Integer resistanceCold,
                   Integer priceSeed, Integer priceCut,
                   String image, String description, String selectionMini,
                   Integer availableSeed, Integer availableCut,
                   Integer soldSeed, Integer soldCut,
                   Selection selection) {
        this.time = time;
        this.strength = strength;
        this.name = name;
        this.cluster = cluster;
        this.berry = berry;
        this.taste = taste;
        this.resistanceCold = resistanceCold;
        this.priceSeed = priceSeed;
        this.priceCut = priceCut;
        this.image = image;
        this.description = description;
        this.selectionMini = selectionMini;
        this.availableSeed = availableSeed;
        this.availableCut = availableCut;
        this.soldSeed = soldSeed;
        this.soldCut = soldCut;
        this.selection = selection;
    }
}