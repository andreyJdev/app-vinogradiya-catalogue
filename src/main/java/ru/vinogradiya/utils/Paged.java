package ru.vinogradiya.utils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@JsonIgnoreProperties("pageable")
@EqualsAndHashCode
@Schema(requiredProperties = {
        "content", "numberOfElements", "size", "number", "sort", "first", "last", "empty"
})
public class Paged<T> extends SliceImpl<T> {

    @Schema(description = "Количество элементов на странице")
    int numberOfElements;

    @Schema(description = "Размер страницы")
    int size;

    @Schema(description = "Номер текущей страницы")
    int number;

    @Schema(description = "Информация о сортировке")
    Sort sort;

    @Schema(description = "Является ли это первой страницей")
    boolean first;

    @Schema(description = "Является ли это последней страницей")
    boolean last;

    @Schema(description = "Является ли эта страница пустой")
    boolean empty;

    public Paged(List<T> content, Pageable pageable, boolean hasNext) {
        super(content, pageable, hasNext);
    }

    public Paged(List<T> content) {
        super(content);
    }

    @Override
    public <U> Paged<U> map(Function<? super T, ? extends U> converter) {
        return new Paged<>(this.getConvertedContent(converter), this.getPageable(), this.hasNext());
    }
}