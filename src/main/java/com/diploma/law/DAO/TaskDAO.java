package com.diploma.law.DAO;
import com.diploma.law.models.ProblemsEntity;
import com.diploma.law.models.UsersEntity;

import java.util.List;

public interface TaskDAO {
    ProblemsEntity findById(int id);
    ProblemsEntity FindByNumber(int number);
    List<ProblemsEntity> findAllTasks();
    void saveTask(ProblemsEntity task);
    List<ProblemsEntity> findTasks(UsersEntity user);
    void deleteTask(int id);
}
