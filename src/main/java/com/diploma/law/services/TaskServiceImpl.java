package com.diploma.law.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.diploma.law.DAO.TaskDAO;
import com.diploma.law.models.ProblemsEntity;
import com.diploma.law.models.UsersEntity;

@Service("TaskService")
@Transactional
public class TaskServiceImpl implements TaskService
{

    @Autowired
    TaskDAO taskDAO;

    @Override
    public ProblemsEntity findById(int id)
    {
        return taskDAO.findById(id);
    }

    @Override
    public void saveTask(ProblemsEntity task)
    {
        taskDAO.saveTask(task);
    }

    @Override
    public List<ProblemsEntity> findTasks(UsersEntity user)
    {
        return taskDAO.findTasks(user);
    }

    @Override
    public void deleteTask(int id)
    {
        taskDAO.deleteTask(id);
    }
}
