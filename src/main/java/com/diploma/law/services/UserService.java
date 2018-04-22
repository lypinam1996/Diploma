package com.diploma.law.services;
import com.diploma.law.models.UsersEntity;

import java.util.List;

public interface UserService {
    UsersEntity findById(int id);
    UsersEntity FindByLogin(String title);
    List<UsersEntity> findAllUsers();
    void saveUser(UsersEntity user);
}
