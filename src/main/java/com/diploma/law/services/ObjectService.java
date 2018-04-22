package com.diploma.law.services;
import com.diploma.law.models.ObjectsEntity;
import java.util.List;

public interface ObjectService {
    ObjectsEntity findById(int id);
    ObjectsEntity FindByTitle(String title);
    List<ObjectsEntity> findAllObjects();
}
