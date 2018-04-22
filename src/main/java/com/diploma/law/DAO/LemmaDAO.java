package com.diploma.law.DAO;
import com.diploma.law.models.LemmasEntity;

import java.util.Set;

public interface LemmaDAO {
    LemmasEntity findById(int id);
    LemmasEntity FindByTitle(String title);
    Set<LemmasEntity> findAllLemmas();
}
