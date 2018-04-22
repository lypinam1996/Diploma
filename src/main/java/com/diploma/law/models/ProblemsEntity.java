package com.diploma.law.models;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Problems", schema = "law", catalog = "")
public class ProblemsEntity {
    private int idProblem;
    private String title;
    private Integer number;
    private String text;
    private List<ArticlesEntity> article;
    private UsersEntity usersByUser;


    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "ArticlesProblems",
            joinColumns = {
                    @JoinColumn(name = "idProblem")},
            inverseJoinColumns = {
                    @JoinColumn(name = "idArticle")})
    public List<ArticlesEntity> getArticle() {
        return article;
    }

    public void setArticle(List<ArticlesEntity> article) {
        this.article = article;
    }

    @ManyToOne
    @JoinColumn(name = "id_user")
    public UsersEntity getUsersByUser() {
        return usersByUser;
    }

    public void setUsersByUser(UsersEntity usersByUser) {
        this.usersByUser = usersByUser;
    }

    @Id
    @Column(name = "idProblem")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getIdProblem() {
        return idProblem;
    }

    public void setIdProblem(int idProblem) {
        this.idProblem = idProblem;
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
    @Column(name = "number")
    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    @Basic
    @Column(name = "text")
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProblemsEntity that = (ProblemsEntity) o;
        return idProblem == that.idProblem &&
                Objects.equals(title, that.title) &&
                Objects.equals(number, that.number) &&
                Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idProblem, title, number, text);
    }
}
