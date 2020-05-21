package com.diploma.law.DAO;

import java.util.List;

import com.diploma.law.models.ObjectsEntity;

public interface ObjectDAO
{
    ObjectsEntity findById(int id);

    ObjectsEntity FindByTitle(String title);

    List<ObjectsEntity> findAllObjects();

    void deleteObject(int id);

    void saveObject(ObjectsEntity object);
}
