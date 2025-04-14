package ru.vinogradiya.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ProductFilter {

    @Schema(description = "Название сорта")
    Set<String> names;

    @Schema(description = "Значение морозостойкости")
    Set<@Pattern(regexp = "^(0|[1-9]\\d*|-[1-9]\\d*)$", message = "Только числовые значения") String> resistances;

    @Schema(description = "Название селекции")
    Set<String> selections;

    public Set<String> getNames() {
        return Optional.ofNullable(names)
                .map(namesList -> namesList.stream()
                        .map(name -> Optional.ofNullable(name)
                                .filter(n -> !n.isBlank())
                                .orElse(null))
                        .collect(Collectors.toSet()))
                .orElse(Collections.emptySet());
    }

    public Set<String> getResistances() {
        return Optional.ofNullable(resistances)
                .map(resistancesList -> resistancesList.stream()
                        .map(resistance -> Optional.ofNullable(resistance)
                                .filter(r -> !r.isBlank())
                                .orElse(null))
                        .collect(Collectors.toSet()))
                .orElse(Collections.emptySet());
    }

    public Set<String> getSelections() {
        return Optional.ofNullable(selections)
                .map(selectionsList -> selectionsList.stream()
                        .map(selection -> Optional.ofNullable(selection)
                                .filter(s -> !s.isBlank())
                                .orElse(null))
                        .collect(Collectors.toSet()))
                .orElse(Collections.emptySet());
    }

    public void setNames(List<String> names) {
        if (Objects.nonNull(names)) this.names = new HashSet<>(names);
        else this.names = Collections.emptySet();
    }

    public void setResistances(List<String> resistances) {
        if (Objects.nonNull(resistances)) this.resistances = new HashSet<>(resistances);
        else this.resistances = Collections.emptySet();
    }

    public void setSelections(List<String> selections) {
        if (Objects.nonNull(selections)) this.selections = new HashSet<>(selections);
        else this.selections = Collections.emptySet();
    }

    public static ProductFilter builder() {
        return new ProductFilter();
    }

    public ProductFilter names(List<String> names) {
        setNames(names);
        return this;
    }

    public ProductFilter resistances(List<String> names) {
        setResistances(names);
        return this;
    }

    public ProductFilter selections(List<String> names) {
        setSelections(names);
        return this;
    }

    public ProductFilter build() {
        return this;
    }
}