package ru.vinogradiya.utils.common;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PredicateManager {

    private final CriteriaBuilder builder;
    private final List<Predicate> predicates = new ArrayList<>();

    public static PredicateManager builder(CriteriaBuilder builder) {
        return new PredicateManager(builder);
    }

    public <T> PredicateManager addEqual(T filterObj, Path<T> dbObj) {
        if (filterObj != null) {
            predicates.add(builder.equal(dbObj, filterObj));
        }
        return this;
    }

    public <T> PredicateManager addIn(List<T> filterObjects, Path<T> dbObj) {
        if (!filterObjects.isEmpty()) {
            predicates.add(dbObj.in(filterObjects));
        }
        return this;
    }

    public Predicate[] buildArray() {
        return predicates.toArray(Predicate[]::new);
    }
}