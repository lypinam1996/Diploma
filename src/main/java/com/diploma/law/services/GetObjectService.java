package com.diploma.law.services;

import java.util.List;

import org.springframework.validation.BindingResult;

import com.diploma.law.dto.MainDto;
import com.diploma.law.models.ArticlesEntity;

public interface GetObjectService
{
    List<ArticlesEntity> getObject(BindingResult bindingResult, MainDto mainDto);
}
