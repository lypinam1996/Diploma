package com.diploma.law.services;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.diploma.law.models.ProblemsEntity;

public interface QualifyOffenseService
{
    Model qualifyOffense(ProblemsEntity problem, Model result, BindingResult bindingResult);
}
