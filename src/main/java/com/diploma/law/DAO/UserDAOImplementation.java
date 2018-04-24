package com.diploma.law.DAO;
import com.diploma.law.models.UsersEntity;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("UserDAO")
public class UserDAOImplementation extends AbstractDAO<Integer,UsersEntity> implements UserDAO{
    @Override
    public UsersEntity findById(int id) {
        Criteria criteria = getSession().createCriteria(UsersEntity.class);
        criteria.add(Restrictions.eq("idUser", id));
        return (UsersEntity) criteria.uniqueResult();
    }

    @Override
    public UsersEntity FindByLogin(String title) {
        Criteria criteria = getSession().createCriteria(UsersEntity.class);
        criteria.add(Restrictions.eq("login", title));
        return (UsersEntity) criteria.uniqueResult();
    }


    @Override
    public List<UsersEntity> findAllUsers() {
        Criteria criteria = getSession().createCriteria(UsersEntity.class);
        return (List<UsersEntity>) criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
    }

    @Override
    public void saveUser(UsersEntity user) {
        getSession().save(user);

    }
}
