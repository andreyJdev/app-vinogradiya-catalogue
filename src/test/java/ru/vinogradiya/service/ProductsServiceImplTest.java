package ru.vinogradiya.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import ru.vinogradiya.models.dto.db.ProductCreateData;
import ru.vinogradiya.models.dto.db.ProductUpdateData;
import ru.vinogradiya.models.dto.request.ProductCreateInput;
import ru.vinogradiya.models.dto.request.ProductUpdateInput;
import ru.vinogradiya.models.dto.request.filter.ProductFilter;
import ru.vinogradiya.models.dto.response.ProductItem;
import ru.vinogradiya.models.entity.Product;
import ru.vinogradiya.models.entity.Selection;
import ru.vinogradiya.repositories.ProductsRepository;
import ru.vinogradiya.utils.common.exception.ApiException;
import ru.vinogradiya.utils.mapping.ItemMapper;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;

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
                    new BigDecimal(600),
                    new BigDecimal(300),
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
                    new BigDecimal(500),
                    new BigDecimal(300),
                    "basanti.webp",
                    null,
                    "США",
                    0,
                    0,
                    0,
                    2,
                    selections.getFirst())
    );

    private ProductsService service;

    @Mock
    private ProductsRepository repository;

    @BeforeEach
    void setUp() {
        ItemMapper mapper = Mappers.getMapper(ItemMapper.class);
        service = new ProductsServiceImpl(mapper, repository);
    }

    @Test
    @SuppressWarnings(value = "unchecked")
    @DisplayName("Проверка получения от репозитория данных и их преобразования в ProductItemDto")
    void testFindAll_shouldReturnPagedProductItem() {

        // given
        ProductFilter filter = ProductFilter.builder().build();
        Pageable pageable = PageRequest.of(0, 2);
        Page<Product> expected = new PageImpl<>(products, pageable, products.size());

        Mockito.when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expected);

        // when
        Page<ProductItem> result = service.findAll(null, filter, pageable);

        // then
        Assertions.assertAll(
                () -> assertEquals(2, result.getContent().size()),
                () -> assertEquals(2, result.getNumberOfElements()),
                () -> assertEquals(products.getFirst().getName(), result.getContent().getFirst().getName()),
                () -> assertEquals(products.get(1).getName(), result.getContent().get(1).getName())
        );
        Mockito.verify(repository, Mockito.times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    @DisplayName("Проверка получения от репозитория одного элемента и его преобразования в ProductItemDto")
    void testFindById_shouldReturnProductItem() {

        // given
        Mockito.when(repository.findById(ID)).thenReturn(Optional.of(products.get(1)));

        // when
        ProductItem result = service.findById(ID);

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
        ProductItem result = service.findById(id);

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
        ProductCreateInput createRequest = new ProductCreateInput();
        createRequest.setName("Ангуляй Воид Секевич");
        Product product = new Product();
        product.setName(createRequest.getName());
        Mockito.doNothing().when(repository).create(any(ProductCreateData.class));
        Mockito.when(repository.findById(any(UUID.class)))
                .thenReturn(Optional.of(product));

        // when
        ProductItem result = service.save(createRequest);

        // then
        Assertions.assertAll(
                () -> assertInstanceOf(ProductItem.class, result),
                () -> assertEquals(createRequest.getName(), result.getName())
        );
    }

    @Test
    @DisplayName("Проверка обновления сорта винограда и его преобразования в ProductItemDto")
    void testUpdate_shouldReturnProductItem() {

        // given
        ProductUpdateInput updateRequest = new ProductUpdateInput();
        updateRequest.setName("Алиса");
        Product product = new Product();
        product.setName(updateRequest.getName());
        Mockito.doNothing().when(repository).update(any(ProductUpdateData.class), any(UUID.class));
        Mockito.when(repository.findById(any(UUID.class)))
                .thenReturn(Optional.of(product));

        // when
        ProductItem result = service.update(updateRequest, UUID.randomUUID());

        // then
        Assertions.assertAll(
                () -> assertInstanceOf(ProductItem.class, result),
                () -> assertEquals(updateRequest.getName(), result.getName())
        );
    }
}