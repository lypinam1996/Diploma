package com.diploma.law.DAO;

import java.util.List;

import com.diploma.law.models.CorpusesEntity;

public interface CorpusDAO
{
    CorpusesEntity findById(int id);

    CorpusesEntity FindByTitle(String title);

    List<CorpusesEntity> findAllCorpuses();

    void saveObject(CorpusesEntity object);

    CorpusesEntity findMaxId();
}
