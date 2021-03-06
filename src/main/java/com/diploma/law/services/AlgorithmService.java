package com.diploma.law.services;

import com.diploma.law.models.ArticlesEntity;
import com.diploma.law.models.ProblemsEntity;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

public interface AlgorithmService {
    List<ArticlesEntity> qualifyOffense(ProblemsEntity problem, BindingResult bindingResult);
    ArrayList<ArrayList<String>> getVictimAndSubject(String text, ArticlesEntity article);
}
