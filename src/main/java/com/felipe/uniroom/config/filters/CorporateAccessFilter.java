package com.felipe.uniroom.config.filters;

import com.felipe.uniroom.config.Role;
import com.felipe.uniroom.entities.Branch;
import com.felipe.uniroom.entities.Corporate;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.lang.reflect.Field;

public class CorporateAccessFilter implements AccessFilterStrategy {

    @Override
    public <T> Predicate createFilter(Class<T> clazz, Root<T> root, CriteriaBuilder criteriaBuilder, Role role) {
        if (clazz.equals(Corporate.class)) {
            return root.get("idCorporate")
                    .in(role.getCorporates().stream()
                            .map(Corporate::getIdCorporate)
                            .toList());
        }

        if (hasBranchField(clazz)) {
            Join<T, Branch> branchJoin = root.join("branch");
            return branchJoin.get("corporate").in(role.getCorporates());
        }

        return root.get("corporate").in(role.getCorporates());
    }

    private boolean hasBranchField(Class<?> clazz) {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getName().equals("branch")) {
                return true;
            }
        }

        return false;
    }
}
