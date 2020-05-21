package com.diploma.law.DAO;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.diploma.law.models.CorpusesEntity;

@Repository("CorpusDAO")
public class CorpusDAOImpl extends AbstractDAO<Integer, CorpusesEntity> implements CorpusDAO
{

    @Override
    public CorpusesEntity findById(int id)
    {
        Criteria criteria = getSession().createCriteria(CorpusesEntity.class);
        criteria.add(Restrictions.eq("idCorpus", id));
        return (CorpusesEntity)criteria.uniqueResult();
    }

    @Override
    public CorpusesEntity findMaxId()
    {
        Query query = getSession().createSQLQuery("select idCorpus from Corpuses where idCorpus>=all(select * from Corpuses)");
        List<Integer> listId = query.list();
        ArrayList<CorpusesEntity> listClarFacts = new ArrayList<>();
        for (int i = 0; i < listId.size(); i++)
        {
            listClarFacts.add(findById(listId.get(i)));
        }
        return listClarFacts.get(0);
    }

    @Override
    public CorpusesEntity FindByTitle(String title)
    {
        Criteria criteria = getSession().createCriteria(CorpusesEntity.class);
        criteria.add(Restrictions.eq("title", title));
        return (CorpusesEntity)criteria.uniqueResult();
    }

    @Override
    public List<CorpusesEntity> findAllCorpuses()
    {
        Criteria criteria = getSession().createCriteria(CorpusesEntity.class);
        return (List<CorpusesEntity>)criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
    }

    @Override
    public void saveObject(CorpusesEntity object)
    {

        getSession().saveOrUpdate(object);
    }

}
