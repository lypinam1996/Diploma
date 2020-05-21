package com.diploma.law.DAO;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.diploma.law.models.ProblemsEntity;
import com.diploma.law.models.UsersEntity;

@Repository("TaskDAO")
public class TaskDAOImplementation extends AbstractDAO<Integer, ProblemsEntity> implements TaskDAO
{

    @Override
    public ProblemsEntity findById(int id)
    {
        Criteria criteria = getSession().createCriteria(ProblemsEntity.class);
        criteria.add(Restrictions.eq("idProblem", id));
        return (ProblemsEntity)criteria.uniqueResult();
    }

    @Override
    public List<ProblemsEntity> findTasks(UsersEntity user)
    {
        Query query = getSession().createQuery("from ProblemsEntity where usersByUser = :paramName");
        query.setParameter("paramName", user);
        List list = query.list();
        return list;
    }

    @Override
    public void saveTask(ProblemsEntity task)
    {
        getSession().save(task);
    }

    @Override
    public void deleteTask(int id)
    {
        Query query = getSession().createSQLQuery("DELETE from Problems where idProblem=:id");
        query.setInteger("id", id);
        query.executeUpdate();
    }
}
