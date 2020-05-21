package com.diploma.law.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ParserObject implements Serializable
{
    @JsonProperty("ID")
    private String id;
    @JsonProperty("FORM")
    private String form;
    @JsonProperty("LEMMA")
    private String lemma;
    @JsonProperty("postag")
    private String posTag;
    @JsonProperty("FEATS")
    private String feats;
    @JsonProperty("HEAD")
    private String head;
    @JsonProperty("DEPREL")
    private String depRel;

    public ParserObject()
    {

    }

    public ParserObject(String id, String form, String lemma, String posTag, String feats, String head, String depRel)
    {
        this.id = id;
        this.form = form;
        this.lemma = lemma;
        this.posTag = posTag;
        this.feats = feats;
        this.head = head;
        this.depRel = depRel;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getForm()
    {
        return form;
    }

    public void setForm(String form)
    {
        this.form = form;
    }

    public String getLemma()
    {
        return lemma;
    }

    public void setLemma(String lemma)
    {
        this.lemma = lemma;
    }

    public String getPosTag()
    {
        return posTag;
    }

    public void setPosTag(String posTag)
    {
        this.posTag = posTag;
    }

    public String getFeats()
    {
        return feats;
    }

    public void setFeats(String feats)
    {
        this.feats = feats;
    }

    public String getHead()
    {
        return head;
    }

    public void setHead(String head)
    {
        this.head = head;
    }

    public String getDepRel()
    {
        return depRel;
    }

    public void setDepRel(String depRel)
    {
        this.depRel = depRel;
    }
}
