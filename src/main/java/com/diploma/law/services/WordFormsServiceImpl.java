package com.diploma.law.services;
import com.diploma.law.DAO.WordFormsDAO;
import com.diploma.law.models.WordformsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;

@Service("WordFormsService")
@Transactional
public class WordFormsServiceImpl implements WordFormsService{

    @Autowired
    WordFormsDAO words;

    @Override
    public WordformsEntity findById(int id) {
        return words.findById(id);
    }

    @Override
    public List<WordformsEntity> FindByTitle(String title) {
        return words.FindByTitle(title);
    }

    @Override
    public List<WordformsEntity> findAllWords() {
        return words.findAllWords();
    }
}
