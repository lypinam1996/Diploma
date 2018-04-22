package com.diploma.law.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Objects", schema = "law", catalog = "")
public class ObjectsEntity {
    private int idObject;
    private String title;
    private List<ClarifyingFactsEntity> clarifyingfacts;
    private List<LemmasEntity> lemmas;


    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "ObjectsLemmas",
            joinColumns = {
                    @JoinColumn(name = "idObject")},
            inverseJoinColumns = {
                    @JoinColumn(name = "idLemma")})
    public List<LemmasEntity> getLemmas() {
        return lemmas;
    }

    public void setLemmas(List<LemmasEntity> lemmas) {
        this.lemmas = lemmas;
    }


    @OneToMany(mappedBy = "object")
    public List<ClarifyingFactsEntity> getClarifyingfacts() {
        return clarifyingfacts;
    }

    public void setClarifyingfacts(List<ClarifyingFactsEntity> clarifyingfacts) {
        this.clarifyingfacts = clarifyingfacts;
    }

    @Id
    @Column(name = "idObject")
    public int getIdObject() {
        return idObject;
    }

    public void setIdObject(int idObject) {
        this.idObject = idObject;
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
        ObjectsEntity that = (ObjectsEntity) o;
        return idObject == that.idObject &&
                Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idObject, title);
    }
}
