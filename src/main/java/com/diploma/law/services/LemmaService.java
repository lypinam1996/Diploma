package com.diploma.law.services;

import com.diploma.law.models.LemmasEntity;
import java.util.List;

public interface LemmaService
{
    LemmasEntity findById(int id);

    LemmasEntity FindByTitle(String title);

    List<LemmasEntity> findAllLemmas();
}
