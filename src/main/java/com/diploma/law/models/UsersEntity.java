package com.diploma.law.models;

import java.util.List;
import java.util.Objects;

import javax.persistence.*;

@Entity
@Table(name = "users", schema = "law", catalog = "")
public class UsersEntity
{
    private int                  idUser;
    private String               login;
    private String               password;
    private StatusEntity         status;
    private List<ProblemsEntity> locksByIdUser;

    @OneToMany(mappedBy = "usersByUser")
    public List<ProblemsEntity> getLocksByIdUser()
    {
        return locksByIdUser;
    }

    public void setLocksByIdUser(List<ProblemsEntity> locksByIdUser)
    {
        this.locksByIdUser = locksByIdUser;
    }

    @ManyToOne
    @JoinColumn(name = "status", referencedColumnName = "id_status")
    public StatusEntity getStatus()
    {
        return status;
    }

    public void setStatus(StatusEntity status)
    {
        this.status = status;
    }

    @Id
    @Column(name = "id_user")
    public int getIdUser()
    {
        return idUser;
    }

    public void setIdUser(int idUser)
    {
        this.idUser = idUser;
    }

    @Basic
    @Column(name = "login")
    public String getLogin()
    {
        return login;
    }

    public void setLogin(String login)
    {
        this.login = login;
    }

    @Basic
    @Column(name = "password")
    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsersEntity that = (UsersEntity)o;
        return idUser == that.idUser && Objects.equals(login, that.login) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode()
    {

        return Objects.hash(idUser, login, password);
    }
}
