package com.example.demo.specification;

import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;

import com.example.demo.domain.entity.Customer;
import com.example.demo.search.CustomerSearchDTO;

public class CustomerSpecification {

    public static Specification<Customer> build(CustomerSearchDTO filter) {

        return Specification
                .where(notDeleted())
                .and(firstNameContains(filter.firstName()))
                .and(lastNameContains(filter.lastName()))
                .and(emailContains(filter.email()))
                .and(activeEquals(filter.active()))
                .and(createdAfter(filter.createdFrom()))
                .and(createdBefore(filter.createdTo()));
    }

    private static Specification<Customer> notDeleted() {
        return (root, query, cb) ->
                cb.isFalse(root.get("deleted"));
    }

    private static Specification<Customer> firstNameContains(String firstName) {
        return (root, query, cb) -> {
            if (firstName == null || firstName.isBlank()) return null;
            return cb.like(
                    cb.lower(root.get("firstName")),
                    "%" + firstName.toLowerCase() + "%"
            );
        };
    }

    private static Specification<Customer> lastNameContains(String lastName) {
        return (root, query, cb) -> {
            if (lastName == null || lastName.isBlank()) return null;
            return cb.like(
                    cb.lower(root.get("lastName")),
                    "%" + lastName.toLowerCase() + "%"
            );
        };
    }

    private static Specification<Customer> emailContains(String email) {
        return (root, query, cb) -> {
            if (email == null || email.isBlank()) return null;
            return cb.like(
                    cb.lower(root.get("email")),
                    "%" + email.toLowerCase() + "%"
            );
        };
    }

    private static Specification<Customer> activeEquals(Boolean active) {
        return (root, query, cb) -> {
            if (active == null) return null;
            return cb.equal(root.get("active"), active);
        };
    }

    private static Specification<Customer> createdAfter(LocalDateTime from) {
        return (root, query, cb) -> {
            if (from == null) return null;
            return cb.greaterThanOrEqualTo(root.get("createdAt"), from);
        };
    }

    private static Specification<Customer> createdBefore(LocalDateTime to) {
        return (root, query, cb) -> {
            if (to == null) return null;
            return cb.lessThanOrEqualTo(root.get("createdAt"), to);
        };
    }
}