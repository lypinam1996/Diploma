package com.diploma.law.DAO;

import java.util.List;

import com.diploma.law.models.LemmasEntity;

public interface LemmaDAO
{
    LemmasEntity findById(int id);

    LemmasEntity FindByTitle(String title);

    List<LemmasEntity> findAllLemmas();
}
