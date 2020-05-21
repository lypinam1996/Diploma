package com.diploma.law.models;

import java.util.List;
import java.util.Objects;

import javax.persistence.*;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@Table(name = "Corpuses", schema = "law", catalog = "")
public class CorpusesEntity
{
    private int                         idCorpus;
    private String                      title;
    private ArticlesEntity              article;
    private List<ClarifyingFactsEntity> clarifyingfacts;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "corpus")
    public List<ClarifyingFactsEntity> getClarifyingfacts()
    {
        return clarifyingfacts;
    }

    public void setClarifyingfacts(List<ClarifyingFactsEntity> clarifyingfacts)
    {
        this.clarifyingfacts = clarifyingfacts;
    }

    @OneToOne
    @JoinColumn(name = "idCorpus")
    public ArticlesEntity getArticle()
    {
        return article;
    }

    public void setArticle(ArticlesEntity article)
    {
        this.article = article;
    }

    @Id
    @Column(name = "idCorpus")
    public int getIdCorpus()
    {
        return idCorpus;
    }

    public void setIdCorpus(int idCorpus)
    {
        this.idCorpus = idCorpus;
    }

    @Basic
    @Column(name = "title")
    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CorpusesEntity that = (CorpusesEntity)o;
        return idCorpus == that.idCorpus && Objects.equals(title, that.title);
    }

    @Override
    public int hashCode()
    {

        return Objects.hash(idCorpus, title);
    }
}
