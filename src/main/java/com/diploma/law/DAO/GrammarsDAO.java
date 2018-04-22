package com.diploma.law.DAO;
import com.diploma.law.models.GrammarsEntity;

import java.util.List;

public interface GrammarsDAO {
    GrammarsEntity findById(int id);
    GrammarsEntity FindByTitle(String title);
    List<GrammarsEntity> findAllGrammars();
}