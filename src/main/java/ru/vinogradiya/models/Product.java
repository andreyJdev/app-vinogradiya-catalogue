package ru.vinogradiya.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Data
@EqualsAndHashCode
@NoArgsConstructor
@Table(name = "product")
public class Product implements Comparable<Product> {
    @Id
    @Column(name = "id")
    @SequenceGenerator(
            name = "product_sequence",
            sequenceName = "product_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "product_sequence"
    )
    private Long id;

    @Column(name = "name", unique = true)
    //@UniqueName(message = "Выберите другое Название сорта, это занято")
    @NotNull(message = "Поле не должно быть пустым")
    @NotBlank(message = "Обязательное заполнение Названия сорта")
    @Size(max = 32, message = "Не больше 100 символов")
    private String name;

    @Column(name = "time")
    @NotNull(message = "Поле не должно быть пустым")
    @NotEmpty(message = "Заполните Время созревания")
    @Size(max = 32, message = "Не больше 100 символов")
    private String time;

    @Column(name = "strength")
    @NotNull(message = "Поле не должно быть пустым")
    @NotEmpty(message = "Заполните Сила роста")
    @Size(max = 32, message = "Не больше 100 символов")
    private String strength;

    @Column(name = "cluster")
    @NotNull(message = "Поле не должно быть пустым")
    @NotEmpty(message = "Заполните Гроздь")
    @Size(max = 32, message = "Не больше 100 символов")
    private String cluster;

    @Column(name = "berry")
    @NotNull(message = "Поле не должно быть пустым")
    @NotEmpty(message = "Заполните Ягода")
    @Size(max = 32, message = "Не больше 100 символов")
    private String berry;

    @Column(name = "taste")
    @NotNull(message = "Поле не должно быть пустым")
    @NotEmpty(message = "Заполните Вкус и консистенция мякоти")
    @Size(max = 64, message = "Не больше 100 символов")
    private String taste;

    @Column(name = "resistance_cold")
    @NotNull(message = "Поле не должно быть пустым")
    private Integer resistanceCold;

    @Column(name = "price_seed")
    @NotNull(message = "Поле не должно быть пустым")
    private Integer priceSeed;

    @Column(name = "price_cut")
    @NotNull(message = "Поле не должно быть пустым")
    private Integer priceCut;

    @Column(name = "image")
    @Size(max = 128, message = "Не больше 100 символов")
    private String image;

    @Column(name = "description")
    @Size(max = 2048, message = "Не больше 2048 символов")
    private String description;

    @Column(name = "selection_mini")
    @Size(max = 32, message = "Не больше 100 символов")
    private String selectionMini;

    @Column(name = "available_seed")
    private Integer availableSeed;

    @Column(name = "available_cut")
    private Integer availableCut;

    @Column(name = "sold_seed")
    private Integer soldSeed;

    @Column(name = "sold_cut")
    private Integer soldCut;

    @ManyToOne
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

    @Override
    public int compareTo(Product product) {
        return this.name.compareTo(product.getName());
    }
}
