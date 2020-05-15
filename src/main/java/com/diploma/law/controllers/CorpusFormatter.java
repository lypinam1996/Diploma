package com.diploma.law.controllers;

import com.diploma.law.models.CorpusesEntity;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Controller;
import java.text.ParseException;
import java.util.Locale;

@Controller
public class CorpusFormatter implements Formatter<CorpusesEntity>
{

    @Override
    public CorpusesEntity parse(String s, Locale locale) throws ParseException
    {
        CorpusesEntity corpusesEntity = new CorpusesEntity();
        String[] data = s.split("_");
        corpusesEntity.setIdCorpus(Integer.parseInt(data[0]));
        corpusesEntity.setTitle(data[1]);
        return corpusesEntity;

    }

    @Override
    public String print(CorpusesEntity statusEntity, Locale locale)
    {
        String res;
        res = String.valueOf(statusEntity.getIdCorpus()) + "_" + statusEntity.getTitle();
        return res;
    }

}
