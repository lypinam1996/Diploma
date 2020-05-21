package com.diploma.law.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.diploma.law.DAO.ClarifyingFactsDAO;
import com.diploma.law.models.ClarifyingFactsEntity;

@Service("ClarifyingFactsService")
@Transactional
public class ClarifyingFactsServiceImpl implements ClarifyingFactsService
{

    @Autowired
    ClarifyingFactsDAO clarifyingFacts;

    @Override
    public ClarifyingFactsEntity findById(int id)
    {
        return clarifyingFacts.findById(id);
    }

    @Override
    public List<ClarifyingFactsEntity> findAllClaifyingFacts()
    {
        return clarifyingFacts.findAllClaifyingFacts();
    }

    @Override
    public List<ClarifyingFactsEntity> findAllFactsObject(int id_obj)
    {
        return clarifyingFacts.findAllFactsObject(id_obj);
    }

    @Override
    public List<ClarifyingFactsEntity> findAllFactsLemmas(int id_lemm)
    {
        return clarifyingFacts.findAllFactsLemmas(id_lemm);
    }

    @Override
    public void saveObject(ClarifyingFactsEntity object)
    {
        clarifyingFacts.saveObject(object);
    }
}
