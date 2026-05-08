package ru.vinogradiya.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ru.vinogradiya.models.dto.db.ProductCreateData;
import ru.vinogradiya.models.dto.db.ProductUpdateData;
import ru.vinogradiya.models.entity.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductsRepository extends JpaRepository<Product, UUID>,
        JpaSpecificationExecutor<Product>, ProductsFilterValuesRepository {

    @EntityGraph(value = "Product.findAll", type = EntityGraph.EntityGraphType.FETCH)
    @NonNull
    Page<Product> findAll(Specification<Product> spec, @NonNull Pageable pageable);

    @EntityGraph(attributePaths = {"selection"}, type = EntityGraph.EntityGraphType.FETCH)
    @NonNull
    Optional<Product> findById(@NonNull UUID id);

    @EntityGraph(attributePaths = {"selection"}, type = EntityGraph.EntityGraphType.FETCH)
    @Query(value = """
            SELECT p FROM Product p
            WHERE p.name IN (:names)
            """)
    List<Product> findAllByNameIn(List<String> names);

    @Query(value = """
            INSERT INTO product (id, name, time, strength, cluster, berry,
                        taste, resistance_cold, price_seed, price_cut,
                        image, description, selection_mini, available_seed,
                        available_cut, sold_seed, sold_cut, selection_id)
                    VALUES (:#{#data.id},
                            :#{#data.name},
                            :#{#data.time},
                            :#{#data.strength},
                            :#{#data.cluster},
                            :#{#data.berry},
                            :#{#data.taste},
                            :#{#data.resistanceCold},
                            :#{#data.priceSeed},
                            :#{#data.priceCut},
                            :#{#data.image},
                            :#{#data.description},
                            :#{#data.selectionMini},
                            COALESCE(:#{#data.availableSeed}, 0),
                            COALESCE(:#{#data.availableCut}, 0),
                            COALESCE(:#{#data.soldSeed}, 0),
                            COALESCE(:#{#data.soldCut}, 0),
                            :#{#data.selectionId})
            """, nativeQuery = true)
    @Modifying
    void create(ProductCreateData data);

    @Query(value = """
            UPDATE product SET
            name = :#{#data.name},
            time = :#{#data.time},
            strength = :#{#data.strength},
            cluster = :#{#data.cluster},
            berry = :#{#data.berry},
            taste = :#{#data.taste},
            resistance_cold = :#{#data.resistanceCold},
            price_seed = :#{#data.priceSeed},
            price_cut = :#{#data.priceCut},
            image = :#{#data.image},
            description = :#{#data.description},
            selection_mini = :#{#data.selectionMini},
            available_seed = COALESCE(:#{#data.availableSeed}, 0),
            available_cut = COALESCE(:#{#data.availableCut}, 0),
            sold_seed = COALESCE(:#{#data.soldSeed}, 0),
            sold_cut = COALESCE(:#{#data.soldCut}, 0),
            selection_id = :#{#data.selectionId}
            WHERE id = :#{#id}
            """, nativeQuery = true)
    @Modifying
    void update(ProductUpdateData data, UUID id);
}