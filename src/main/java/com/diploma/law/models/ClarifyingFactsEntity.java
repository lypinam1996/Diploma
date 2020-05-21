package com.diploma.law.models;

import java.util.Objects;

import javax.persistence.*;

@Entity
@Table(name = "ClarifyingFacts", schema = "law", catalog = "")
public class ClarifyingFactsEntity
{
    private int            idClarifyingFact;
    private CorpusesEntity corpus;
    private ObjectsEntity  object;
    private LemmasEntity   lemma;
    private String         question;

    public String getQuestion()
    {
        return question;
    }

    public void setQuestion(String question)
    {
        this.question = question;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "idLemma")
    public LemmasEntity getLemma()
    {
        return lemma;
    }

    public void setLemma(LemmasEntity lemma)
    {
        this.lemma = lemma;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "idObject")
    public ObjectsEntity getObject()
    {
        return object;
    }

    public void setObject(ObjectsEntity object)
    {
        this.object = object;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "idCorpus")
    public CorpusesEntity getCorpus()
    {
        return corpus;
    }

    public void setCorpus(CorpusesEntity corpus)
    {
        this.corpus = corpus;
    }

    @Id
    @Column(name = "idClarifyingFact")
    public int getIdClarifyingFact()
    {
        return idClarifyingFact;
    }

    public void setIdClarifyingFact(int idClarifyingFact)
    {
        this.idClarifyingFact = idClarifyingFact;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClarifyingFactsEntity that = (ClarifyingFactsEntity)o;
        return idClarifyingFact == that.idClarifyingFact;
    }

    @Override
    public int hashCode()
    {

        return Objects.hash(idClarifyingFact);
    }
}
