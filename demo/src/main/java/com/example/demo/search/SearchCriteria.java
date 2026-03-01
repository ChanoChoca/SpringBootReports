package com.example.demo.search;

public record SearchCriteria(
        String field,
        SearchOperation operation,
        Object value,
        Object valueTo
) {}