package com.diploma.law.controllers;

import com.diploma.law.models.*;
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
import java.util.Set;

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

    @Autowired
    ObjectService objectService;

    @RequestMapping(value = "/articles", method = RequestMethod.GET)
    public String getArticles(Model model)  {
        List<ArticlesEntity> articles = articleService.findAllArticles();
        model.addAttribute("articles",articles);
        return "articles";
    }

    @RequestMapping(value = "/corpuses", method = RequestMethod.GET)
    public String getCorpuses(Model model)  {
        List<ClarifyingFactsEntity>  clarifyingFact= clarifyingFacts.findAllClaifyingFacts();
        for(int i=0;i<clarifyingFact.size();i++) {
            if (clarifyingFact.get(i).getIdClarifyingFact()==8){
                clarifyingFact.remove(i);
            }
        }
        model.addAttribute("clarifyingFact",clarifyingFact);
        return "corpuses";
    }

    @RequestMapping(value = "/objects", method = RequestMethod.GET)
    public String getObjects(Model model)  {
        List<ObjectsEntity>  objects= objectService.findAllObjects();
        model.addAttribute("objects",objects);
        return "objects";
    }

    @RequestMapping(value = "/{id}/lemmas", method = RequestMethod.GET)
    public String getLemmas(Model model,@PathVariable int id)  {
        ObjectsEntity object = objectService.findById(id);
        List<LemmasEntity> lemmas = object.getLemmas();
        model.addAttribute("object",object);
        model.addAttribute("lemmas",lemmas);
        return "lemmas";
    }



    @RequestMapping(value = "/{id}/deleteObject", method = RequestMethod.GET)
    public ModelAndView deleteObject(@PathVariable int id){
        ModelAndView model = new ModelAndView();
        objectService.deleteObject(id);
        model.setViewName("deleteProblem");
        return model;
    }

    @RequestMapping(value = "/{id}/deleteArticle", method = RequestMethod.GET)
    public ModelAndView deleteArticle(@PathVariable int id){
        ModelAndView model = new ModelAndView();
        articleService.deleteTask(id);
        model.setViewName("deleteProblem");
        return model;
    }

    @RequestMapping(value = "/addObject", method = RequestMethod.GET)
    public String addLemmas(Model model) {
        ObjectsEntity object = new ObjectsEntity();
        model.addAttribute("object", object);
        return "addObject";
    }

    @RequestMapping(value = "/addObject", method = RequestMethod.POST)
    public String saveObject(@ModelAttribute("object") ObjectsEntity object, Model model) {
        objectService.saveObject(object);
        ObjectsEntity objects = new ObjectsEntity();
        model.addAttribute("object", objects);
        model.addAttribute("successMessage", "Добавление прошло успешно");
        return "addObject";
    }

    @RequestMapping(value = "/{id1}/{id}/addLemma", method = RequestMethod.GET)
    public String addObject(Model model, @PathVariable String id) {
        ObjectsEntity object = objectService.findById(Integer.parseInt(id));
        List<LemmasEntity> lemmas = lemmaService.findAllLemmas();
        model.addAttribute("object", object);
        model.addAttribute("lemmas", lemmas);
        return "addLemma";
    }

    @RequestMapping(value = "/addLemma", method = RequestMethod.POST)
    public String saveLemma(@ModelAttribute("object") ObjectsEntity object, Model model) {
        ObjectsEntity obj = objectService.FindByTitle(object.getTitle());
        obj.getLemmas().addAll(object.getLemmas());
        objectService.saveObject(obj);
        model.addAttribute("successMessage", "Добавление прошло успешно");
        return "addLemma";
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
        List<CorpusesEntity> corpuses = corpusService.findAllCorpuses();
        model.addAttribute("corpus", corpuses);
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

    @RequestMapping(value = "/{id}/editObject", method = RequestMethod.GET)
    public String addObject(@PathVariable String id, Model model) {
        ObjectsEntity object = objectService.findById(Integer.parseInt(id));
        model.addAttribute("object", object);
        return "editObject";
    }

    @RequestMapping(value = "/editObject", method = RequestMethod.POST)
    public String editObject(@ModelAttribute("object") ObjectsEntity object, Model model) {
        objectService.saveObject(object);
        model.addAttribute("successMessage", "Редактирование прошло успешно");
        return "editObject";
    }

}
