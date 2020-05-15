package com.diploma.law.services;

import com.diploma.law.models.GrammarsEntity;

public interface GrammarsService
{
    GrammarsEntity findById(String id);

    GrammarsEntity FindByTitle(String title);

}
