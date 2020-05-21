package com.diploma.law.services;

import java.util.List;

import com.diploma.law.models.WordformsEntity;

public interface WordFormsService
{
    WordformsEntity findById(int id);

    List<WordformsEntity> FindByTitle(String title);
}
