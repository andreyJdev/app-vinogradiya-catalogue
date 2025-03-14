package ru.vinogradiya.models.dto;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Элемент получения сорта винограда")
public class ProductItemDto {

    @JsonView(ProductItemViews.Private.class)
    @Schema(description = "Идентификатор сорта")
    private Long id;

    @JsonView(ProductItemViews.UserAccess.class)
    @Schema(description = "Имя сорта винограда")
    private String name;

    @JsonView(ProductItemViews.UserAccess.class)
    @Schema(description = "Время созревания")
    private String time;

    @JsonView(ProductItemViews.UserAccess.class)
    @Schema(description = "Сила роста")
    private String strength;

    @JsonView(ProductItemViews.UserAccess.class)
    @Schema(description = "Характеристики кисти")
    private String cluster;

    @JsonView(ProductItemViews.UserAccess.class)
    @Schema(description = "Характеристики ягоды")
    private String berry;

    @JsonView(ProductItemViews.UserAccess.class)
    @Schema(description = "Вкус ягоды")
    private String taste;

    @JsonView(ProductItemViews.UserAccess.class)
    @Schema(description = "Стойкость к морозу")
    private Integer resistanceCold;

    @JsonView(ProductItemViews.UserAccess.class)
    @Schema(description = "Цена саженца")
    private Integer priceSeed;

    @JsonView(ProductItemViews.UserAccess.class)
    @Schema(description = "Цена черенка")
    private Integer priceCut;

    @JsonView(ProductItemViews.UserAccess.class)
    @Schema(description = "Изображение")
    private String image;

    @JsonView(ProductItemViews.UserAccess.class)
    @Schema(description = "Описание")
    private String description;

    @JsonView(ProductItemViews.UserAccess.class)
    @Schema(description = "Мини селекция (если нет основной)")
    private String selectionMini;

    @JsonView(ProductItemViews.AdminAccess.class)
    @Schema(description = "Доступно саженцев")
    private Integer availableSeed;

    @JsonView(ProductItemViews.AdminAccess.class)
    @Schema(description = "Доступно черенков")
    private Integer availableCut;

    @JsonView(ProductItemViews.AdminAccess.class)
    @Schema(description = "Продано саженцев")
    private Integer soldSeed;

    @JsonView(ProductItemViews.AdminAccess.class)
    @Schema(description = "Продано черенков")
    private Integer soldCut;

    @JsonView(ProductItemViews.UserAccess.class)
    @Schema(name = "Имя селекции винограда")
    private String selection;
}