package com.diploma.law.dto;

import java.util.List;

import com.diploma.law.models.GrammarsEntity;
import com.diploma.law.models.LemmasEntity;
import com.diploma.law.models.WordformsEntity;

public class WordDto
{
    private String               words;
    private WordformsEntity      wordForms;
    private LemmasEntity         lemmas;
    private List<GrammarsEntity> grammars;
    private ParserObject         parserObject;
    private boolean              isObject;
    private boolean              isClarFact;
    private boolean              isNoun;

    public boolean isNoun()
    {
        return isNoun;
    }

    public void setNoun(boolean noun)
    {
        isNoun = noun;
    }

    public String getWords()
    {
        return words;
    }

    public void setWords(String words)
    {
        this.words = words;
    }

    public WordformsEntity getWordForms()
    {
        return wordForms;
    }

    public void setWordForms(WordformsEntity wordForms)
    {
        this.wordForms = wordForms;
    }

    public LemmasEntity getLemmas()
    {
        return lemmas;
    }

    public void setLemmas(LemmasEntity lemmas)
    {
        this.lemmas = lemmas;
    }

    public List<GrammarsEntity> getGrammars()
    {
        return grammars;
    }

    public void setGrammars(List<GrammarsEntity> grammars)
    {
        this.grammars = grammars;
    }

    public ParserObject getParserObject()
    {
        return parserObject;
    }

    public void setParserObject(ParserObject parserObject)
    {
        this.parserObject = parserObject;
    }

    public boolean isObject()
    {
        return isObject;
    }

    public void setObject(boolean object)
    {
        isObject = object;
    }

    public boolean isClarFact()
    {
        return isClarFact;
    }

    public void setClarFact(boolean clarFact)
    {
        isClarFact = clarFact;
    }
}
