package com.diploma.law.DAO;

import com.diploma.law.models.StatusEntity;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository("StatusDAO")
public class StatusDAOImplemention extends AbstractDAO<Integer, StatusEntity> implements StatusDAO
{

    @Override
    public StatusEntity findById(int id)
    {
        Criteria criteria = getSession().createCriteria(StatusEntity.class);
        criteria.add(Restrictions.eq("id", id));
        return (StatusEntity)criteria.uniqueResult();
    }
}
