package com.felipe.uniroom.config.filters;

import com.felipe.uniroom.config.Role;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class AdminAccessFilter implements AccessFilterStrategy {

    @Override
    public <T> Predicate createFilter(Class<T> clazz, Root<T> root, CriteriaBuilder criteriaBuilder, Role role) {
        return criteriaBuilder.conjunction(); // No filter for admin, return a predicate that always evaluates to true
    }
}
