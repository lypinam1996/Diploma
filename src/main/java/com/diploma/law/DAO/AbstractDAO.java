package com.diploma.law.DAO;

import java.lang.reflect.ParameterizedType;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class AbstractDAO<K, E>
{

    private final Class<K> persistanceClass;

    @Autowired
    SessionFactory         _sessionFactory;

    @SuppressWarnings("unchecked")
    public AbstractDAO()
    {
        this.persistanceClass = (Class<K>)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];

    }

    protected Session getSession()
    {
        return _sessionFactory.getCurrentSession();
    }

    public void persist(E entity)
    {
        getSession().persist(entity);
    }

    public void delete(E entity)
    {
        getSession().delete(entity);
    }

}
