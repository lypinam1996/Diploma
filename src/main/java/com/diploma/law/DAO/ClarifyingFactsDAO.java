package com.diploma.law.DAO;

import java.util.List;

import com.diploma.law.models.ClarifyingFactsEntity;

public interface ClarifyingFactsDAO
{
    ClarifyingFactsEntity findById(int id);

    List<ClarifyingFactsEntity> findAllClaifyingFacts();

    List<ClarifyingFactsEntity> findAllFactsObject(int id_obj);

    List<ClarifyingFactsEntity> findAllFactsLemmas(int id_lemm);

    void saveObject(ClarifyingFactsEntity object);
}
