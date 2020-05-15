package com.diploma.law.DAO;

import com.diploma.law.models.UsersEntity;
import java.util.List;

public interface UserDAO
{
    UsersEntity findById(int id);

    UsersEntity FindByLogin(String title);

    List<UsersEntity> findAllUsers();

    void saveUser(UsersEntity user);
}
