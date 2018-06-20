package com.diploma.law.controllers;

import com.diploma.law.models.CorpusesEntity;
import com.diploma.law.models.LemmasEntity;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Controller;

import java.text.ParseException;
import java.util.Locale;

@Controller
public class LemmaFormatter implements Formatter<LemmasEntity> {


    @Override
    public LemmasEntity parse(String s, Locale locale) throws ParseException {
        LemmasEntity corpusesEntity = new LemmasEntity();
        String[] data = s.split("_");
        corpusesEntity.setIdLemma(Integer.parseInt(data[0]));
        corpusesEntity.setTitle(data[1]);
        return corpusesEntity;

    }

    @Override
    public String print(LemmasEntity statusEntity, Locale locale) {
        String res;
        res = String.valueOf(statusEntity.getIdLemma()) + "_" + statusEntity.getTitle();
        return res;
    }

}
