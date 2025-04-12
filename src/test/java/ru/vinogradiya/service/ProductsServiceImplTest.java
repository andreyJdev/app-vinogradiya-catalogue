package ru.vinogradiya.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.vinogradiya.models.dto.ProductCreateDto;
import ru.vinogradiya.models.dto.ProductItemDto;
import ru.vinogradiya.models.dto.ProductFilter;
import ru.vinogradiya.models.entity.Product;
import ru.vinogradiya.models.entity.Selection;
import ru.vinogradiya.repositories.ProductsRepositoryImpl;
import ru.vinogradiya.utils.common.Paged;
import ru.vinogradiya.utils.common.exception.ApiException;
import ru.vinogradiya.utils.mapping.CatalogueMapper;
import ru.vinogradiya.utils.mapping.ProductsMapper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class ProductsServiceImplTest {

    private static final UUID ID = UUID.randomUUID();

    private final List<Selection> selections = Arrays.asList(
            new Selection(UUID.randomUUID(), "Новая", Collections.emptyList()),
            new Selection(UUID.randomUUID(), "Старая", Collections.emptyList())
    );

    private final List<Product> products = Arrays.asList(
            new Product(
                    UUID.randomUUID(),
                    "Деф1",
                    "Оч. ранний",
                    "Сильно-рослый",
                    "Крупная 500-1200г.",
                    "36х28 мм 15-20г. розовая",
                    "Мясисто-сочная с мускатным ароматом, оч. сладкая",
                    -23,
                    600,
                    300,
                    "basanti.webp",
                    null,
                    "США",
                    0,
                    0,
                    0,
                    2,
                    selections.get(1)
            ),
            new Product(
                    ID,
                    "Деф2",
                    "Оч. ранний",
                    "Сильно-рослый",
                    "Крупная 500-1200г.",
                    "36х28 мм 15-20г. розовая",
                    "Мясисто-сочная с мускатным ароматом, оч. сладкая",
                    -23,
                    500,
                    300,
                    "basanti.webp",
                    null,
                    "США",
                    0,
                    0,
                    0,
                    2,
                    selections.get(0))
    );

    private ProductsService service;

    @Mock
    private ProductsRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        CatalogueMapper<ProductItemDto> mapper = new ProductsMapper();
        service = new ProductsServiceImpl(mapper, repository);
    }

    @Test
    @DisplayName("Проверка получения от репозитория данных и их преобразования в ProductItemDto")
    void testFindAll_shouldReturnPagedProductItem() {

        // given
        ProductFilter filter = ProductFilter.builder().build();
        Pageable pageable = PageRequest.of(0, 2);
        Mockito.when(repository.findAll(null, filter, pageable)).thenReturn(new Paged<>(products));

        // when
        Paged<ProductItemDto> result = service.findAll(null, filter, pageable);

        // then
        Assertions.assertAll(
                () -> assertEquals(2, result.getContent().size()),
                () -> assertEquals(2, result.getNumberOfElements()),
                () -> assertEquals(products.get(0).getName(), result.getContent().get(0).getName()),
                () -> assertEquals(products.get(1).getName(), result.getContent().get(1).getName())
        );
        Mockito.verify(repository, Mockito.times(1)).findAll(null, filter, pageable);
    }

    @Test
    @DisplayName("Проверка получения от репозитория одного элемента и его преобразования в ProductItemDto")
    void testFindById_shouldReturnProductItem() {

        // given
        Mockito.when(repository.findById(ID)).thenReturn(Optional.of(products.get(1)));

        // when
        ProductItemDto result = service.findById(ID);

        // then
        Assertions.assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(products.get(1).getName(), result.getName())
        );
    }

    @Test
    @DisplayName("Проверка получения от репозитория несуществующего элемента и его преобразования в Null")
    void testFindById_shouldReturnNull() {

        // given
        UUID id = UUID.randomUUID();
        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());

        // when and then
        Assertions.assertThrows(ApiException.class, () -> service.findById(id));
    }

    @Test
    @DisplayName("Проверка получения от репозитория одного элемента без селекции и его преобразования в ProductItemDto")
    void testFindById_shouldReturnProductItemWithNullSelection() {

        // given
        UUID id = UUID.randomUUID();
        Product product = new Product();
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(product));

        // when
        ProductItemDto result = service.findById(id);

        // then
        Assertions.assertAll(
                () -> assertNotNull(result),
                () -> assertNull(result.getSelection())
        );
    }

    @Test
    @DisplayName("Проверка добавления в репозиторий сорта винограда и его преобразования в ProductItemDto")
    void testSave_shouldReturnProductItem() {

        // given
        ProductCreateDto createDto = new ProductCreateDto();
        createDto.setName("Ангуляй Воид Секевич");
        Product product = new Product();
        product.setName(createDto.getName());
        Mockito.when(repository.create(createDto)).thenReturn(product);

        // when
        ProductItemDto result = service.save(createDto);

        // then
        Assertions.assertAll(
                () -> assertInstanceOf(ProductItemDto.class, result),
                () -> assertEquals(createDto.getName(), result.getName())
        );
    }
}