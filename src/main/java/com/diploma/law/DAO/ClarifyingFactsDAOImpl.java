package com.diploma.law.DAO;

import com.diploma.law.models.ClarifyingFactsEntity;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;

@Repository("ClarifyingFactsDAO")
public class ClarifyingFactsDAOImpl extends AbstractDAO<Integer, ClarifyingFactsEntity> implements ClarifyingFactsDAO
{
    @Override

    public ClarifyingFactsEntity findById(int id)
    {
        Criteria criteria = getSession().createCriteria(ClarifyingFactsEntity.class);
        criteria.add(Restrictions.eq("idClarifyingFact", id));
        return (ClarifyingFactsEntity)criteria.uniqueResult();
    }

    @Override
    public List<ClarifyingFactsEntity> findAllClaifyingFacts()
    {
        Criteria criteria = getSession().createCriteria(ClarifyingFactsEntity.class);
        return (List<ClarifyingFactsEntity>)criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
    }

    @Override
    public List<ClarifyingFactsEntity> findAllFactsObject(int id_obj)
    {
        Query query = getSession().createSQLQuery("select idClarifyingFact from ClarifyingFacts where idObject = :id");
        query.setInteger("id", id_obj);
        List<Integer> listId = listId = query.list();
        ArrayList<ClarifyingFactsEntity> listClarFacts = new ArrayList<>();
        for (int i = 0; i < listId.size(); i++)
        {
            listClarFacts.add(findById(listId.get(i)));
        }
        return listClarFacts;
    }

    @Override
    public List<ClarifyingFactsEntity> findAllFactsLemmas(int id_lemm)
    {
        Query query = getSession().createSQLQuery("select idClarifyingFact from ClarifyingFacts where idLemma is null and idObject = :id");
        query.setInteger("id", id_lemm);
        List<Integer> listId = listId = query.list();
        ArrayList<ClarifyingFactsEntity> listClarFacts = new ArrayList<>();
        for (int i = 0; i < listId.size(); i++)
        {
            listClarFacts.add(findById(listId.get(i)));
        }
        return listClarFacts;
    }

    @Override
    public void saveObject(ClarifyingFactsEntity object)
    {

        getSession().saveOrUpdate(object);
    }
}
