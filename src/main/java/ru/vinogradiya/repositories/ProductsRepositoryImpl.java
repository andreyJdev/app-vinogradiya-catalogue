package ru.vinogradiya.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ru.vinogradiya.models.dto.ProductItemFilter;
import ru.vinogradiya.models.entity.Product;
import ru.vinogradiya.models.entity.Product_;
import ru.vinogradiya.models.entity.Selection;
import ru.vinogradiya.models.entity.Selection_;
import ru.vinogradiya.utils.Paged;
import ru.vinogradiya.utils.PredicateManager;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductsRepositoryImpl implements ProductsRepository {

    private final EntityManager entityManager;

    @Override
    public Paged<Product> findAll(ProductItemFilter filter, Pageable pageable) {
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
    public Optional<Product> findById(Long id) {
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

    private Predicate[] buildPredicates(ProductItemFilter filter, Root<Product> product) {
        if (filter != null) {
            Join<Product, Selection> selection = product.join(Product_.selection, JoinType.LEFT);
            return PredicateManager.builder(getCriteriaBuilder())
                    .addIn(filter.getSelections(), selection.get(Selection_.name))
                    .buildArray();
        }
        return new Predicate[]{};
    }

    private CriteriaBuilder getCriteriaBuilder() {
        return entityManager.getCriteriaBuilder();
    }
}