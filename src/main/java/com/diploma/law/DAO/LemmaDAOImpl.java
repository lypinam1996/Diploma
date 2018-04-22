package com.diploma.law.DAO;
import com.diploma.law.models.LemmasEntity;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository("LemmaDAO")
public class LemmaDAOImpl extends AbstractDAO<Integer,LemmasEntity> implements LemmaDAO{
    @Override
    public LemmasEntity findById(int id) {
        Criteria criteria = getSession().createCriteria(LemmasEntity.class);
        criteria.add(Restrictions.eq("idLemma", id));
        return (LemmasEntity) criteria.uniqueResult();
    }

    @Override
    public LemmasEntity FindByTitle(String title) {
        Criteria criteria = getSession().createCriteria(LemmasEntity.class);
        criteria.add(Restrictions.eq("title", title));
        return (LemmasEntity) criteria.uniqueResult();
    }

    @Override
    public Set<LemmasEntity> findAllLemmas() {
        Criteria criteria = getSession().createCriteria(LemmasEntity.class);
      //  criteria.setFetchMode("grammars", FetchMode.DEFAULT);
     //   criteria.setFetchMode("object", FetchMode.DEFAULT);
        return (Set<LemmasEntity>) criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
    }
}
