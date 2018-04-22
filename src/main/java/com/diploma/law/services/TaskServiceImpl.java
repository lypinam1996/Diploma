package com.diploma.law.services;
import com.diploma.law.DAO.TaskDAO;
import com.diploma.law.models.ProblemsEntity;
import com.diploma.law.models.UsersEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service("TaskService")
@Transactional
public class TaskServiceImpl implements TaskService{

    @Autowired
    TaskDAO taskDAO;

    @Override
    public ProblemsEntity findById(int id) {
        return taskDAO.findById(id);
    }

    @Override
    public ProblemsEntity FindByNumber(int number) {
        return taskDAO.FindByNumber(number);
    }

    @Override
    public List<ProblemsEntity> findAllTasks() {
        return taskDAO.findAllTasks();
    }

    @Override
    public void saveTask(ProblemsEntity task) {
        taskDAO.saveTask(task);
    }

    @Override
    public List<ProblemsEntity> findTasks(UsersEntity user) {
        return taskDAO.findTasks(user);
    }
}
