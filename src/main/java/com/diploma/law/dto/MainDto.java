package com.diploma.law.dto;

import java.util.List;
import java.util.Map;

import com.diploma.law.models.ClarifyingFactsEntity;
import com.diploma.law.models.LemmasEntity;
import com.diploma.law.models.ObjectsEntity;

public class MainDto
{

    private String                      text;
    private List<String>                sentences;
    //где i - порядковый номер предложения, list - все слова с их свойствами
    private Map<Integer, List<WordDto>> wordMap;
    private List<LemmasEntity>          allLemas;
    private ObjectsEntity               object;
    private List<ClarifyingFactsEntity> clarifyingFacts;
    private int                         mainSentenceInt;
    private String                      mainSentence;

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public List<String> getSentences()
    {
        return sentences;
    }

    public void setSentences(List<String> sentences)
    {
        this.sentences = sentences;
    }

    public ObjectsEntity getObject()
    {
        return object;
    }

    public void setObject(ObjectsEntity object)
    {
        this.object = object;
    }

    public List<ClarifyingFactsEntity> getClarifyingFacts()
    {
        return clarifyingFacts;
    }

    public void setClarifyingFacts(List<ClarifyingFactsEntity> clarifyingFacts)
    {
        this.clarifyingFacts = clarifyingFacts;
    }

    public Map<Integer, List<WordDto>> getWordMap()
    {
        return wordMap;
    }

    public void setWordMap(Map<Integer, List<WordDto>> wordMap)
    {
        this.wordMap = wordMap;
    }

    public List<LemmasEntity> getAllLemas()
    {
        return allLemas;
    }

    public void setAllLemas(List<LemmasEntity> allLemas)
    {
        this.allLemas = allLemas;
    }

    public String getMainSentence()
    {
        return mainSentence;
    }

    public void setMainSentence(String mainSentence)
    {
        this.mainSentence = mainSentence;
    }

    public int getMainSentenceInt()
    {
        return mainSentenceInt;
    }

    public void setMainSentenceInt(int mainSentenceInt)
    {
        this.mainSentenceInt = mainSentenceInt;
    }
}
