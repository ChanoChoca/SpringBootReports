package com.example.demo.specification;

import java.util.Collection;

import org.springframework.data.jpa.domain.Specification;

import com.example.demo.search.SearchCriteria;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class GenericSpecification<T> implements Specification<T> {

    private final SearchCriteria criteria;

    public GenericSpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(
            Root<T> root,
            CriteriaQuery<?> query,
            CriteriaBuilder cb) {

        Path<?> path = root.get(criteria.field());

        return switch (criteria.operation()) {

            case EQUAL ->
                    cb.equal(path, criteria.value());

            case NOT_EQUAL ->
                    cb.notEqual(path, criteria.value());

            case GREATER_THAN ->
                    cb.greaterThan(path.as(Comparable.class),
                            (Comparable) criteria.value());

            case LESS_THAN ->
                    cb.lessThan(path.as(Comparable.class),
                            (Comparable) criteria.value());

            case LIKE ->
                    cb.like(
                            cb.lower(path.as(String.class)),
                            "%" + criteria.value().toString().toLowerCase() + "%"
                    );

            case IN ->
                    path.in((Collection<?>) criteria.value());

            case BETWEEN ->
                    cb.between(
                            path.as(Comparable.class),
                            (Comparable) criteria.value(),
                            (Comparable) criteria.valueTo()
                    );
        };
    }
}
