package ru.vinogradiya.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.vinogradiya.utils.common.string.MessageUtil.NUMBER_PATTERN;

public class ProductFilter {

    @Schema(description = "Название сорта")
    Set<String> names;

    @Schema(description = "Значение морозостойкости")
    Set<@Pattern(regexp = NUMBER_PATTERN, message = "{vinogradiya.catalogue.base.integer}")
    @Size(max = 3, message = "{vinogradiya.catalogue.base.max_size}") String> resistances;

    @Schema(description = "Название селекции")
    Set<String> selections;

    public Set<String> getNames() {
        return filterStringCollectionToStringSet(names);
    }

    public Set<Integer> getResistances() {
        return filterStringCollectionToIntSet(resistances);
    }

    public Set<String> getSelections() {
        return filterStringCollectionToStringSet(selections);
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

    private Set<String> filterStringCollectionToStringSet(Collection<String> input) {
        return Optional.ofNullable(input)
                .map(list -> list.stream()
                        .map(it -> Objects.isNull(it) || it.isBlank() ? null : it.trim())
                        .collect(Collectors.toSet()))
                .orElse(Collections.emptySet());
    }

    private Set<Integer> filterStringCollectionToIntSet(Collection<String> input) {
        return Optional.ofNullable(input)
                .map(list -> list.stream()
                        .map(it -> Objects.isNull(it) || it.isBlank() ? null : Integer.parseInt(it.trim()))
                        .collect(Collectors.toSet()))
                .orElse(Collections.emptySet());
    }
}