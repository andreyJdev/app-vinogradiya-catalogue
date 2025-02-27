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

import java.util.AbstractMap;
import java.util.List;
import java.util.stream.Stream;

@Repository
@RequiredArgsConstructor
public class ProductsRepositoryImpl implements ProductsRepository {

    private final EntityManager entityManager;

    @Override
    public Paged<Product> findAll(ProductItemFilter filter, Pageable pageable) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> query = builder.createQuery(Product.class);
        Root<Product> product = query.from(Product.class);

        product.fetch(Product_.selection, JoinType.LEFT);
        Join<Product, Selection> selection = product.join(Product_.selection, JoinType.LEFT);

        List<Predicate> predicates = Stream.of(
                        new AbstractMap.SimpleEntry<>(selection.get(Selection_.name), filter.getSelection())
                )
                .filter(compared -> compared.getValue() != null && !compared.getValue().isBlank())
                .map(compared -> builder.equal(compared.getKey(), compared.getValue()))
                .toList();

        query.where(predicates.toArray(new Predicate[0]));

        query.where(predicates.toArray(new Predicate[0]));
        List<Order> orders = pageable.getSort().stream()
                .map(order -> order.isAscending() ?
                        builder.asc(product.get(order.getProperty())) :
                        builder.desc(product.get(order.getProperty())))
                .toList();
        query.orderBy(orders);

        TypedQuery<Product> typedQuery = entityManager.createQuery(query);
        List<Product> result;

        if (pageable.isPaged()) {
            typedQuery.setFirstResult((int) pageable.getOffset());
            typedQuery.setMaxResults(pageable.getPageSize());
        }
        result = typedQuery.getResultList();

        return new Paged<>(
                result,
                pageable,
                pageable.isPaged() && result.size() > pageable.getPageSize()
        );
    }
}