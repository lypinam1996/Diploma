package com.diploma.law.DAO;

import com.diploma.law.models.GrammarsEntity;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository("GrammarsDAO")
public class GrammarsDAOImpl extends AbstractDAO<Integer, GrammarsEntity> implements GrammarsDAO
{
    @Override
    public GrammarsEntity findById(String id)
    {
        Criteria criteria = getSession().createCriteria(GrammarsEntity.class);
        criteria.add(Restrictions.eq("idGrammar", id));
        return (GrammarsEntity)criteria.uniqueResult();
    }

    @Override
    public GrammarsEntity FindByTitle(String id)
    {
        Criteria criteria = getSession().createCriteria(GrammarsEntity.class);
        criteria.add(Restrictions.eq("alias", id));
        return (GrammarsEntity)criteria.uniqueResult();
    }

}
