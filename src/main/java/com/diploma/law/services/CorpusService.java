package com.diploma.law.services;
import com.diploma.law.models.CorpusesEntity;
import java.util.List;


public interface CorpusService {
    CorpusesEntity findById(int id);
    CorpusesEntity FindByTitle(String title);
    List<CorpusesEntity> findAllCorpuses();
}
