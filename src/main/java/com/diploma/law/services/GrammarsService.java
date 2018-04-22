package com.diploma.law.services;
import com.diploma.law.models.GrammarsEntity;
import java.util.List;


public interface GrammarsService {
    List<GrammarsEntity> findById(int id);
    GrammarsEntity FindByTitle(String title);
    List<GrammarsEntity> findAllGrammars();
}
