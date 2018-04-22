package com.diploma.law.services;
import com.diploma.law.models.LemmasEntity;
import java.util.Set;


public interface LemmaService {
    LemmasEntity findById(int id);
    LemmasEntity FindByTitle(String title);
    Set<LemmasEntity> findAllLemmas();
}
