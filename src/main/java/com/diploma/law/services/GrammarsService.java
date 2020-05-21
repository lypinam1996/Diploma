package com.diploma.law.services;

import java.util.List;

import com.diploma.law.models.GrammarsEntity;

public interface GrammarsService
{
    List<GrammarsEntity> findById(int id);

    GrammarsEntity FindByTitle(String title);

}
