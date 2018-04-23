package com.diploma.law.DAO;
import com.diploma.law.models.ProblemsEntity;
import com.diploma.law.models.UsersEntity;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository("TaskDAO")
public class TaskDAOImplementation extends AbstractDAO<Integer,ProblemsEntity> implements TaskDAO {


    @Override
    public ProblemsEntity findById(int id) {
        Criteria criteria = getSession().createCriteria(ProblemsEntity.class);
        criteria.add(Restrictions.eq("idProblem", id));
        return (ProblemsEntity) criteria.uniqueResult();
    }

    @Override
    public ProblemsEntity FindByNumber(int number) {
        Criteria criteria = getSession().createCriteria(ProblemsEntity.class);
        criteria.add(Restrictions.eq("number", number));
        return (ProblemsEntity) criteria.uniqueResult();
    }

    @Override
    public List<ProblemsEntity> findTasks(UsersEntity user){
        Query query = getSession().createQuery("from Problems where user = :paramName");
        query.setParameter("paramName", user);
        List list = query.list();
        return list;
    }

    @Override
    public List<ProblemsEntity> findAllTasks() {
        Criteria criteria = getSession().createCriteria(ProblemsEntity.class);
        return (List<ProblemsEntity>) criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();

    }

    @Override
    public void saveTask(ProblemsEntity task) {
        getSession().save(task);
    }

    @Override
    public void deleteTask(int id) {
        Query query = getSession().createSQLQuery("DELETE from Problems where idProblem=:id");
        query.setInteger("id", id);
        query.executeUpdate();
    }
}

