package com.diploma.law.DAO;

import java.util.List;

import com.diploma.law.models.GrammarsEntity;

public interface GrammarsDAO
{
    List<String> findByLemmaId(int id);

    GrammarsEntity FindByTitle(String title);
    GrammarsEntity findById(String  id);
}
