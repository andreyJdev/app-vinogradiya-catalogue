package ru.vinogradiya.repositories;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.jdbc.Sql;
import ru.vinogradiya.models.dto.FilterValue;
import ru.vinogradiya.models.dto.ProductFilter;
import ru.vinogradiya.models.entity.Product;
import ru.vinogradiya.models.entity.Product_;
import ru.vinogradiya.utils.JpaRepositoryBasedTest;
import ru.vinogradiya.utils.enums.FilterProperty;

import java.util.Collections;
import java.util.List;

import static ru.vinogradiya.queries.product.ProductSpecificationBuilder.buildSpecificationFrom;

@Sql("/db/sql-test-data/products-filter-values.sql")
class ProductsFilterValuesRepositoryImplTest extends JpaRepositoryBasedTest {

    @Autowired
    private ProductsRepository repository;

    @Test
    @DisplayName("Значение фильтра FilterParams, эквивалентное значению property, не должно влиять на результат")
    void findFindAll_shouldReturnPagedWhenFilterMatchProperty() {

        // given
        FilterProperty property = FilterProperty.NAME;
        ProductFilter filter = ProductFilter.builder()
                .names(List.of("Рембо", "Алиса в стране чудес"))
                .build();
        Specification<Product> spec = buildSpecificationFrom(property, filter, "");

        // when
        Page<FilterValue> paged = repository.findDistinctFields(property, spec, PageRequest.of(0, 10));

        // then
        Assertions.assertThat(paged).hasSize(9);
    }

    @Test
    @DisplayName("Должен работать поиск по значению property")
    void testFindAll_shouldReturnsPagedWhenFilterConditionApplied() {

        //given
        String search = "Рембо";
        Specification<Product> spec = buildSpecificationFrom(FilterProperty.NAME, ProductFilter.builder().build(), search);

        // when
        Page<FilterValue> paged = repository.findDistinctFields(FilterProperty.NAME, spec, PageRequest.of(0, 10));

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(paged).hasSize(1);
            softAssertions.assertThat(paged.getContent().get(0).getValue()).isEqualTo(search);
        });
    }

    @Test
    @DisplayName("Должны быть найдены все имена сортов, у которых нет селекции")
    void testFindAll_shouldReturnsPagedWhenHaveNotSelection() {

        // given
        ProductFilter filter = ProductFilter.builder()
                .selections(Collections.singletonList(null))
                .build();
        FilterProperty property = FilterProperty.NAME;
        Specification<Product> spec = buildSpecificationFrom(property, filter, "");

        // when
        Page<FilterValue> paged = repository.findDistinctFields(property, spec,
                PageRequest.of(0, 10));

        // then
        Assertions.assertThat(paged).hasSize(2);
    }

    @Test
    @DisplayName("Должны быть найдены названия сортов без селекции или с указанной")
    void testFindAll_shouldReturnsPagedWhenApplyingFilterParams() {

        // given
        ProductFilter filter = ProductFilter.builder()
                .resistances(List.of("-23"))
                .selections(List.of("", "Гибридные формы селекции Криули С.И."))
                .build();
        FilterProperty property = FilterProperty.NAME;
        Specification<Product> spec = buildSpecificationFrom(property, filter, "");

        // when
        Page<FilterValue> paged = repository.findDistinctFields(property, spec,
                PageRequest.of(0, 10, Sort.by(Product_.NAME).ascending()));

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(paged).hasSize(2);
            softAssertions.assertThat(paged.getContent().get(0).getValue()).isEqualTo("Алиса в стране чудес");
            softAssertions.assertThat(paged.getContent().get(1).getValue()).isEqualTo("Рембо");
        });
    }

    // todo сделать так, чтобы тест отрабатывал успешно, сейчас ошибка из-за join: null значения игнорируются
    @Disabled
    @Test
    @DisplayName("Должны быть найдены все селекции, включая null значение")
    void testFindAll_shouldFindAllSelectionsWithNullValue() {

        // given
        ProductFilter filter = ProductFilter.builder()
                .selections(List.of("", "Гибридные формы селекции Криули С.И."))
                .build();
        FilterProperty property = FilterProperty.SELECTION;
        Specification<Product> spec = buildSpecificationFrom(property, filter, null);

        // when
        Page<FilterValue> paged = repository.findDistinctFields(property, spec,
                PageRequest.of(0, 10));

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(paged).hasSize(5);
            softAssertions.assertThat(paged.getContent().contains(null)).isTrue();
        });
    }
}