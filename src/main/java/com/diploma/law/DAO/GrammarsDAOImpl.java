package com.diploma.law.DAO;
import com.diploma.law.models.GrammarsEntity;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("GrammarsDAO")
public class GrammarsDAOImpl extends AbstractDAO<Integer,GrammarsEntity> implements GrammarsDAO{
    @Override
    public GrammarsEntity findById(int id) {
        Criteria criteria = getSession().createCriteria(GrammarsEntity.class);
        criteria.add(Restrictions.eq("idGrammar", id));
        return (GrammarsEntity) criteria.uniqueResult();
    }

    @Override
    public GrammarsEntity FindByTitle(String title) {
        Criteria criteria = getSession().createCriteria(GrammarsEntity.class);
        criteria.add(Restrictions.eq("title", title));
        return (GrammarsEntity) criteria.uniqueResult();
    }

    @Override
    public List<GrammarsEntity> findAllGrammars() {
        Criteria criteria = getSession().createCriteria(GrammarsEntity.class);
        return (List<GrammarsEntity>) criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
    }
}
