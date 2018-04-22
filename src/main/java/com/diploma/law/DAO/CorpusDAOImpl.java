package com.diploma.law.DAO;
import com.diploma.law.models.CorpusesEntity;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("CorpusDAO")
public class CorpusDAOImpl extends AbstractDAO<Integer,CorpusesEntity> implements CorpusDAO{

    @Override
    public CorpusesEntity findById(int id) {
        Criteria criteria = getSession().createCriteria(CorpusesEntity.class);
        criteria.add(Restrictions.eq("idCorpus", id));
        return (CorpusesEntity) criteria.uniqueResult();
    }

    @Override
    public CorpusesEntity FindByTitle(String title) {
        Criteria criteria = getSession().createCriteria(CorpusesEntity.class);
        criteria.add(Restrictions.eq("title", title));
        return (CorpusesEntity) criteria.uniqueResult();
    }

    @Override
    public List<CorpusesEntity> findAllCorpuses() {
        Criteria criteria = getSession().createCriteria(CorpusesEntity.class);
        return (List<CorpusesEntity>) criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
    }
}
