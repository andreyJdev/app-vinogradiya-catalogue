package ru.vinogradiya.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Generated;

import java.util.Optional;

@Data
public class ProductUpdateDto {

    //todo добавить аннотацию проверки уникальности
    @NotNull
    @Size(min = 2, max = 32)
    @Schema(description = "Название сорта")
    private String name;

    @Size(min = 2, max = 32)
    @Schema(description = "Время созревания")
    private String time;

    @Size(min = 2, max = 32)
    @Schema(description = "Сила роста")
    private String strength;

    @Size(min = 2, max = 32)
    @Schema(description = "Описание особенностей грозди")
    private String cluster;

    @Size(min = 2, max = 64)
    @Schema(description = "Описание особенностей ягоды")
    private String berry;

    @Size(min = 2, max = 64)
    @Schema(description = "Описание вкусовых особенностей")
    private String taste;

    @Schema(description = "Значение морозостойкости")
    private Integer resistanceCold;

    @Schema(description = "Цена саженца")
    private Integer priceSeed;

    @Schema(description = "Цена черенка")
    private Integer priceCut;

    @Size(max = 128)
    @Schema(description = "Изображение")
    private String image;

    @Size(max = 2048)
    @Schema(description = "Описание")
    private String description;

    @Size(min = 2, max = 32)
    @Schema(description = "Название мини селекции (если есть)")
    private String selectionMini;

    @Schema(description = "Доступно саженцев")
    private Integer availableSeed;

    @Schema(description = "Доступно черенков")
    private Integer availableCut;

    @Schema(description = "Продано саженцев")
    private Integer soldSeed;

    @Schema(description = "Продано черенков")
    private Integer soldCut;

    //todo добавить аннотацию проверки существования, ели не null
    @Schema(description = "Идентификатор селекции")
    private Integer selectionId;

    @Generated
    public String getTime() {
        return blankToNull(this.time);
    }

    @Generated
    public String getStrength() {
        return blankToNull(this.strength);
    }

    @Generated
    public String getCluster() {
        return blankToNull(this.cluster);
    }

    @Generated
    public String getBerry() {
        return blankToNull(this.berry);
    }

    @Generated
    public String getTaste() {
        return blankToNull(this.taste);
    }

    @Generated
    public Integer getPriceSeed() {
        return nullToZero(this.priceSeed);
    }

    @Generated
    public Integer getPriceCut() {
        return nullToZero(this.priceCut);
    }

    @Generated
    public String getImage() {
        return blankToNull(this.image);
    }

    @Generated
    public String getDescription() {
        return blankToNull(this.description);
    }

    @Generated
    public String getSelectionMini() {
        return blankToNull(this.description);
    }

    @Generated
    public Integer getAvailableSeed() {
        return nullToZero(this.availableSeed);
    }

    @Generated
    public Integer getAvailableCut() {
        return nullToZero(this.availableCut);
    }

    @Generated
    public Integer getSoldSeed() {
        return nullToZero(this.soldSeed);
    }

    @Generated
    public Integer getSoldCut() {
        return soldCut;
    }

    @Generated
    public Integer getSelectionId() {
        return this.selectionId == 0 ? null : this.selectionId;
    }

    private String blankToNull(String str) {
        return Optional.ofNullable(str).filter(s -> !s.isBlank()).orElse(null);
    }

    private Integer nullToZero(Integer num) {
        return Optional.ofNullable(num).orElse(0);
    }
}
