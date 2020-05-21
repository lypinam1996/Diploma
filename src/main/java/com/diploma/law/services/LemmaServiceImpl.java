package com.diploma.law.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.diploma.law.DAO.LemmaDAO;
import com.diploma.law.models.LemmasEntity;

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
