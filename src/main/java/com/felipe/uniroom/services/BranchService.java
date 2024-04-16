package com.felipe.uniroom.services;

import com.felipe.uniroom.entities.Branch;
import com.felipe.uniroom.entities.User;
import com.felipe.uniroom.repositories.BranchRepository;
import com.felipe.uniroom.repositories.UserRepository;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Objects;

public class BranchService {
    public static Boolean save(Branch branch) {
        try {
            return BranchRepository.saveOrUpdate(branch);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Boolean update(Branch branch) {
        try {
            final Branch result = UserRepository.findById(Branch.class, branch.getIdBranch());

            if (result != null) {
                result.setIdBranch(branch.getIdBranch());
                result.setName(branch.getName());
                result.setCorporate(branch.getCorporate());
                result.setUser(branch.getUser());
                result.setActive(branch.getActive());

                return BranchRepository.saveOrUpdate(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Boolean delete(Branch branch) {
        try {
            final Branch result = BranchRepository.findById(Branch.class, branch.getIdBranch());

            if (Objects.nonNull(result)) {
                return BranchRepository.delete(Branch.class, result.getIdBranch());
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
    }
}
