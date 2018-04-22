package com.diploma.law.models;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Wordforms", schema = "law", catalog = "")
public class WordformsEntity {
    private int idWordform;
    private String title;
    private List<GrammarsEntity> grammars;
    private LemmasEntity lemma;

    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="idLemma")
    public LemmasEntity getLemma() {
        return lemma;
    }
    public void setLemma(LemmasEntity lemma) {
        this.lemma = lemma;
    }

    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "WordformsGrammars",
            joinColumns = {
                    @JoinColumn(name = "idWordform")},
            inverseJoinColumns = {
                    @JoinColumn(name = "idGrammar")})
    public List<GrammarsEntity> getGrammars() {
        return grammars;
    }

    public void setGrammars(List<GrammarsEntity> grammars) {
        this.grammars = grammars;
    }

    @Id
    @Column(name = "idWordform")
    public int getIdWordform() {
        return idWordform;
    }

    public void setIdWordform(int idWordform) {
        this.idWordform = idWordform;
    }

    @Basic
    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WordformsEntity that = (WordformsEntity) o;
        return idWordform == that.idWordform &&
                Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idWordform, title);
    }
}
