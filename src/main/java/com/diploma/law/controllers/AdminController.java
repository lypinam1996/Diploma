package com.diploma.law.controllers;

import com.diploma.law.models.ArticlesEntity;
import com.diploma.law.models.CorpusesEntity;
import com.diploma.law.models.ProblemsEntity;
import com.diploma.law.models.UsersEntity;
import com.diploma.law.services.*;
import com.github.oxaoo.mp4ru.exceptions.FailedParsingException;
import com.github.oxaoo.mp4ru.exceptions.InitRussianParserException;
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

import java.util.ArrayList;
import java.util.List;

@Controller
public class AdminController {

    @Autowired
    UserService userService;

    @Autowired
    LemmaService lemmaService;

    @Autowired
    ClarifyingFactsService clarifyingFacts;

    @Autowired
    ArticleService articleService;

    @Autowired
    CorpusService corpusService;

    @RequestMapping(value = "/articles", method = RequestMethod.GET)
    public String getArticles(Model model)  {
        List<ArticlesEntity> articles = articleService.findAllArticles();
        model.addAttribute("articles",articles);
        return "articles";
    }

    @RequestMapping(value = "/{id}/deleteArticle", method = RequestMethod.GET)
    public ModelAndView deleteArticle(@PathVariable int id){
        ModelAndView model = new ModelAndView();
        articleService.deleteTask(id);
        model.setViewName("deleteProblem");
        return model;
    }

    @RequestMapping(value = "/addArticle", method = RequestMethod.GET)
    public String addArticle(Model model) {
        ArticlesEntity article = new ArticlesEntity();
        List<CorpusesEntity> corpuses = corpusService.findAllCorpuses();
        model.addAttribute("corpus", corpuses);
        model.addAttribute("article", article);
        return "addArticle";
    }

    @RequestMapping(value = "/addArticle", method = RequestMethod.POST)
    public String saveArticle(@ModelAttribute("article") ArticlesEntity article, Model model) {
        articleService.saveArticle(article);
        ArticlesEntity article2 = new ArticlesEntity();
        model.addAttribute("article", article2);
        model.addAttribute("successMessage", "Добавление прошло успешно");
        return "addArticle";
    }

    @RequestMapping(value = "/{id}/editArticle", method = RequestMethod.GET)
    public String addArticle2(@PathVariable String id, Model model) {
        ArticlesEntity articlesEntity = articleService.findById(Integer.parseInt(id));
        model.addAttribute("article", articlesEntity);
        return "editArticle";
    }

    @RequestMapping(value = "/editArticle", method = RequestMethod.POST)
    public String editArticle(@ModelAttribute("article") ArticlesEntity article, Model model) {
        articleService.saveArticle(article);
        model.addAttribute("successMessage", "Редактирование прошло успешно");
        return "editArticle";
    }

}
