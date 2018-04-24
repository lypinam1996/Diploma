package com.diploma.law.controllers;

import com.diploma.law.models.*;
import com.diploma.law.services.*;
import ognl.IntHashMap;
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




    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String getTasks(Model model){
        List<ProblemsEntity> tasks;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UsersEntity user = userService.FindByLogin(auth.getName());
        tasks = taskService.findTasks(user);
        model.addAttribute("tasks",tasks);
        return "mainPage";
    }
    ////////////////////////////////


    @RequestMapping(value = "/problem", method = RequestMethod.POST)
    public String add(@ModelAttribute("problem") ProblemsEntity problem,
                                          Model model,BindingResult bindingResult) {

        if (problem.getText()!=null){
            String text = problem.getText();
            ArrayList<String> words = getWordsFromText(text);
            ArrayList<WordformsEntity> wordformsEntities = getAllWordForms(words);
            ArrayList<LemmasEntity> lemmas = getAllLemmas(wordformsEntities);
            ArrayList<ObjectsEntity> objects = getAllObjects(lemmas);
            if (objects.isEmpty()) {
                bindingResult
                        .rejectValue("title", "error.title",
                                "*Не возможно квалифицировать данное преступление. Пожалуйса, переформулируйте его другими словами.");
            }
            else{
                List<ClarifyingFactsEntity> listclarifyingfacts = findingAllClarifyingFactsThatBelongToTheObject(objects);//нашли все доп слова, которые подходят под объект жизнь человека
                List<ClarifyingFactsEntity> cf = new ArrayList<ClarifyingFactsEntity>();
                List<ArticlesEntity> articles = new ArrayList<>();
                if (findingAllClarifyingFactsThatOccurInTheText(lemmas, listclarifyingfacts).size()!=0)//проверяет есть ли леммы, которые подходят под под статьи
                {
                    cf = findingAllClarifyingFactsThatOccurInTheText(lemmas,listclarifyingfacts);//находит эти леммы
                    for(int i=0;i<cf.size();i++) {
                        String information=cf.get(i).getQuestion();
                        ClassNameHere class1 = new ClassNameHere();
                        boolean result = class1.infoBox(information, "Вопрос");
                        if (result) {
                            articles.add(cf.get(i).getCorpus().getArticle());
                        }
                        else{
                            for(int g=0;g<listclarifyingfacts.size();g++) {
                                if (listclarifyingfacts.get(g).getLemma()==null)
                                    articles.add(listclarifyingfacts.get(g).getCorpus().getArticle());
                            }
                        }
                    }
                }
                else{
                    for(int i=0;i<listclarifyingfacts.size();i++) {
                        if (listclarifyingfacts.get(i).getLemma()==null)
                            articles.add(listclarifyingfacts.get(i).getCorpus().getArticle());
                    }
                }
                Map<WordformsEntity,List<GrammarsEntity>> grammarsWordFOrms = findingAllGrammarsWordFOrms(wordformsEntities);
                Map<LemmasEntity,List<GrammarsEntity>> grammarsLemmas = findingAllGrammarsLemmas(lemmas);
                List<LemmasEntity> lemmaNouns = findingAllLemmasWhichAreNouns(grammarsLemmas);
                ArrayList<WordformsEntity> wordformsIm = findingAllWordsWhicAreIm(grammarsWordFOrms);
                List<LemmasEntity> subjects = getSubject(wordformsIm,lemmaNouns);
                List<LemmasEntity> finalSubjects = getFinalSubjects(subjects,"преступником");
                ArrayList<WordformsEntity> wordformsVict = findingAllWordsWhicAreVIctim(grammarsWordFOrms);
                List<LemmasEntity> victims = getSubject(wordformsVict,lemmaNouns);
                List<LemmasEntity> finalVictims = getFinalSubjects(victims,"пострадавшим");
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                UsersEntity user = userService.FindByLogin(auth.getName());
                problem.setUsersByUser(user);
                problem.setArticle(articles);
                problem.setText(text);
                taskService.saveTask(problem);
                model.addAttribute("articles",articles);
                model.addAttribute("subject",subjects);
                model.addAttribute("victim",victims);
            }

        }
        return "problem";
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

    private List<LemmasEntity> getFinalSubjects(List<LemmasEntity> allSubjects, String noun){
        for(int i=0;i<allSubjects.size();i++) {
            ClassNameHere class1 = new ClassNameHere();
            String information = allSubjects.get(i).getTitle()+" является "+noun+"?";
            boolean result = class1.infoBox(information, "Вопрос");
            if (result==false) {
                allSubjects.remove(i);
            }
        }
        return allSubjects;
    }

    private ArrayList<LemmasEntity> getSubject(ArrayList<WordformsEntity> words, List<LemmasEntity> lemmasEntities) {
        ArrayList<LemmasEntity> result = new ArrayList<>();
        List<LemmasEntity> lemm = new ArrayList<>();
        lemm=getAllLemmas(words);
        for (int i = 0; i < lemm.size(); i++) {
            for (int j = 0; j < lemmasEntities.size(); j++) {
                if(lemm.get(i).equals(lemmasEntities.get(j))){
                    result.add(lemm.get(i));
                }
            }
        }
        return result;
    }

    private ArrayList<WordformsEntity> findingAllWordsWhicAreIm(Map<WordformsEntity,List<GrammarsEntity>> grammars) {
        ArrayList<WordformsEntity> wordformsEntities = new ArrayList<>();
        int k =0;

        List<WordformsEntity> keys = new ArrayList<WordformsEntity>(grammars.keySet());
        for(int i = 0; i < keys.size(); i++) {
            WordformsEntity key = keys.get(i);
            List<GrammarsEntity> gram = grammars.get(key);
            k=0;
            for (int j = 0; j < gram.size(); j++){
                if (gram.get(j).getIdGrammar().equals("nomn") ) {
                    wordformsEntities.add(key);
                }
            }
        }
        return wordformsEntities;
    }

    private ArrayList<WordformsEntity> findingAllWordsWhicAreVIctim(Map<WordformsEntity,List<GrammarsEntity>> grammars) {
        ArrayList<WordformsEntity> wordformsEntities = new ArrayList<>();
        int k =0;

        List<WordformsEntity> keys = new ArrayList<WordformsEntity>(grammars.keySet());
        for(int i = 0; i < keys.size(); i++) {
            WordformsEntity key = keys.get(i);
            List<GrammarsEntity> gram = grammars.get(key);
            k=0;
            for (int j = 0; j < gram.size(); j++){
                if (    gram.get(j).getIdGrammar().equals("accs") ||
                        gram.get(j).getIdGrammar().equals("datv") ||
                        gram.get(j).getIdGrammar().equals("loct") ||
                        gram.get(j).getIdGrammar().equals("gent") ||
                        gram.get(j).getIdGrammar().equals("ablt")) {
                    wordformsEntities.add(key);
                }
            }
        }
        return wordformsEntities;
    }


    private List<LemmasEntity> findingAllLemmasWhichAreNouns(Map<LemmasEntity,List<GrammarsEntity>> grammars) {
        List<LemmasEntity> lemmasEntities = new ArrayList<>();
        int k =0;

        List<LemmasEntity> keys = new ArrayList<LemmasEntity>(grammars.keySet());
        for(int i = 0; i < keys.size(); i++) {
            LemmasEntity key = keys.get(i);
            List<GrammarsEntity> gram = grammars.get(key);
            k=0;
            for (int j = 0; j < gram.size(); j++){
                if(gram.get(j).getIdGrammar().equals("NOUN") || gram.get(j).getIdGrammar().equals("anim")){
                    k++;
                }
            }
            if(k==2){
                lemmasEntities.add(key);
            }
        }
        return lemmasEntities;
    }

    private  Map<LemmasEntity,List<GrammarsEntity>> findingAllGrammarsLemmas(ArrayList<LemmasEntity> lemmas) {
        Map<LemmasEntity,List<GrammarsEntity>> listGrammars = new HashMap<>();
        for (int i = 0; i < lemmas.size(); i++) {
            List<GrammarsEntity> newGramList = new ArrayList<>();
            newGramList=(lemmas.get(i).getGrammars());
            listGrammars.put(lemmas.get(i),newGramList);
        }
        return listGrammars;
    }


    private Map<WordformsEntity,List<GrammarsEntity>> findingAllGrammarsWordFOrms(ArrayList<WordformsEntity> words) {
        Map<WordformsEntity,List<GrammarsEntity>> listGrammars = new HashMap<>();
        for (int i = 0; i < words.size(); i++) {
            List<GrammarsEntity> newGramList = new ArrayList<>();
            newGramList=(words.get(i).getGrammars());
            listGrammars.put(words.get(i),newGramList);
        }
        return listGrammars;
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
        ArrayList<ObjectsEntity> res = new ArrayList<ObjectsEntity>();
        Set<ObjectsEntity> set = new HashSet<ObjectsEntity>();
        for (int i = 0; i < lemmas.size(); i++) {
            for (int j = 0; j < lemmas.get(i).getObjects().size(); j++) {
                set.add(lemmas.get(i).getObjects().get(j));
            }
        }
        ObjectsEntity[] result = set.toArray(new ObjectsEntity[set.size()]);
        for (int i = 0; i < result.length; i++) {
            res.add(result[i]);
        }
        return res;
    }

    private static ArrayList<LemmasEntity> getAllLemmas(ArrayList<WordformsEntity> wordformsEntities) {
        Set<LemmasEntity> set = new HashSet<LemmasEntity>();
        ArrayList<LemmasEntity> res = new ArrayList<LemmasEntity>();
        for (int i = 0; i < wordformsEntities.size(); i++) {
            set.add(wordformsEntities.get(i).getLemma());
        }
        LemmasEntity[] result = set.toArray(new LemmasEntity[set.size()]);
        for (int i = 0; i < result.length; i++) {
            res.add(result[i]);
        }
        return res;
    }

    private ArrayList<WordformsEntity> getAllWordForms(ArrayList<String> words) {
        ArrayList<WordformsEntity> wordformsEntities = new ArrayList<>();
        for (int i = 0; i < words.size(); i++) {
            List<WordformsEntity> list = wordForms.FindByTitle(words.get(i));
            for (int j = 0; j < list.size(); j++) {
                wordformsEntities.add(list.get(j));
            }
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
