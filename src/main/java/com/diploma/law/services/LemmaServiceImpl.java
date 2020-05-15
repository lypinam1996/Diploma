package com.diploma.law.services;

import com.diploma.law.DAO.LemmaDAO;
import com.diploma.law.models.LemmasEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;

@Service("LemmaService")
@Transactional
public class LemmaServiceImpl implements LemmaService
{

    @Autowired
    LemmaDAO lemma;

    @Override
    public LemmasEntity findById(int id)
    {
        return lemma.findById(id);
    }

    @Override
    public LemmasEntity FindByTitle(String title)
    {
        return lemma.FindByTitle(title);
    }

    @Override
    public List<LemmasEntity> findAllLemmas()
    {
        return lemma.findAllLemmas();
    }
}
