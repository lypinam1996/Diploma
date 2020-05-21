package com.diploma.law.DAO;

import java.util.List;

import com.diploma.law.models.UsersEntity;

public interface UserDAO
{
    UsersEntity findById(int id);

    UsersEntity FindByLogin(String title);

    List<UsersEntity> findAllUsers();

    void saveUser(UsersEntity user);
}
