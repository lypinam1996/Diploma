package com.diploma.law.DAO;
import com.diploma.law.models.WordformsEntity;
import java.util.List;

public interface WordFormsDAO {
    WordformsEntity findById(int id);
    List<WordformsEntity> FindByTitle(String title);
    List<WordformsEntity> findAllWords();
}
