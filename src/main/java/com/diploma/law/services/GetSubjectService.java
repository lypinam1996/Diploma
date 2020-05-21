package com.diploma.law.services;

import com.diploma.law.dto.MainDto;
import com.diploma.law.models.ProblemsEntity;

public interface GetSubjectService
{
    void getVictimAndSubject(MainDto mainDto, ProblemsEntity problemsEntity);

    void getSubject(MainDto mainDto, ProblemsEntity problemsEntity);
}
