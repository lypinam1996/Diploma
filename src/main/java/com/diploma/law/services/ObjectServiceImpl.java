package com.diploma.law.services;
import com.diploma.law.DAO.ObjectDAO;
import com.diploma.law.models.ObjectsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;

@Service("ObjectService")
@Transactional
public class ObjectServiceImpl implements ObjectService{

    @Autowired
    ObjectDAO object;

    @Override
    public ObjectsEntity findById(int id) {
        return object.findById(id);
    }

    @Override
    public ObjectsEntity FindByTitle(String title) {
        return object.FindByTitle(title);
    }

    @Override
    public List<ObjectsEntity> findAllObjects() {
        return object.findAllObjects();
    }
}
