package com.felipe.uniroom.config.filters;

import com.felipe.uniroom.config.Role;
import com.felipe.uniroom.entities.Branch;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class EmployeeAccessFilter implements AccessFilterStrategy {

    @Override
    public <T> Predicate createFilter(Class<T> clazz, Root<T> root, CriteriaBuilder criteriaBuilder, Role role) {
        if (clazz.equals(Branch.class)) {
            return criteriaBuilder.equal(root.get("idBranch"), role.getBranches().getFirst().getIdBranch());
        }

        return criteriaBuilder.equal(root.get("branch"), role.getBranches().getFirst());
    }
}
