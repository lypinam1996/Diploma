package com.diploma.law.DAO;

import com.diploma.law.models.WordformsEntity;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository("WordFormsDAO")
public class WordFormsDAOImpl extends AbstractDAO<Integer, WordformsEntity> implements WordFormsDAO
{
    @Override
    public WordformsEntity findById(int id)
    {
        Criteria criteria = getSession().createCriteria(WordformsEntity.class);
        criteria.add(Restrictions.eq("idWordform", id));
        return (WordformsEntity)criteria.uniqueResult();
    }

    @Override
    public List<WordformsEntity> FindByTitle(String title)
    {
        Criteria criteria = getSession().createCriteria(WordformsEntity.class);
        criteria.add(Restrictions.eq("title", title));
        return (List<WordformsEntity>)criteria.list();
    }

}
