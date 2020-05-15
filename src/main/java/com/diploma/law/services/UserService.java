package com.diploma.law.services;

import com.diploma.law.models.UsersEntity;

public interface UserService
{
    UsersEntity findById(int id);

    UsersEntity FindByLogin(String title);

    void saveUser(UsersEntity user);
}
