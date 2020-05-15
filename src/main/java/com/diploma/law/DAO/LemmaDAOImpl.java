package com.diploma.law.DAO;

import com.diploma.law.models.LemmasEntity;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;

@Repository("LemmaDAO")
public class LemmaDAOImpl extends AbstractDAO<Integer, LemmasEntity> implements LemmaDAO
{
    @Override
    public LemmasEntity findById(int id)
    {
        Criteria criteria = getSession().createCriteria(LemmasEntity.class);
        criteria.add(Restrictions.eq("idLemma", id));
        return (LemmasEntity)criteria.uniqueResult();
    }

    @Override
    public LemmasEntity FindByTitle(String title)
    {
        Criteria criteria = getSession().createCriteria(LemmasEntity.class);
        criteria.add(Restrictions.eq("title", title));
        return (LemmasEntity)criteria.uniqueResult();
    }

    @Override
    public List<LemmasEntity> findAllLemmas()
    {
        SQLQuery query = getSession().createSQLQuery("select * from Lemmas LIMIT 20");
        List<Object[]> rows = query.list();
        List<LemmasEntity> lemm = new ArrayList<>();
        for (Object[] row : rows)
        {
            LemmasEntity lemma = new LemmasEntity();
            lemma.setIdLemma(Integer.parseInt(row[0].toString()));
            lemma.setTitle(row[1].toString());
            lemm.add(lemma);
        }
        return lemm;
    }
}
