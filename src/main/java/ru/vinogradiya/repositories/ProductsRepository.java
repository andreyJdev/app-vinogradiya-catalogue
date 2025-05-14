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
import ru.vinogradiya.models.dto.ProductCreateDto;
import ru.vinogradiya.models.dto.ProductUpdateDto;
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
                    VALUES (:#{#dto.id},
                            :#{#dto.name},
                            :#{#dto.time},
                            :#{#dto.strength},
                            :#{#dto.cluster},
                            :#{#dto.berry},
                            :#{#dto.taste},
                            :#{#dto.resistanceCold},
                            :#{#dto.priceSeed},
                            :#{#dto.priceCut},
                            :#{#dto.image},
                            :#{#dto.description},
                            :#{#dto.selectionMini},
                            :#{#dto.availableSeed},
                            :#{#dto.availableCut},
                            :#{#dto.soldSeed},
                            :#{#dto.soldCut},
                            :#{#dto.selectionId})
            """, nativeQuery = true)
    @Modifying
    void create(ProductCreateDto dto);

    @Query(value = """
            UPDATE Product p SET
            p.name = :#{#dto.name},
            p.time = :#{#dto.time},
            p.strength = :#{#dto.strength},
            p.cluster = :#{#dto.cluster},
            p.berry = :#{#dto.berry},
            p.taste = :#{#dto.taste},
            p.resistanceCold = :#{#dto.resistanceCold},
            p.priceSeed = :#{#dto.priceSeed},
            p.priceCut = :#{#dto.priceCut},
            p.image = :#{#dto.image},
            p.description = :#{#dto.description},
            p.selectionMini = :#{#dto.selectionMini},
            p.availableSeed = :#{#dto.availableSeed},
            p.availableCut = :#{#dto.availableCut},
            p.soldSeed = :#{#dto.soldSeed},
            p.soldCut = :#{#dto.soldCut},
            p.selection = :#{#dto.selectionId}
            WHERE p.id = :#{#dto.id}
            """)
    @Modifying
    void update(ProductUpdateDto dto);
}