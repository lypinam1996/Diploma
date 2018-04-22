package com.diploma.law.models;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Grammars", schema = "law", catalog = "")
public class GrammarsEntity {
    private String idGrammar;
    private String title;
    private String alias;
    private String description;
    private List<LemmasEntity> lemmas;
    private List<WordformsEntity> wordforms;


    @ManyToMany
    @JoinTable(name = "WordformsGrammars",
            joinColumns = {
                    @JoinColumn(name = "idGrammar")},
            inverseJoinColumns = {
                    @JoinColumn(name = "idWordform")})
    public List<WordformsEntity> getWordforms() {
        return wordforms;
    }

    public void setWordforms(List<WordformsEntity> wordforms) {
        this.wordforms = wordforms;
    }


    @ManyToMany( cascade = CascadeType.ALL)
    @JoinTable(name = "GrammarsLemmas",
            joinColumns = {
                    @JoinColumn(name = "idGrammar")},
            inverseJoinColumns = {
                    @JoinColumn(name = "idLemma")})
    public List<LemmasEntity> getLemmas() {
        return lemmas;
    }

    public void setLemmas(List<LemmasEntity> lemmas) {
        this.lemmas = lemmas;
    }

    @Id
    @Column(name = "idGrammar")
    public String getIdGrammar() {
        return idGrammar;
    }

    public void setIdGrammar(String idGrammar) {
        this.idGrammar = idGrammar;
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
    @Column(name = "alias")
    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Basic
    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GrammarsEntity that = (GrammarsEntity) o;
        return Objects.equals(idGrammar, that.idGrammar) &&
                Objects.equals(title, that.title) &&
                Objects.equals(alias, that.alias) &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idGrammar, title, alias, description);
    }
}
