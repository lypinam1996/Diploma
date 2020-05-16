package com.diploma.law.dto;

import com.diploma.law.models.ClarifyingFactsEntity;
import com.diploma.law.models.LemmasEntity;
import com.diploma.law.models.ObjectsEntity;
import com.diploma.law.models.WordformsEntity;

import java.util.ArrayList;
import java.util.List;

public class MainDto
{

    private String                      text;
    private ArrayList<String>           sentences;
    private ArrayList<String>           words;
    private ArrayList<WordformsEntity>  wordForms;
    private ArrayList<LemmasEntity>     lemmas;
    private ObjectsEntity               object;
    private List<ClarifyingFactsEntity> clarifyingFacts;
    private String                      mainSentence;

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public ArrayList<String> getSentences()
    {
        return sentences;
    }

    public void setSentences(ArrayList<String> sentences)
    {
        this.sentences = sentences;
    }

    public ArrayList<String> getWords()
    {
        return words;
    }

    public void setWords(ArrayList<String> words)
    {
        this.words = words;
    }

    public ArrayList<WordformsEntity> getWordForms()
    {
        return wordForms;
    }

    public void setWordForms(ArrayList<WordformsEntity> wordForms)
    {
        this.wordForms = wordForms;
    }

    public ArrayList<LemmasEntity> getLemmas()
    {
        return lemmas;
    }

    public void setLemmas(ArrayList<LemmasEntity> lemmas)
    {
        this.lemmas = lemmas;
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

    public String getMainSentence() {
        return mainSentence;
    }

    public void setMainSentence(String mainSentence) {
        this.mainSentence = mainSentence;
    }
}
