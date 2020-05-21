package com.diploma.law.models;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ProblemArticle", schema = "law", catalog = "")
public class ProblemArticleEntity
{
    private int idProblemArticle;

    @Id
    @Column(name = "idProblemArticle")
    public int getIdProblemArticle()
    {
        return idProblemArticle;
    }

    public void setIdProblemArticle(int idProblemArticle)
    {
        this.idProblemArticle = idProblemArticle;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProblemArticleEntity that = (ProblemArticleEntity)o;
        return idProblemArticle == that.idProblemArticle;
    }

    @Override
    public int hashCode()
    {

        return Objects.hash(idProblemArticle);
    }
}
