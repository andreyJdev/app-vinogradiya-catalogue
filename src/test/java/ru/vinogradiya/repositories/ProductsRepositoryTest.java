package ru.vinogradiya.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.vinogradiya.models.dto.ProductItemFilter;
import ru.vinogradiya.models.entity.Product;
import ru.vinogradiya.models.entity.Selection;
import ru.vinogradiya.utils.JpaRepositoryBasedTest;
import ru.vinogradiya.utils.Paged;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Import({ProductsRepositoryImpl.class})
public class ProductsRepositoryTest extends JpaRepositoryBasedTest {

    private final Pageable pageable = PageRequest.of(4, 2);

    @PersistenceContext
    EntityManager manager;

    @Autowired
    ProductsRepository repository;

    @Test
    @DisplayName("Метод findAll должен возвращать Product")
    void testFindAll_shouldReturnProducts() {
        Selection selection = new Selection("Новая");
        Product product1 = new Product("Басанти", "Оч. ранний", "Сильно-рослый", "Крупная 500-1200г.", "36х28 мм 15-20г. розовая",
                "Мясисто-сочная с мускатным ароматом, оч. сладкая", -23, 500, 300, "basanti.webp", null, "США", 0, 0, 0, 2, selection);
        Product product2 = new Product("Оч. ранний", "Басанти", "Сильно-рослый", "Крупная 500-1200г.", "36х28 мм 15-20г. розовая",
                "Мясисто-сочная с мускатным ароматом, оч. сладкая", -23, 500, 300, "basanti.webp", null, "США", 0, 0, 0, 2, selection);
        manager.persist(selection);
        manager.persist(product1);
        manager.persist(product2);
        manager.flush();

        ProductItemFilter filter = new ProductItemFilter();
        Pageable pageable = Pageable.unpaged();

        Paged<Product> result = repository.findAll(filter, pageable);

        assertNotNull(result);
        assertFalse(result.getContent().isEmpty());
        assertTrue(result.getContent().contains(product1));
        assertTrue(result.getContent().contains(product2));
    }
}