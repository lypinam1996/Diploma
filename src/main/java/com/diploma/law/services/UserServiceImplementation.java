package com.diploma.law.services;
import com.diploma.law.DAO.StatusDAO;
import com.diploma.law.DAO.UserDAO;
import com.diploma.law.models.StatusEntity;
import com.diploma.law.models.UsersEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service("UsersService")
@Transactional
public class UserServiceImplementation implements UserService {

    @Autowired
    UserDAO userDao;

    @Autowired
    StatusDAO statusDAO;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public UsersEntity findById(int id) {
        return userDao.findById(id);
    }

    @Override
    public UsersEntity FindByLogin(String title) {
        return userDao.FindByLogin(title);
    }

    @Override
    public List<UsersEntity> findAllUsers() {
        return userDao.findAllUsers();
    }

    @Override
    public void saveUser(UsersEntity user) {
        StatusEntity status = statusDAO.findById(2);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus(status);

        userDao.saveUser(user);

    }
}
