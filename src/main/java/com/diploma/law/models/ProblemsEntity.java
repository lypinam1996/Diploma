package com.diploma.law.models;

import java.util.List;
import java.util.Objects;

import javax.persistence.*;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@Table(name = "Problems", schema = "law", catalog = "")
public class ProblemsEntity
{
    private int                  idProblem;
    private String               title;
    private Integer              number;
    private String               text;
    private List<ArticlesEntity> article;
    private UsersEntity          usersByUser;
    private String               subject;
    private String               victims;
    private String               date;
    private String               address;
    private String               weapon;

    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "ProblemArticle", joinColumns = { @JoinColumn(name = "idProblem") }, inverseJoinColumns = {
            @JoinColumn(name = "idArticle") })
    public List<ArticlesEntity> getArticle()
    {
        return article;
    }

    public void setArticle(List<ArticlesEntity> article)
    {
        this.article = article;
    }

    @ManyToOne
    @JoinColumn(name = "id_user")
    public UsersEntity getUsersByUser()
    {
        return usersByUser;
    }

    public void setUsersByUser(UsersEntity usersByUser)
    {
        this.usersByUser = usersByUser;
    }

    @Id
    @Column(name = "idProblem")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getIdProblem()
    {
        return idProblem;
    }

    public void setIdProblem(int idProblem)
    {
        this.idProblem = idProblem;
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

    @Basic
    @Column(name = "number")
    public Integer getNumber()
    {
        return number;
    }

    public void setNumber(Integer number)
    {
        this.number = number;
    }

    @Basic
    @Column(name = "text")
    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    @Basic
    @Column(name = "date")
    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProblemsEntity that = (ProblemsEntity)o;
        return idProblem == that.idProblem && Objects.equals(title, that.title) && Objects.equals(number, that.number)
                && Objects.equals(text, that.text);
    }

    @Override
    public int hashCode()
    {

        return Objects.hash(idProblem, title, number, text);
    }

    @Basic
    @Column(name = "subject")
    public String getSubject()
    {
        return subject;
    }

    public void setSubject(String subject)
    {
        this.subject = subject;
    }

    @Basic
    @Column(name = "victims")
    public String getVictims()
    {
        return victims;
    }

    public void setVictims(String victims)
    {
        this.victims = victims;
    }

    @Basic
    @Column(name = "address")
    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    @Basic
    @Column(name = "weapon")
    public String getWeapon() {
        return weapon;
    }

    public void setWeapon(String weapon) {
        this.weapon = weapon;
    }
}
