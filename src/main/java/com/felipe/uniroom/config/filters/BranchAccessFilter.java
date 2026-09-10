package com.felipe.uniroom.config.filters;

import com.felipe.uniroom.config.Role;
import com.felipe.uniroom.entities.Branch;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class BranchAccessFilter implements AccessFilterStrategy {

    @Override
    public <T> Predicate createFilter(Class<T> clazz, Root<T> root, CriteriaBuilder criteriaBuilder, Role role) {
        if (clazz.equals(Branch.class)) {
            return root.get("idBranch")
                    .in(role.getBranches().stream()
                            .map(Branch::getIdBranch)
                            .toList());
        }

        return root.get("branch").in(role.getBranches());
    }
}
