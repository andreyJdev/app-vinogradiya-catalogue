package ru.vinogradiya.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Generated;
import ru.vinogradiya.models.entity.Selection_;
import ru.vinogradiya.utils.validation.annotation.PresentInDbConstraint;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Data
public abstract class ProductInput {

    @Size(max = 32)
    @Schema(description = "Время созревания")
    private String time;

    @Size(max = 32)
    @Schema(description = "Сила роста")
    private String strength;

    @Size(max = 32)
    @Schema(description = "Описание особенностей грозди")
    private String cluster;

    @Size(max = 64)
    @Schema(description = "Описание особенностей ягоды")
    private String berry;

    @Size(max = 64)
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

    @Size(max = 32)
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

    @Pattern(regexp = "^(\\s*|[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-4[0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12})$")
    @PresentInDbConstraint(table = "selection", column = Selection_.ID, message = "Селекция с идентификатором: {0} не найдена")
    @Schema(description = "Идентификатор селекции")
    private String selectionId;

    @Generated
    public String getTime() {
        return upperFirst(blankToNull(this.time));
    }

    @Generated
    public String getStrength() {
        return upperFirst(blankToNull(this.strength));
    }

    @Generated
    public String getCluster() {
        return upperFirst(blankToNull(this.cluster));
    }

    @Generated
    public String getBerry() {
        return upperFirst(blankToNull(this.berry));
    }

    @Generated
    public String getTaste() {
        return upperFirst(blankToNull(this.taste));
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
        return upperFirst(blankToNull(this.description));
    }

    @Generated
    public String getSelectionMini() {
        return upperFirst(blankToNull(this.description));
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
        return this.soldCut;
    }

    @Generated
    public UUID getSelectionId() {
        return Objects.nonNull(blankToNull(this.selectionId)) ? UUID.fromString(this.selectionId) : null;
    }

    protected String blankToNull(String str) {
        return Optional.ofNullable(str).filter(s -> !s.isBlank()).orElse(null);
    }

    protected String upperFirst(String str) {
        return Optional.ofNullable(str).map(s -> s.length() > 1 ? s.substring(0, 1).toUpperCase() + s.substring(1) : s).orElse(null);
    }

    protected Integer nullToZero(Integer num) {
        return Optional.ofNullable(num).orElse(0);
    }
}