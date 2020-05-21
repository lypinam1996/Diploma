package com.diploma.law.DAO;

import java.util.List;

import com.diploma.law.models.ProblemsEntity;
import com.diploma.law.models.UsersEntity;

public interface TaskDAO
{
    ProblemsEntity findById(int id);

    void saveTask(ProblemsEntity task);

    List<ProblemsEntity> findTasks(UsersEntity user);

    void deleteTask(int id);
}
