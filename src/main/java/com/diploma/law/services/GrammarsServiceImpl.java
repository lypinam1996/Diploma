package com.diploma.law.services;
import com.diploma.law.DAO.GrammarsDAO;
import com.diploma.law.models.GrammarsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;

@Service("GrammarsService")
@Transactional
public class GrammarsServiceImpl implements GrammarsService{

    @Autowired
    GrammarsDAO grammars;


    @Override
    public  List<GrammarsEntity> findById(int id) {
        return grammars.findById(id);
    }

    @Override
    public GrammarsEntity FindByTitle(String title) {
        return grammars.FindByTitle(title);
    }

    @Override
    public List<GrammarsEntity> findAllGrammars() {
        return grammars.findAllGrammars();
    }
}
