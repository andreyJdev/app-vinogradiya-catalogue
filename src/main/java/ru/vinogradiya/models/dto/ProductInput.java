package ru.vinogradiya.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Generated;
import ru.vinogradiya.models.entity.Selection_;
import ru.vinogradiya.utils.validation.annotation.PresentInDbConstraint;

import java.util.Optional;
import java.util.UUID;

import static ru.vinogradiya.utils.common.string.MessageUtil.NUMBER_PATTERN;
import static ru.vinogradiya.utils.common.string.MessageUtil.POSITIVE_FLOAT_PATTERN;
import static ru.vinogradiya.utils.common.string.MessageUtil.POSITIVE_NUMBER_PATTERN;
import static ru.vinogradiya.utils.dto.InputDtoMethods.blankToNull;
import static ru.vinogradiya.utils.dto.InputDtoMethods.getNumber;
import static ru.vinogradiya.utils.dto.InputDtoMethods.upperFirst;

@Data
public abstract class ProductInput {

    @Schema(description = "Время созревания")
    @Size(max = 32, message = "{vinogradiya.catalogue.base.max_size}")
    private String time;

    @Schema(description = "Сила роста")
    @Size(max = 32, message = "{vinogradiya.catalogue.base.max_size}")
    private String strength;

    @Schema(description = "Описание особенностей грозди")
    @Size(max = 32, message = "{vinogradiya.catalogue.base.max_size}")
    private String cluster;

    @Schema(description = "Описание особенностей ягоды")
    @Size(max = 64, message = "{vinogradiya.catalogue.base.max_size}")
    private String berry;

    @Schema(description = "Описание вкусовых особенностей")
    @Size(max = 64, message = "{vinogradiya.catalogue.base.max_size}")
    private String taste;

    @Schema(description = "Значение морозостойкости")
    @Pattern(regexp = NUMBER_PATTERN, message = "{vinogradiya.catalogue.base.integer}")
    @Size(max = 3, message = "{vinogradiya.catalogue.base.max_size}")
    private String resistanceCold;

    @Schema(description = "Цена саженца")
    @Pattern(regexp = POSITIVE_FLOAT_PATTERN, message = "{vinogradiya.catalogue.base.positive}")
    @Size(max = 8, message = "{vinogradiya.catalogue.base.max_size}")
    private String priceSeed;

    @Schema(description = "Цена черенка")
    @Pattern(regexp = POSITIVE_FLOAT_PATTERN, message = "{vinogradiya.catalogue.base.positive}")
    @Size(max = 8, message = "{vinogradiya.catalogue.base.max_size}")
    private String priceCut;

    @Schema(description = "Изображение")
    @Size(max = 128, message = "{vinogradiya.catalogue.base.max_size}")
    private String image;

    @Schema(description = "Описание")
    @Size(max = 2048, message = "{vinogradiya.catalogue.base.max_size}")
    private String description;

    @Schema(description = "Название мини селекции (если есть)")
    @Size(max = 32, message = "{vinogradiya.catalogue.base.max_size}")
    private String selectionMini;

    @Schema(description = "Доступно саженцев")
    @Pattern(regexp = POSITIVE_NUMBER_PATTERN, message = "{vinogradiya.catalogue.base.positive}")
    @Size(max = 8, message = "{vinogradiya.catalogue.base.max_size}")
    private String availableSeed;

    @Schema(description = "Доступно черенков")
    @Pattern(regexp = POSITIVE_NUMBER_PATTERN, message = "{vinogradiya.catalogue.base.positive}")
    @Size(max = 8, message = "{vinogradiya.catalogue.base.max_size}")
    private String availableCut;

    @Schema(description = "Продано саженцев")
    @Pattern(regexp = POSITIVE_NUMBER_PATTERN, message = "{vinogradiya.catalogue.base.positive}")
    @Size(max = 8, message = "{vinogradiya.catalogue.base.max_size}")
    private String soldSeed;

    @Schema(description = "Продано черенков")
    @Pattern(regexp = POSITIVE_NUMBER_PATTERN, message = "{vinogradiya.catalogue.base.positive}")
    @Size(max = 8, message = "{vinogradiya.catalogue.base.max_size}")
    private String soldCut;

    @Schema(description = "Идентификатор селекции")
    @PresentInDbConstraint(table = "selection", column = Selection_.ID, message = "{vinogradiya.catalogue.selection.not_found}")
    @Pattern(regexp = "^([0-9a-fA-F]{8}-[0-9a-fA-F]{4}-4[0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12})$|\\s*")
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
    public Integer getResistanceCold() {
        return getNumber(this.resistanceCold);
    }

    @Generated
    public Integer getPriceSeed() {
        return getNumber(this.priceSeed);
    }

    @Generated
    public Integer getPriceCut() {
        return getNumber(this.priceCut);
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
        return upperFirst(blankToNull(this.selectionMini));
    }

    @Generated
    public Integer getAvailableSeed() {
        return getNumber(this.availableSeed);
    }

    @Generated
    public Integer getAvailableCut() {
        return getNumber(this.availableCut);
    }

    @Generated
    public Integer getSoldSeed() {
        return getNumber(this.soldSeed);
    }

    @Generated
    public Integer getSoldCut() {
        return getNumber(this.soldCut);
    }

    @Generated
    public UUID getSelectionId() {
        return Optional.ofNullable(blankToNull(this.selectionId))
                .map(UUID::fromString).orElse(null);
    }
}