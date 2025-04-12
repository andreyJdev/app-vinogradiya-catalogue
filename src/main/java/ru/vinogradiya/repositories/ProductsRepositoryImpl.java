package ru.vinogradiya.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ru.vinogradiya.models.dto.ProductCreateDto;
import ru.vinogradiya.models.dto.ProductFilter;
import ru.vinogradiya.models.entity.Product;
import ru.vinogradiya.models.entity.Product_;
import ru.vinogradiya.models.entity.Selection;
import ru.vinogradiya.models.entity.Selection_;
import ru.vinogradiya.utils.common.Paged;
import ru.vinogradiya.utils.common.PredicateManager;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ProductsRepositoryImpl implements ProductsRepository {

    private final EntityManager entityManager;

    @Override
    public Paged<Product> findAll(String search, ProductFilter filter, Pageable pageable) {
        CriteriaBuilder builder = getCriteriaBuilder();
        CriteriaQuery<Product> query = builder.createQuery(Product.class);
        Root<Product> product = query.from(Product.class);

        query.select(product)
                .where(buildPredicates(filter, product));

        if (pageable.getSort().isSorted()) {
            List<Order> orders = pageable.getSort().stream()
                    .map(order -> order.isAscending() ?
                            builder.asc(product.get(order.getProperty())) :
                            builder.desc(product.get(order.getProperty())))
                    .toList();
            query.orderBy(orders);
        }

        TypedQuery<Product> typedQuery = entityManager.createQuery(query);

        if (pageable.isPaged()) {
            typedQuery.setFirstResult((int) pageable.getOffset());
            typedQuery.setMaxResults(pageable.getPageSize());
        }
        List<Product> result = typedQuery.getResultList();

        return new Paged<>(
                result,
                pageable,
                pageable.isPaged() && result.size() > pageable.getPageSize()
        );
    }

    @Override
    public Optional<Product> findById(UUID id) {
        CriteriaQuery<Product> query = getCriteriaBuilder().createQuery(Product.class);
        Root<Product> product = query.from(Product.class);

        query.select(product)
                .where(PredicateManager.builder(getCriteriaBuilder())
                        .addEqual(id, product.get(Product_.id))
                        .buildArray()
                );

        List<Product> result = entityManager.createQuery(query).getResultList();

        return result.stream().findFirst();
    }

    @Override
    public Optional<Product> findByName(String name) {
        CriteriaQuery<Product> query = getCriteriaBuilder().createQuery(Product.class);
        Root<Product> product = query.from(Product.class);

        query.select(product)
                .where(PredicateManager.builder(getCriteriaBuilder())
                        .addEqual(name, product.get(Product_.name))
                        .buildArray()
                );

        List<Product> result = entityManager.createQuery(query).getResultList();

        return result.stream().findFirst();
    }

    private Predicate[] buildPredicates(ProductFilter filter, Root<Product> product) {
        if (filter != null) {
            Join<Product, Selection> selection = product.join(Product_.selection, JoinType.LEFT);
            return PredicateManager.builder(getCriteriaBuilder())
                    .addIn(filter.getSelections(), selection.get(Selection_.name))
                    .buildArray();
        }
        return new Predicate[]{};
    }

    @Override
    public Product create(ProductCreateDto createDto) {

        Selection selection = getSelectionById(Optional.ofNullable(createDto.getSelectionId())
                .map(UUID::fromString).orElse(null));
        Product product = buildProduct(createDto, selection);

        if (selection != null) {
            selection.getProducts().add(product);
        }

        entityManager.persist(product);

        return product;
    }

    private CriteriaBuilder getCriteriaBuilder() {
        return entityManager.getCriteriaBuilder();
    }

    private Selection getSelectionById(UUID selectionId) {
        if (selectionId == null) {
            return null;
        }

        CriteriaQuery<Selection> query = getCriteriaBuilder().createQuery(Selection.class);
        Root<Selection> root = query.from(Selection.class);

        try {
            return entityManager.createQuery(query
                            .select(root)
                            .where(getCriteriaBuilder().equal(root.get(Selection_.id), selectionId)))
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    private Product buildProduct(ProductCreateDto createDto, Selection selection) {
        return new Product(
                UUID.randomUUID(),
                createDto.getName(),
                createDto.getTime(),
                createDto.getStrength(),
                createDto.getCluster(),
                createDto.getBerry(),
                createDto.getTaste(),
                createDto.getResistanceCold(),
                createDto.getPriceSeed(),
                createDto.getPriceCut(),
                createDto.getImage(),
                createDto.getDescription(),
                createDto.getSelectionMini(),
                createDto.getAvailableSeed(),
                createDto.getAvailableCut(),
                createDto.getSoldSeed(),
                createDto.getSoldCut(),
                selection);
    }
}