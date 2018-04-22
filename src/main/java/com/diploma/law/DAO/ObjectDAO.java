package com.diploma.law.DAO;
import com.diploma.law.models.ObjectsEntity;

import java.util.List;

public interface ObjectDAO {
    ObjectsEntity findById(int id);
    ObjectsEntity FindByTitle(String title);
    List<ObjectsEntity> findAllObjects();
}
