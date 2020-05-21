package com.diploma.law.services;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;

import com.diploma.law.dto.MainDto;
import com.diploma.law.models.ArticlesEntity;
import com.diploma.law.models.ClarifyingFactsEntity;

@Service
@Transactional
public class GetObjectServiceImpl implements GetObjectService{

    @Override
    public List<ArticlesEntity> getObject(BindingResult bindingResult, MainDto mainDto)
    {
        List<ArticlesEntity> articles = new ArrayList<>();
        if (mainDto.getObject() == null)
        {
            bindingResult.rejectValue("title", "error.title",
                    "*Невозможно квалифицировать данное преступление. Пожалуйса, переформулируйте его другими словами.");
        }
        else
        {
            List<ClarifyingFactsEntity> clarifyingFacts = mainDto.getClarifyingFacts();
            if (!CollectionUtils.isEmpty(clarifyingFacts))
            {
                for (ClarifyingFactsEntity factsEntity : clarifyingFacts)
                {
                    articles.add(factsEntity.getCorpus().getArticle());
                }
            }
            else
            {
                List<ClarifyingFactsEntity> clarifyingFactsObject = mainDto.getObject().getClarifyingfacts();
                for (ClarifyingFactsEntity clarifyingFact : clarifyingFactsObject)
                {
                    if (clarifyingFact.getLemma() == null) articles.add(clarifyingFact.getCorpus().getArticle());
                }
            }
        }
        return articles;
    }

}
