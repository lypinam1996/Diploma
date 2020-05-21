package com.diploma.law.services;

import java.util.List;

import com.diploma.law.models.CorpusesEntity;

public interface CorpusService
{
    CorpusesEntity findById(int id);

    CorpusesEntity FindByTitle(String title);

    List<CorpusesEntity> findAllCorpuses();

    void saveObject(CorpusesEntity object);

    CorpusesEntity findMaxId();
}
