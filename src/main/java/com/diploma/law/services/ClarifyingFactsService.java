package com.diploma.law.services;

import com.diploma.law.models.ClarifyingFactsEntity;
import java.util.List;

public interface ClarifyingFactsService
{
    ClarifyingFactsEntity findById(int id);

    List<ClarifyingFactsEntity> findAllClaifyingFacts();

    List<ClarifyingFactsEntity> findAllFactsObject(int id_obj);

    List<ClarifyingFactsEntity> findAllFactsLemmas(int id_lemm);

    void saveObject(ClarifyingFactsEntity object);
}
