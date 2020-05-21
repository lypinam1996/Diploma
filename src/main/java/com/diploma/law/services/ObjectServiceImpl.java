package com.diploma.law.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.diploma.law.DAO.ObjectDAO;
import com.diploma.law.models.ObjectsEntity;

@Service("ObjectService")
@Transactional
public class ObjectServiceImpl implements ObjectService
{

    @Autowired
    ObjectDAO object;

    @Override
    public ObjectsEntity findById(int id)
    {
        return object.findById(id);
    }

    @Override
    public ObjectsEntity FindByTitle(String title)
    {
        return object.FindByTitle(title);
    }

    @Override
    public List<ObjectsEntity> findAllObjects()
    {
        return object.findAllObjects();
    }

    @Override
    public void deleteObject(int id)
    {
        object.deleteObject(id);
    }

    @Override
    public void saveObject(ObjectsEntity object1)
    {
        object.saveObject(object1);
    }
}
