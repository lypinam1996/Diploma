package com.diploma.law.services;

import java.util.List;

import com.diploma.law.models.ObjectsEntity;

public interface ObjectService
{
    ObjectsEntity findById(int id);

    ObjectsEntity FindByTitle(String title);

    List<ObjectsEntity> findAllObjects();

    void deleteObject(int id);

    void saveObject(ObjectsEntity object);
}
