package com.diploma.law.services;
import com.diploma.law.models.ProblemsEntity;
import com.diploma.law.models.UsersEntity;

import java.util.List;

public interface TaskService {
    ProblemsEntity findById(int id);
    ProblemsEntity FindByNumber(int number);
    List<ProblemsEntity> findAllTasks();
    void saveTask(ProblemsEntity task);
    List<ProblemsEntity> findTasks(UsersEntity user);
}
