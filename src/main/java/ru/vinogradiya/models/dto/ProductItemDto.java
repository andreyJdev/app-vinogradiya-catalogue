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
    @Schema(description = "Название сорта")
    private String name;

    @JsonView(ProductItemViews.UserAccess.class)
    @Schema(description = "Время созревания")
    private String time;

    @JsonView(ProductItemViews.UserAccess.class)
    @Schema(description = "Сила роста")
    private String strength;

    @JsonView(ProductItemViews.UserAccess.class)
    @Schema(description = "Описание особенностей грозди")
    private String cluster;

    @JsonView(ProductItemViews.UserAccess.class)
    @Schema(description = "Описание особенностей ягоды")
    private String berry;

    @JsonView(ProductItemViews.UserAccess.class)
    @Schema(description = "Описание вкусовых особенностей")
    private String taste;

    @JsonView(ProductItemViews.UserAccess.class)
    @Schema(description = "Значение морозостойкости")
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
    @Schema(description = "Название мини селекции (если есть)")
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

    @Schema(description = "Название селекции")
    private Selection selection;

    public record Selection(@JsonView(ProductItemViews.UserAccess.class)
                            String name) {

        public static Selection of(String name) {
            return new Selection(name);
        }
    }
}