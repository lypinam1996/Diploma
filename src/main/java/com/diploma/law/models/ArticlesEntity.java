package com.diploma.law.models;

import org.hibernate.annotations.GeneratorType;
import org.omg.CORBA.portable.IDLEntity;
import org.springframework.boot.autoconfigure.web.ResourceProperties;

import javax.persistence.*;
import java.security.Identity;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Articles", schema = "law", catalog = "")
public class ArticlesEntity {
    private int idArticle;
    private String number;
    private String title;
    private String text;
    private Integer section;
    private CorpusesEntity corpus;
    private List<ProblemsEntity> problems;


    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "ArticlesProblems",
            joinColumns = {
                    @JoinColumn(name = "idArticle")},
            inverseJoinColumns = {
                    @JoinColumn(name = "idProblem")})
    public List<ProblemsEntity> getProblems() {
        return problems;
    }

    public void setProblems(List<ProblemsEntity> problems) {
        this.problems = problems;
    }

    @OneToOne
    @JoinColumn(name = "idArticle")
    public CorpusesEntity getCorpus() {
        return corpus;
    }

    public void setCorpus(CorpusesEntity corpus) {
        this.corpus = corpus;
    }


    @Id
    @Column(name = "idArticle")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getIdArticle() {
        return idArticle;
    }

    public void setIdArticle(int idArticle) {
        this.idArticle = idArticle;
    }

    @Basic
    @Column(name = "number")
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Basic
    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "text")
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Basic
    @Column(name = "section")
    public Integer getSection() {
        return section;
    }

    public void setSection(Integer section) {
        this.section = section;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArticlesEntity that = (ArticlesEntity) o;
        return idArticle == that.idArticle &&
                Objects.equals(number, that.number) &&
                Objects.equals(title, that.title) &&
                Objects.equals(text, that.text) &&
                Objects.equals(section, that.section);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idArticle, number, title, text, section);
    }
}
