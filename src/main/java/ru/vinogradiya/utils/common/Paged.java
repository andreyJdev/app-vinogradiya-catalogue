package ru.vinogradiya.utils.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.function.Function;

@JsonIgnoreProperties("pageable")
@EqualsAndHashCode
@Schema(requiredProperties = {
        "content", "numberOfElements", "size", "number", "sort", "first", "last", "empty"
})
public class Paged<T> extends SliceImpl<T> {

    @Schema(description = "Количество элементов на странице")
    private int numberOfElements;

    @Schema(description = "Размер страницы")
    private int size;

    @Schema(description = "Номер текущей страницы")
    private int number;

    @Schema(description = "Информация о сортировке")
    private Sort sort;

    @Schema(description = "Является ли это первой страницей")
    private boolean first;

    @Schema(description = "Является ли это последней страницей")
    private boolean last;

    @Schema(description = "Является ли эта страница пустой")
    private boolean empty;

    public Paged(List<T> content, Pageable pageable, boolean hasNext) {
        super(content, pageable, hasNext);
        this.numberOfElements = content.size();
        if (pageable.isPaged()) {
            this.size = pageable.getPageSize();
            this.number = pageable.getPageNumber();
            this.sort = pageable.getSort();
            this.first = pageable.getPageNumber() == 0;
            this.empty = this.numberOfElements == 0;
            this.last = !this.empty && this.numberOfElements < pageable.getPageSize();
        }
    }

    public Paged(List<T> content) {
        super(content);
        this.numberOfElements = content.size();
        this.empty = this.numberOfElements == 0;
    }

    @Override
    public <U> Paged<U> map(Function<? super T, ? extends U> converter) {
        return new Paged<>(this.getConvertedContent(converter), this.getPageable(), this.hasNext());
    }
}