package com.diploma.law.DAO;
import com.diploma.law.models.CorpusesEntity;

import java.util.List;

public interface CorpusDAO {
    CorpusesEntity findById(int id);
    CorpusesEntity FindByTitle(String title);
    List<CorpusesEntity> findAllCorpuses();
}
