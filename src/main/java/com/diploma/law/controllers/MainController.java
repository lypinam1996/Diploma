package com.diploma.law.controllers;

import com.diploma.law.models.*;
import com.diploma.law.services.*;
import com.github.oxaoo.mp4ru.exceptions.FailedParsingException;
import com.github.oxaoo.mp4ru.exceptions.InitRussianParserException;
import com.github.oxaoo.mp4ru.syntax.RussianParser;
import ognl.IntHashMap;
import org.maltparser.MaltParserService;
import org.maltparser.core.exception.MaltChainedException;
import org.maltparser.core.syntaxgraph.DependencyStructure;
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

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.*;

@Controller
public class MainController {

    @Autowired
    UserService userService;

    @Autowired
    GrammarsService grammarsService;

    @Autowired
    TaskService taskService;

    @Autowired
    LemmaService lemmaService;

    @Autowired
    WordFormsService wordForms;

    @Autowired
    ClarifyingFactsService clarifyingFacts;

    @Autowired
    AlgorithmService algorithm;

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String getTasks(Model model)  {
        List<ProblemsEntity> tasks;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UsersEntity user = userService.FindByLogin(auth.getName());
        String page="";
        if(user.getIdUser()==1){
             page = "adminMainPage";
        }
        else{
            tasks = taskService.findTasks(user);
            model.addAttribute("tasks",tasks);
             page = "mainPage";
        }

        return page;
    }

    @RequestMapping(value = "/problem", method = RequestMethod.GET)
    public String getProblem(Model model)  {
        ProblemsEntity problem = new ProblemsEntity();
        model.addAttribute("problem",problem);
        return "problem";
    }

    @RequestMapping(value = "/problem", method = RequestMethod.POST)
    public String add(@ModelAttribute("problem") ProblemsEntity problem,
                                          Model model,BindingResult bindingResult) throws FailedParsingException, InitRussianParserException {

        if (problem.getText()!=null){
            List<ArticlesEntity> articles = algorithm.qualifyOffense(problem,bindingResult);
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            UsersEntity user = userService.FindByLogin(auth.getName());
            problem.setUsersByUser(user);
            problem.setArticle(articles);
            problem.setText(problem.getText());
            if(!articles.isEmpty()) {
                ArrayList<ArrayList<String>> subject = algorithm.getVictimAndSubject(problem.getText(),articles.get(0));
                List<String> subjects= subject.get(0);
                List<String> victims= subject.get(1);
                subjects=question(subjects," субъект");
                victims=question(victims," потерпевший");
                if(subjects.isEmpty()){
                    subjects.add("Не определён");
                }
                if(victims.isEmpty()){
                    victims.add("Не определён");
                }
                model.addAttribute("articles", articles.get(0));
                model.addAttribute("subjects", subjects);
                model.addAttribute("victims", victims);
                problem.setSubject(subjects.get(0));
                problem.setVictims(victims.get(0));
                taskService.saveTask(problem);
            }
        }
        return "problem";
    }

    private List<String> question( List<String> subjects,String title){
        int i=0;
        while (!subjects.isEmpty() && i<subjects.size()){
            ClassNameHere class1 = new ClassNameHere();
            boolean result = class1.infoBox(subjects.get(i) + title+" преступления?", "Вопрос");
            if(result==false){
                subjects.remove(i);
            }
            else{
                i++;
            }
        }
        return subjects;
    }

    @RequestMapping(value = "/{id}/seeProblem", method = RequestMethod.GET)
    public ModelAndView seeProblem(@PathVariable int id){
        ProblemsEntity problem = taskService.findById(id);
        ModelAndView model = new ModelAndView();
        List<ArticlesEntity> articles = problem.getArticle();
        model.addObject("task",problem);
        model.addObject("articles",articles);
        model.setViewName("seeProblem");
        return model;
    }

    @RequestMapping(value = "/{id}/deleteProblem", method = RequestMethod.GET)
    public ModelAndView deleteProblem(@PathVariable int id){
        ModelAndView model = new ModelAndView();
        taskService.deleteTask(id);
        model.setViewName("deleteProblem");
        return model;
    }





}
