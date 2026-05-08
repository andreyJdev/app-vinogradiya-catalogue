package ru.vinogradiya.models.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import ru.vinogradiya.models.entity.Selection;
import ru.vinogradiya.utils.mapping.ItemViews;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Data
@Builder
@Schema(description = "Элемент получения сорта винограда")
public class ProductItem {

    @JsonView(ItemViews.Private.class)
    @Schema(description = "Идентификатор сорта")
    private UUID id;

    @JsonView(ItemViews.UserAccess.class)
    @Schema(description = "Название сорта")
    private String name;

    @JsonView(ItemViews.UserAccess.class)
    @Schema(description = "Время созревания")
    private String time;

    @JsonView(ItemViews.UserAccess.class)
    @Schema(description = "Сила роста")
    private String strength;

    @JsonView(ItemViews.UserAccess.class)
    @Schema(description = "Описание особенностей грозди")
    private String cluster;

    @JsonView(ItemViews.UserAccess.class)
    @Schema(description = "Описание особенностей ягоды")
    private String berry;

    @JsonView(ItemViews.UserAccess.class)
    @Schema(description = "Описание вкусовых особенностей")
    private String taste;

    @JsonView(ItemViews.UserAccess.class)
    @Schema(description = "Значение морозостойкости")
    private Integer resistanceCold;

    @JsonView(ItemViews.UserAccess.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.00", locale = "ru")
    @Schema(description = "Цена саженца")
    private BigDecimal priceSeed;

    @JsonView(ItemViews.UserAccess.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.00", locale = "ru")
    @Schema(description = "Цена черенка")
    private BigDecimal priceCut;

    @JsonView(ItemViews.UserAccess.class)
    @Schema(description = "Изображение")
    private String image;

    @JsonView(ItemViews.UserAccess.class)
    @Schema(description = "Описание")
    private String description;

    @JsonView(ItemViews.UserAccess.class)
    @Schema(description = "Название мини селекции (если есть)")
    private String selectionMini;

    @JsonView(ItemViews.AdminAccess.class)
    @Schema(description = "Доступно саженцев")
    private Integer availableSeed;

    @JsonView(ItemViews.AdminAccess.class)
    @Schema(description = "Доступно черенков")
    private Integer availableCut;

    @JsonView(ItemViews.AdminAccess.class)
    @Schema(description = "Продано саженцев")
    private Integer soldSeed;

    @JsonView(ItemViews.AdminAccess.class)
    @Schema(description = "Продано черенков")
    private Integer soldCut;

    @Schema(description = "Название селекции")
    private SelectionDto selection;

    public record SelectionDto(@JsonView(ItemViews.UserAccess.class)
                               String name) {

        public static SelectionDto of(Selection selection) {
            return Objects.nonNull(selection) ? new SelectionDto(selection.getName()) : null;
        }
    }
}