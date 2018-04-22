package com.diploma.law.DAO;
import com.diploma.law.models.GrammarsEntity;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("GrammarsDAO")
public class GrammarsDAOImpl extends AbstractDAO<Integer,GrammarsEntity> implements GrammarsDAO{
    @Override
    public List<GrammarsEntity> findById(int id) {
        Criteria criteria = getSession().createCriteria(GrammarsEntity.class);
        criteria.add(Restrictions.eq("idGrammar", id));
        return (List<GrammarsEntity>) criteria.uniqueResult();
    }

    @Override
    public GrammarsEntity FindByTitle(String id) {
        Criteria criteria = getSession().createCriteria(GrammarsEntity.class);
        criteria.add(Restrictions.eq("alias", id));
        return (GrammarsEntity) criteria.uniqueResult();
    }

    @Override
    public List<GrammarsEntity> findAllGrammars() {
        Criteria criteria = getSession().createCriteria(GrammarsEntity.class);
        return (List<GrammarsEntity>) criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
    }
}
