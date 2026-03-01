package com.example.demo.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.example.demo.search.SearchCriteria;

public class GenericSpecificationBuilder<T> {

    private final List<SearchCriteria> criteriaList = new ArrayList<>();

    public GenericSpecificationBuilder<T> with(SearchCriteria criteria) {
        criteriaList.add(criteria);
        return this;
    }

    public Specification<T> build() {

        if (criteriaList.isEmpty()) {
            return null;
        }

        Specification<T> result = new GenericSpecification<>(criteriaList.get(0));

        for (int i = 1; i < criteriaList.size(); i++) {
            result = Specification.where(result)
                    .and(new GenericSpecification<>(criteriaList.get(i)));
        }

        return result;
    }
}