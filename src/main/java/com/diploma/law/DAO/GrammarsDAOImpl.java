package com.diploma.law.DAO;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.diploma.law.models.GrammarsEntity;

@Repository("GrammarsDAO")
public class GrammarsDAOImpl extends AbstractDAO<Integer, GrammarsEntity> implements GrammarsDAO
{
    @Override
    public List<String> findByLemmaId(int id)
    {
        Query query = getSession().createSQLQuery(
                "  SELECT GrammarsLemmas.idGrammar FROM GrammarsLemmas join Lemmas on GrammarsLemmas.idLemma=Lemmas.idLemma where Lemmas.idLemma  = :id");
        query.setInteger("id", id);
        return query.list();
    }

    @Override
    public GrammarsEntity findById(String  id) {
        Criteria criteria = getSession().createCriteria(GrammarsEntity.class);
        criteria.add(Restrictions.eq("idGrammar", id));
        return (GrammarsEntity) criteria.uniqueResult();
    }


    @Override
    public GrammarsEntity FindByTitle(String id)
    {
        Criteria criteria = getSession().createCriteria(GrammarsEntity.class);
        criteria.add(Restrictions.eq("alias", id));
        return (GrammarsEntity)criteria.uniqueResult();
    }

}
