package ru.vinogradiya.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Selection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.query.QueryUtils;
import ru.vinogradiya.models.dto.FilterValue;
import ru.vinogradiya.models.entity.Product;
import ru.vinogradiya.utils.enums.FilterProperty;

import java.util.List;

public class ProductsFilterValuesRepositoryImpl implements ProductsFilterValuesRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<FilterValue> findDistinctFields(FilterProperty property, Specification<Product> specification, Pageable pageable) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<FilterValue> query = builder.createQuery(FilterValue.class);
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);

        Root<Product> countRoot = countQuery.from(Product.class);
        Root<Product> root = query.from(Product.class);

        query.multiselect(
                property.getSelection(root, builder).toArray(new Selection[0])
        ).distinct(true);

        if (specification != null) {
            Predicate predicate = specification.toPredicate(root, query, builder);
            Predicate countPredicate = specification.toPredicate(countRoot, query, builder);
            if (predicate != null) {
                query.where(predicate);
                countQuery.where(countPredicate);
            }
        }

        if (pageable.getSort().isSorted()) {
            query.orderBy(QueryUtils.toOrders(pageable.getSort(), root, builder));
        }
        var builtQuery = entityManager.createQuery(query);
        List<FilterValue> resultList;
        if (pageable.isUnpaged()) {
            resultList = builtQuery.getResultList();
            return new PageImpl<>(resultList);
        }

        resultList = builtQuery.setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
        countQuery.select(builder.countDistinct(property.getValuePath(countRoot)));

        Long total = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(resultList, pageable, total);
    }
}