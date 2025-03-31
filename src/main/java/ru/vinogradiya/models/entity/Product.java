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

    @Id
    @Column(name = "id")
    private UUID id;

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
}