package com.diploma.law.DAO;

import com.diploma.law.models.GrammarsEntity;

public interface GrammarsDAO
{
    GrammarsEntity findById(String id);

    GrammarsEntity FindByTitle(String title);
}
