package com.diploma.law.services;
import com.diploma.law.models.WordformsEntity;
import java.util.List;

public interface WordFormsService {
    WordformsEntity findById(int id);
    List<WordformsEntity> FindByTitle(String title);
    List<WordformsEntity> findAllWords();
}
