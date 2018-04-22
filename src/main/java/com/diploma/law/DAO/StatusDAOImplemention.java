package com.diploma.law.DAO;
import com.diploma.law.models.StatusEntity;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("StatusDAO")
public class StatusDAOImplemention extends AbstractDAO<Integer,StatusEntity> implements StatusDAO{

    @Override
    public StatusEntity findById(int id) {
        Criteria criteria = getSession().createCriteria(StatusEntity.class);
        criteria.add(Restrictions.eq("id", id));
        return (StatusEntity) criteria.uniqueResult();
    }

    @Override
    public StatusEntity FindByLogin(String title) {
        Criteria criteria = getSession().createCriteria(StatusEntity.class);
        criteria.add(Restrictions.eq("title", title));
        return (StatusEntity) criteria.uniqueResult();
    }

    @Override
    public List<StatusEntity> findAllStatuses() {
        Criteria criteria = getSession().createCriteria(StatusEntity.class);
        return (List<StatusEntity>) criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
    }
}
