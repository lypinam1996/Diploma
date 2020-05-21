package com.diploma.law.services;

import java.util.List;

import com.diploma.law.models.LemmasEntity;

public interface LemmaService
{
    LemmasEntity findById(int id);

    LemmasEntity FindByTitle(String title);

    List<LemmasEntity> findAllLemmas();
}
