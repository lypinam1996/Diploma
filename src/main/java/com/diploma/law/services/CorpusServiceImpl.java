package com.diploma.law.services;

import com.diploma.law.DAO.CorpusDAO;
import com.diploma.law.models.CorpusesEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;

@Service("CorpusService")
@Transactional
public class CorpusServiceImpl implements CorpusService
{

    @Autowired
    CorpusDAO corpus;

    @Override
    public CorpusesEntity findById(int id)
    {
        return corpus.findById(id);
    }

    @Override
    public CorpusesEntity FindByTitle(String title)
    {
        return corpus.FindByTitle(title);
    }

    @Override
    public List<CorpusesEntity> findAllCorpuses()
    {
        return corpus.findAllCorpuses();
    }

    @Override
    public void saveObject(CorpusesEntity object)
    {
        corpus.saveObject(object);
    }

    @Override
    public CorpusesEntity findMaxId()
    {
        return corpus.findMaxId();
    }
}
