package com.diploma.law.services;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.diploma.law.DAO.GrammarsDAO;
import com.diploma.law.models.GrammarsEntity;

@Service("GrammarsService")
@Transactional
public class GrammarsServiceImpl implements GrammarsService
{

    @Autowired
    GrammarsDAO grammars;

    @Override
    public List<GrammarsEntity> findById(int idLemma)
    {
        List<String> ids = grammars.findByLemmaId(idLemma);
        List<GrammarsEntity> result = new ArrayList<>();
        for (String id : ids)
        {
            result.add(grammars.findById(id));
        }
        return result;
    }

    @Override
    public GrammarsEntity FindByTitle(String title)
    {
        return grammars.FindByTitle(title);
    }
}
