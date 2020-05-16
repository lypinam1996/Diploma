package com.diploma.law.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.diploma.law.models.ArticlesEntity;
import com.diploma.law.models.ProblemsEntity;
import com.diploma.law.models.UsersEntity;
import com.diploma.law.services.AlgorithmService;
import com.diploma.law.services.TaskService;
import com.diploma.law.services.UserService;

@Controller
public class MainController
{

    @Autowired
    UserService      userService;

    @Autowired
    TaskService      taskService;

    @Autowired
    AlgorithmService algorithm;

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String getTasks(Model model)
    {
        List<ProblemsEntity> tasks;
        UsersEntity user = userService.getUser();
        String page;
        if (user.getIdUser() == 1)
        {
            page = "adminMainPage";
        }
        else
        {
            tasks = taskService.findTasks(user);
            model.addAttribute("tasks", tasks);
            page = "mainPage";
        }
        return page;
    }

    @RequestMapping(value = "/problem", method = RequestMethod.GET)
    public String getProblem(Model model)
    {
        ProblemsEntity problem = new ProblemsEntity();
        model.addAttribute("problem", problem);
        return "problem";
    }

    @RequestMapping(value = "/problem", method = RequestMethod.POST)
    public String add(@ModelAttribute("problem") ProblemsEntity problem, Model model, BindingResult bindingResult)
    {
        model = algorithm.qualifyOffense(problem, model, bindingResult);
        return "problem";
    }

    @RequestMapping(value = "/{id}/seeProblem", method = RequestMethod.GET)
    public ModelAndView seeProblem(@PathVariable int id)
    {
        ProblemsEntity problem = taskService.findById(id);
        ModelAndView model = new ModelAndView();
        List<ArticlesEntity> articles = problem.getArticle();
        model.addObject("task", problem);
        model.addObject("articles", articles);
        model.setViewName("seeProblem");
        return model;
    }

    @RequestMapping(value = "/{id}/deleteProblem", method = RequestMethod.GET)
    public ModelAndView deleteProblem(@PathVariable int id)
    {
        ModelAndView model = new ModelAndView();
        taskService.deleteTask(id);
        model.setViewName("deleteProblem");
        return model;
    }


}
