package com.diploma.law.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.diploma.law.dto.MainDto;
import com.diploma.law.models.ArticlesEntity;
import com.diploma.law.models.ProblemsEntity;

public interface AlgorithmService
{
    Model qualifyOffense(ProblemsEntity problem, Model result, BindingResult bindingResult);

    List<ArticlesEntity> getObject(BindingResult bindingResult, MainDto mainDto);

    ArrayList<ArrayList<String>> getVictimAndSubject(MainDto mainDto);
}
