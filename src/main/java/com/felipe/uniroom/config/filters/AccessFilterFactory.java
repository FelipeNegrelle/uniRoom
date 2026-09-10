package com.felipe.uniroom.config.filters;

import com.felipe.uniroom.config.Role;

public final class AccessFilterFactory {

    private AccessFilterFactory() {
    }

    public static AccessFilterStrategy create(Role role) {
        return switch (role.getRole()) {
            case 'A' -> new AdminAccessFilter();
            case 'C' -> new CorporateAccessFilter();
            case 'B' -> new BranchAccessFilter();
            case 'E' -> new EmployeeAccessFilter();
            default -> throw new IllegalArgumentException("Perfil de acesso inválido: " + role.getRole());
        };
    }
}
