package com.diploma.law.controllers;

import com.diploma.law.models.*;
import com.diploma.law.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.*;

@Controller
public class MainController {

    @Autowired
    UserService userService;

    @Autowired
    TaskService taskService;

    @Autowired
    LemmaService lemmaService;

    @Autowired
    WordFormsService wordForms;

    @Autowired
    ClarifyingFactsService clarifyingFacts;



    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String getTasks(Model model){
        List<ProblemsEntity> tasks;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UsersEntity user = userService.FindByLogin(auth.getName());
        tasks = taskService.findAllTasks();
        model.addAttribute("tasks",tasks);
        return "mainPage";
    }
    ////////////////////////////////


    @RequestMapping(value = "/problem", method = RequestMethod.POST)
    public String add(@ModelAttribute("problem") ProblemsEntity problem,
                                          Model model) {
        if (problem.getText()!=null){
            String text = problem.getText();
            ArrayList<String> words = getWordsFromText(text);
            ArrayList<List<WordformsEntity>> wordformsEntities = getAllWordForms(words);
            ArrayList<LemmasEntity> lemmas = getAllLemmas(wordformsEntities);
            ArrayList<ObjectsEntity> objects = getAllObjects(lemmas);
            List<ClarifyingFactsEntity> listclarifyingfacts = findingAllClarifyingFactsThatBelongToTheObject(objects);//нашли все доп слова, которые подходят под объект жизнь человека
            List<ClarifyingFactsEntity> cf = new ArrayList<ClarifyingFactsEntity>();
            List<ArticlesEntity> articles = new ArrayList<>();
            if (findingAllClarifyingFactsThatOccurInTheText(lemmas, listclarifyingfacts).size()!=0)//проверяет есть ли леммы, которые подходят под под статьи
            {
                cf = findingAllClarifyingFactsThatOccurInTheText(lemmas,listclarifyingfacts);//находит эти леммы
                for(int i=0;i<cf.size();i++) {
                    articles.add(cf.get(i).getCorpus().getArticle());
                }
            }
            else{
                for(int i=0;i<listclarifyingfacts.size();i++) {
                    if (listclarifyingfacts.get(i).getLemma()==null)
                    articles.add(listclarifyingfacts.get(i).getCorpus().getArticle());
                }
            }
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            UsersEntity user = userService.FindByLogin(auth.getName());
            problem.setUsersByUser(user);
            problem.setArticle(articles);
            problem.setText(text);
            taskService.saveTask(problem);
            model.addAttribute("articles",articles);
        }
        return "problem";
    }

    private List<ClarifyingFactsEntity> findingAllClarifyingFactsThatBelongToTheObject(ArrayList<ObjectsEntity> objects) {
        List<ClarifyingFactsEntity> listclarifyingfacts = new ArrayList<>();
        for (int i = 0; i < objects.size(); i++) {
            listclarifyingfacts = clarifyingFacts.findAllFactsObject(objects.get(i).getIdObject());
        }
        return listclarifyingfacts;
    }

    private List<ClarifyingFactsEntity> findingAllClarifyingFactsThatOccurInTheText(ArrayList<LemmasEntity> lemmas,
                                                                              List<ClarifyingFactsEntity> listclarifyingfacts) {

        List<ClarifyingFactsEntity> clarifyingfacts = new ArrayList<>();
        for(int i=0;i< lemmas.size();i++) {
            for (int j=0;j < listclarifyingfacts.size();j++) {
                if (listclarifyingfacts.get(j).getLemma() != null &&
                        lemmas.get(i).getIdLemma() == listclarifyingfacts.get(j).getLemma().getIdLemma()) {
                    clarifyingfacts.add(listclarifyingfacts.get(j));
                }
            }
        }
        return clarifyingfacts;
    }

    private ArrayList<ObjectsEntity> getAllObjects(ArrayList<LemmasEntity> lemmas) {
        ArrayList<ObjectsEntity> objects = new ArrayList<ObjectsEntity>();
        ArrayList<ObjectsEntity> res = new ArrayList<ObjectsEntity>();
        for (int i = 0; i < lemmas.size(); i++) {
            for (int j = 0; j < lemmas.get(i).getObjects().size(); j++) {
                objects.add(lemmas.get(i).getObjects().get(j));
            }
        }
        Set<ObjectsEntity> set = new HashSet<ObjectsEntity>();
        for (int i = 0; i < objects.size(); i++) {
            set.add(objects.get(i));
        }
        ObjectsEntity[] result = set.toArray(new ObjectsEntity[set.size()]);
        for (int i = 0; i < result.length; i++) {
            res.add(result[i]);
        }
        return res;
    }

    private ArrayList<LemmasEntity> getAllLemmas(ArrayList<List<WordformsEntity>> wordformsEntities) {
        ArrayList<LemmasEntity> lemmas = new ArrayList<LemmasEntity>();
        ArrayList<LemmasEntity> res = new ArrayList<LemmasEntity>();
        for (int i = 0; i < wordformsEntities.size(); i++) {
            for (int j = 0; j < wordformsEntities.get(i).size(); j++) {
                lemmas.add(wordformsEntities.get(i).get(j).getLemma());
            }
        }
        Set<LemmasEntity> set = new HashSet<LemmasEntity>();
        for (int i = 0; i < lemmas.size(); i++) {
            set.add(lemmas.get(i));
        }
        LemmasEntity[] result = set.toArray(new LemmasEntity[set.size()]);
        for (int i = 0; i < result.length; i++) {
            res.add(result[i]);
        }
        return res;
    }

    private ArrayList<List<WordformsEntity>> getAllWordForms(ArrayList<String> words) {
        ArrayList<List<WordformsEntity>> wordformsEntities = new ArrayList<List<WordformsEntity>>();
        for (int i = 0; i < words.size(); i++) {
            wordformsEntities.add(wordForms.FindByTitle(words.get(i)));
        }
        return wordformsEntities;
    }

    private ArrayList<String> getWordsFromText(String text) {
        String[] sentences = text.split("\\.");
        ArrayList<String> words = new ArrayList<String>();
        for (int i = 0; i < sentences.length; i++) {
            sentences[i] = sentences[i].replaceAll("[^A-Za-zА-Яа-я0-9,Ё,ё]", " ");
            sentences[i] = sentences[i].replaceAll("  ", " ");
            String[] word = sentences[i].split(" ");
            for (int j = 0; j < word.length; j++) {
                words.add(word[j].trim());
            }
        }
        return words;
    }

}