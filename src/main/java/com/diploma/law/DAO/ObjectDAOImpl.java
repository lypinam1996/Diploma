package com.diploma.law.DAO;
import com.diploma.law.models.ObjectsEntity;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("ObjectDAO")
public class ObjectDAOImpl extends AbstractDAO<Integer,ObjectsEntity> implements ObjectDAO{
    @Override
    public ObjectsEntity findById(int id) {
        Criteria criteria = getSession().createCriteria(ObjectsEntity.class);
        criteria.add(Restrictions.eq("idObject", id));
        return (ObjectsEntity) criteria.uniqueResult();
    }

    @Override
    public ObjectsEntity FindByTitle(String title) {
        Criteria criteria = getSession().createCriteria(ObjectsEntity.class);
        criteria.add(Restrictions.eq("title", title));
        return (ObjectsEntity) criteria.uniqueResult();
    }

    @Override
    public List<ObjectsEntity> findAllObjects() {
        Criteria criteria = getSession().createCriteria(ObjectsEntity.class);
        return (List<ObjectsEntity>) criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
    }

    @Override
    public void deleteObject(int id) {
        Query query = getSession().createSQLQuery("DELETE from Objects where idObject=:id");
        query.setInteger("id", id);
        query.executeUpdate();
    }

    @Override
    public void saveObject(ObjectsEntity object) {

        getSession().saveOrUpdate(object);
    }
}
