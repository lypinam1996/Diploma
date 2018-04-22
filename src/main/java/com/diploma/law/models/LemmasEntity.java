package com.diploma.law.models;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Lemmas", schema = "law", catalog = "")
public class LemmasEntity {
    private int idLemma;
    private String title;
    private List<ClarifyingFactsEntity> clarifyingfacts;
    private List<ObjectsEntity> objects;
    private List<WordformsEntity> wordforms;
   // private List<GrammarsEntity> grammars;


    /*@ManyToMany( cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "GrammarsLemmas",
            joinColumns = {
                    @JoinColumn(name = "idLemma")},
            inverseJoinColumns = {
                    @JoinColumn(name = "idGrammar")})
    public List<GrammarsEntity> getGrammars() {
        return grammars;
    }

    public void setGrammars(List<GrammarsEntity> grammars) {
        this.grammars = grammars;
    }*/

    @OneToMany(mappedBy = "lemma", cascade = CascadeType.ALL)
    public List<WordformsEntity> getWordforms() {
        return wordforms;
    }

    public void setWordforms(List<WordformsEntity> wordforms) {
        this.wordforms = wordforms;
    }

    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinTable(name = "ObjectsLemmas",
            joinColumns = {
                    @JoinColumn(name = "idLemma")},
            inverseJoinColumns = {
                    @JoinColumn(name = "idObject")})
    public List<ObjectsEntity> getObjects() {
        return objects;
    }

    public void setObjects(List<ObjectsEntity> objects) {
        this.objects = objects;
    }

    @OneToMany(mappedBy = "lemma")
    public List<ClarifyingFactsEntity> getClarifyingfacts() {
        return clarifyingfacts;
    }

    public void setClarifyingfacts(List<ClarifyingFactsEntity> clarifyingfacts) {
        this.clarifyingfacts = clarifyingfacts;
    }

    @Id
    @Column(name = "idLemma")
    public int getIdLemma() {
        return idLemma;
    }

    public void setIdLemma(int idLemma) {
        this.idLemma = idLemma;
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
        LemmasEntity that = (LemmasEntity) o;
        return idLemma == that.idLemma &&
                Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idLemma, title);
    }
}
