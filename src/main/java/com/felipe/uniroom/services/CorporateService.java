package com.felipe.uniroom.services;

import com.felipe.uniroom.entities.Corporate;
import com.felipe.uniroom.repositories.CorporateRepository;

public class CorporateService {

    public static Boolean save(Corporate corporate) {
        try {
            //todo verificar cnpj aqui

            return CorporateRepository.saveOrUpdate(corporate);
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
    }
}
