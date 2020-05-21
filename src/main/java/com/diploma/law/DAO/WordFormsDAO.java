package com.diploma.law.DAO;

import java.util.List;

import com.diploma.law.models.WordformsEntity;

public interface WordFormsDAO
{
    WordformsEntity findById(int id);

    List<WordformsEntity> FindByTitle(String title);

}
