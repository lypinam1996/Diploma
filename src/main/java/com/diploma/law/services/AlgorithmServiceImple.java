package com.diploma.law.services;

import com.diploma.law.DAO.ArticleDAO;
import com.diploma.law.DAO.LemmaDAO;
import com.diploma.law.controllers.ClassNameHere;
import com.diploma.law.models.*;
import com.github.oxaoo.mp4ru.exceptions.FailedParsingException;
import com.github.oxaoo.mp4ru.exceptions.InitRussianParserException;
import com.github.oxaoo.mp4ru.syntax.RussianParser;
import org.codehaus.groovy.runtime.ArrayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.apache.commons.lang.ArrayUtils;
import javax.transaction.Transactional;
import java.util.*;

@Service("AlgorithmService")
@Transactional
public class AlgorithmServiceImple implements AlgorithmService{

    @Autowired
    ArticleDAO article;

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

    @Override
    public List<ArticlesEntity> qualifyOffense(ProblemsEntity problem,BindingResult bindingResult) {
        String text = problem.getText();
        List<ArticlesEntity> articles = new ArrayList<>();
        List<String> sentences = textSegmentation(text);
        ArrayList<String> words = getWordsFromText(sentences);
        ArrayList<WordformsEntity> wordformsEntities = getAllWordForms(words);
        ArrayList<LemmasEntity> lemmas = getAllLemmas(wordformsEntities);
        ObjectsEntity object = getObject(lemmas);
        if (object == null) {
            bindingResult
                    .rejectValue("title", "error.title",
                            "*Не возможно квалифицировать данное преступление. Пожалуйса, переформулируйте его другими словами.");
        } else {
            List<ClarifyingFactsEntity> listclarifyingfacts = object.getClarifyingfacts();//нашли все доп слова, которые подходят под объект жизнь человека
            List<ClarifyingFactsEntity> cf = new ArrayList<ClarifyingFactsEntity>();

            if (findingAllClarifyingFactsThatOccurInTheText(lemmas, listclarifyingfacts).size() != 0)//проверяет есть ли леммы, которые подходят под под статьи
            {
                cf = findingAllClarifyingFactsThatOccurInTheText(lemmas, listclarifyingfacts);//находит эти леммы
                for (int i = 0; i < cf.size(); i++) {
                    String information = cf.get(i).getQuestion();
                    ClassNameHere class1 = new ClassNameHere();
                    boolean result = class1.infoBox(information, "Вопрос");
                    if (result) {
                        articles.add(cf.get(i).getCorpus().getArticle());
                    } else {
                        for (int g = 0; g < listclarifyingfacts.size(); g++) {
                            if (listclarifyingfacts.get(g).getLemma() == null)
                                articles.add(listclarifyingfacts.get(g).getCorpus().getArticle());
                        }
                    }
                }
            } else {
                for (int i = 0; i < listclarifyingfacts.size(); i++) {
                    if (listclarifyingfacts.get(i).getLemma() == null)
                        articles.add(listclarifyingfacts.get(i).getCorpus().getArticle());
                }
            }
        }
        return articles;
    }

    @Override
    public ArrayList<ArrayList<String>> getVictimAndSubject(String text, ArticlesEntity article) {
        ArrayList<ArrayList<String>> subject= new ArrayList<>();
    //  try {
          List<String> sentences = textSegmentation(text);
          ObjectsEntity object = article.getCorpus().getClarifyingfacts().get(0).getObject();
          String mainSentences = findingSentenceWithObject(sentences, object);
          //ArrayList<String[]> syntax = Syntax(mainSentences);
          List<String> list = new ArrayList<>();
          list.add(mainSentences);
          ArrayList<String> wordsSentence = getWordsFromText(list);
          ArrayList<List<WordformsEntity>> wordformsSentence=getAllWordFormsForNouns(wordsSentence);
          ArrayList<ArrayList<LemmasEntity>> lemmasSentence = getAllLemmasForNouns(wordformsSentence);
          Map<Integer,Map<LemmasEntity,List<GrammarsEntity>>> grammsAll = findingAllGrammarsLemmas(lemmasSentence);
          ArrayList<ArrayList<LemmasEntity>> lemmaNouns = findingAllLemmasWhichAreNouns(grammsAll);
        Map<Integer,Map<LemmasEntity,List<GrammarsEntity>>> grammLemm = findingAllGrammarsLemmas(lemmaNouns);
        ArrayList<ArrayList<LemmasEntity>> newNouns = checkNouns(grammLemm, lemmaNouns);
          Map<Integer,Map<LemmasEntity,List<GrammarsEntity>>> grammarsLemmasNouns = findingAllGrammarsLemmasWhichAreNouns(newNouns,wordsSentence);

          /*LemmasEntity lemmaObject = findLemmaWhichIsObject(object, lemmasSentence);
          WordformsEntity wordformsObject = findWordFormWhichIsObject(lemmaObject, wordformsSentence);
          int numberOfVerb = findNumberOfTheWord(syntax, wordformsObject);

          List<WordformsEntity> wordformsNouns = findwordformsNouns(newNouns, wordformsSentence);
          subject = findSubject(lemmaObject, numberOfVerb, syntax, wordformsNouns);*/
      //}
      //catch (FailedParsingException exc){

      //}
     // catch (InitRussianParserException exc){

      //}
        return subject;
    }

    private ArrayList<ArrayList<String>> findSubject (LemmasEntity lemmaVerb,int verb, ArrayList<String[]>
        syntax, List < WordformsEntity > newNouns){
        List<String> res = new ArrayList<>();
        ArrayList<Integer> numbers = new ArrayList<>();
        ArrayList<String[]> nouns = new ArrayList<>();
        for (int i = 0; i < newNouns.size(); i++) {
            numbers.add(findNumberOfTheWord(syntax, newNouns.get(i)));
        }
        for (int i = 0; i < numbers.size(); i++) {
            nouns.add(syntax.get(numbers.get(i)));
        }
        String[] verbSyntax = syntax.get(verb);
        if(verbSyntax[7].equals("ROOT") || verbSyntax[7].equals("сент-соч") || verbSyntax[7].equals("соч-союзн")) {
            res=formList(nouns,true);
        }
        if(verbSyntax[7].equals("пасс-анал")){
            res=formList(nouns,false);
        }
        ArrayList<ArrayList<String>> result = subjectVictims(res);

        result=getFirstForm(result);
        result=deleteSimilar(result);
        return result;
    }

    private ArrayList<ArrayList<String>> subjectVictims(List<String> list) {
        ArrayList<ArrayList<String>> res = new ArrayList<>();
        ArrayList<String> subjects = new ArrayList<>();
        ArrayList<String> victims = new ArrayList<>();
        if (list.contains("subject")) {
            int index = list.indexOf("subject");
            for (int i = index + 1; i < list.size(); i++) {
                subjects.add(list.get(i));
            }
            for (int i = 0; i < index; i++) {
                victims.add(list.get(i));
            }
        } else {
            int index = list.indexOf("victims");
            for (int i = index + 1; i < list.size(); i++) {
                victims.add(list.get(i));
            }
            for (int i = 0; i < index; i++) {
                subjects.add(list.get(i));
            }
        }
        res.add(subjects);
        res.add(victims);
        return res;
    }

    private ArrayList<ArrayList<String>> getFirstForm(ArrayList<ArrayList<String>> list){
        ArrayList<ArrayList<String>> res = new ArrayList<>();
        ArrayList<String> subjects = list.get(0);
        ArrayList<String> victims = list.get(1);
        subjects=findFirst(subjects, "subjects");
        victims=findFirst(victims,"victims");
        res.add(subjects);
        res.add(victims);
        return res;
    }

    private ArrayList<String> findFirst(ArrayList<String> subjects, String title){
        GrammarsEntity gramIm = grammarsService.findById("nomn");
        ArrayList<WordformsEntity> wordformsSubject = getAllWordForms(subjects);
        int x=0;
        subjects.clear();
        boolean ok = true;
        while(x<wordformsSubject.size() && ok  ){
            int y =0;
            List<GrammarsEntity> grammars = wordformsSubject.get(x).getGrammars();
            while (y<grammars.size() && ok  ){
                if (grammars.get(y).equals(gramIm)) {
                    if(title.equals("subjects")) {
                        subjects.add("Преступление совершил: "+wordformsSubject.get(x).getTitle());
                        ok = false;
                    }
                    else{
                        subjects.add("Пострадавшим является: "+wordformsSubject.get(x).getTitle());
                        ok = false;
                    }
                }
                else{
                    y++;
                }
            }
            x++;
        }
        if(ok) {
            for (int i = 0; i < wordformsSubject.size(); i++) {
                if(title.equals("subjects")) {
                    subjects.add("Преступления было совершено: "+wordformsSubject.get(i).getTitle());
                }
                else{
                    subjects.add("Противоправные действия, затронули: "+wordformsSubject.get(i).getTitle());
                }
            }
        }
        return subjects;
    }


    private ArrayList<ArrayList<String>> deleteSimilar(ArrayList<ArrayList<String>> list) {
       ArrayList<ArrayList<String>> res = new ArrayList<>();
       ArrayList<String> subjects = list.get(0);
       ArrayList<String> victims = list.get(1);
        for (int i = 0; i < subjects.size(); i++) {
            for (int j = i+1; j < subjects.size(); j++) {
                if (subjects.get(i).equals(subjects.get(j))){
                    subjects.remove(j);
                }
            }
        }
        int count =victims.size();
        for (int i = 0; i < count; i++) {
            for (int j = i+1; j < count; j++) {
                if (victims.get(i).equals(victims.get(j))){
                    victims.remove(j);
                    i--;
                    count--;
                }
            }
        }
        res.add(subjects);
        res.add(victims);
        return res;
    }


    private List<String> formList(ArrayList<String[]> nouns, boolean ok){
        List<String> res = new ArrayList<>();
        ArrayList<String[]> subject = getNoun(nouns, "предик");
        for (int i = 0; i < subject.size(); i++) {
            res.add(subject.get(i)[1]);
            nouns.remove(subject.get(i));
        }
        if(ok==true) {
            res.add("victims");
        }
        else {
            res.add("subject");
        }
        for (int i = 0; i < nouns.size(); i++) {
            res.add(nouns.get(i)[1]);
        }
        return res;
    }

    private ArrayList<String[]> getNoun(ArrayList<String[]> nouns,String title){
        ArrayList<String[]> res = new ArrayList<>();
        for (int i = 0; i < nouns.size(); i++) {
            if(nouns.get(i)[7].equals(title)){
                res.add(nouns.get(i));
            }
        }
        return res;
    }

    private List<WordformsEntity> findwordformsNouns
            (List < LemmasEntity > lemmas, List < WordformsEntity > wordformsNouns){
        List<WordformsEntity> wordforms = new ArrayList<>();
        for (int i = 0; i < lemmas.size(); i++) {
            wordforms.add(findWordFormWhichIsObject(lemmas.get(i), wordformsNouns));
        }
        return wordforms;
    }

    private int findNumberOfTheWord (ArrayList < String[]>syntax, WordformsEntity object){
        int verb = -1;
        for (int i = 0; i < syntax.size(); i++) {
            if (syntax.get(i)[1].equals(object.getTitle())) {
                verb = i;
            }
        }
        return verb;
    }

    private WordformsEntity findWordFormWhichIsObject (LemmasEntity
                                                               object, List < WordformsEntity > wordformsSentence){
        WordformsEntity res = new WordformsEntity();
        List<WordformsEntity> wordforms = object.getWordforms();
        for (int i = 0; i < wordforms.size(); i++) {
            for (int j = 0; j < wordformsSentence.size(); j++) {
                if (wordformsSentence.get(j).getTitle().equals(wordforms.get(i).getTitle())) {
                    res = wordforms.get(i);
                }
            }
        }
        return res;
    }

    private LemmasEntity findLemmaWhichIsObject (ObjectsEntity object, ArrayList < LemmasEntity > lemmas){
        LemmasEntity res = new LemmasEntity();
        List<LemmasEntity> lemmasObject = object.getLemmas();
        for (int i = 0; i < lemmas.size(); i++) {
            for (int j = 0; j < lemmasObject.size(); j++) {
                if (lemmas.get(i).getTitle().equals(lemmasObject.get(j).getTitle())) {
                    res = lemmas.get(i);
                }
            }
        }
        return res;
    }

    private ArrayList<ArrayList<LemmasEntity>> findingAllLemmasWhichAreNouns(Map<Integer,Map<LemmasEntity,List<GrammarsEntity>>> grammars) {
        ArrayList<ArrayList<LemmasEntity>> result = new ArrayList<>();
        Collection<Integer> collection = grammars.keySet();
        for (Integer key : collection) {
            Map<LemmasEntity, List<GrammarsEntity>> map = grammars.get(key);
            int k =0;
            ArrayList<LemmasEntity> res = new ArrayList<>();
            ArrayList<LemmasEntity> keys = new ArrayList<LemmasEntity>(map.keySet());
            for (int i = 0; i < keys.size(); i++) {
                LemmasEntity keyLemm = keys.get(i);
                List<GrammarsEntity> gram = map.get(keyLemm);
                k = 0;
                for (int j = 0; j < gram.size(); j++) {
                    if (gram.get(j).getIdGrammar().equals("NOUN") || gram.get(j).getIdGrammar().equals("anim")) {
                        k++;
                    }
                }
                if (k == 2) {
                    res.add(keyLemm);
                }
            }
            result.add(res);
        }
        return result;
    }

    private ArrayList<ArrayList<LemmasEntity>> checkNouns (Map<Integer,Map<LemmasEntity,List<GrammarsEntity>>> gramm, ArrayList<ArrayList<LemmasEntity>> lemm){
        GrammarsEntity init = grammarsService.findById("Init");
        GrammarsEntity abbr = grammarsService.findById("Abbr");
        ArrayList<ArrayList<LemmasEntity>> newLemm = new ArrayList<>();
        for(int i=0;i<lemm.size();i++){
            ArrayList<LemmasEntity> res = new ArrayList<>();
            for(int j=0;j<lemm.get(i).size();j++) {
                Set<Integer> intg = gramm.keySet();
                for (Integer intgs : intg) {
                    Map<LemmasEntity, List<GrammarsEntity>> map = gramm.get(intgs);
                    if (!map.isEmpty()) {
                        Set<LemmasEntity> lemmasEntities = map.keySet();
                        if (lemmasEntities.contains(lemm.get(i).get(j))) {
                            for (LemmasEntity lemms : lemmasEntities) {
                                List<GrammarsEntity> grammars = map.get(lemms);
                                if (!grammars.contains(init) && !grammars.contains(abbr)) {
                                    res.add(lemm.get(i).get(j));
                                }
                            }
                        }
                    }
                }
            }
            newLemm.add(res);
        }
        return newLemm;
    }

    private  Map<Integer,Map<LemmasEntity,List<GrammarsEntity>>> findingAllGrammarsLemmasWhichAreNouns(ArrayList<ArrayList<LemmasEntity>> lemmas, ArrayList<String> words) {
        Map<Integer, Map<LemmasEntity, List<GrammarsEntity>>> listGrammars = new HashMap<>();
        GrammarsEntity gramName = grammarsService.findById("Name");
        GrammarsEntity gramSurn = grammarsService.findById("Surn");
        GrammarsEntity gramPatr = grammarsService.findById("Patr");
        for (int x = 0; x < lemmas.size(); x++) {
            ArrayList<LemmasEntity> lemma = lemmas.get(x);
            int i = 0;
            boolean lemmBool = true;
            while (lemmBool && (i < lemma.size())) {
                List<GrammarsEntity> newGramList = lemma.get(i).getGrammars();
                int j = 0;;
                while (lemmBool && j < newGramList.size()) {
                    if (newGramList.get(j).equals(gramName) ||
                            newGramList.get(j).equals(gramSurn) ||
                            newGramList.get(j).equals(gramPatr)) {
                        if (newGramList.get(j).equals(gramName)) {
                            ClassNameHere class1 = new ClassNameHere();
                            boolean result = class1.infoBox(words.get(x) + " - имя человека, описание которого присутствует в данном тексте?", "Вопрос");
                            if (result) {
                                Map<Integer, Map<LemmasEntity, List<GrammarsEntity>>> res = help(newGramList,x,words,i,lemma);
                                if (res.size() != 0) {
                                    listGrammars.putAll(res);
                                    lemmBool = false;
                                }
                                else{
                                    j++;
                                }
                            }
                            else{
                                j++;
                            }
                        } else {
                            if (newGramList.get(j).equals(gramSurn)) {
                                ClassNameHere class1 = new ClassNameHere();
                                boolean result = class1.infoBox(words.get(x) + " - фамилия человека, описание которого присутствует в данном тексте?", "Вопрос");
                                if (result) {
                                    Map<Integer, Map<LemmasEntity, List<GrammarsEntity>>> res = help(newGramList,x,words,i,lemma);
                                    if(res.size()!=0){
                                        listGrammars.putAll(res);
                                        lemmBool = false;
                                    }
                                    else{
                                        j++;
                                    }
                                }
                                else{
                                    j++;
                                }
                            } else {
                                if (newGramList.get(j).equals(gramPatr)) {
                                    ClassNameHere class1 = new ClassNameHere();
                                    boolean result = class1.infoBox(words.get(x) + " - отчество человека, описание которого присутствует в данном тексте?", "Вопрос");
                                    if (result) {
                                        Map<Integer, Map<LemmasEntity, List<GrammarsEntity>>> res = help(newGramList,x,words,i,lemma);
                                        if(res.size()!=0){
                                            listGrammars.putAll(res);
                                            lemmBool = false;
                                        }
                                        else{
                                            j++;
                                        }
                                    }
                                    else{
                                        j++;
                                    }
                                }
                            }
                        }
                    } else {
                        j++;
                    }
                }
                if (j == newGramList.size()) {
                    Map<LemmasEntity, List<GrammarsEntity>> map = new HashMap<>();
                    map.put(lemma.get(i), newGramList);
                    listGrammars.put(x, map);
                }
                i++;
            }
        }
        return listGrammars;
    }

    private Map<Integer, Map<LemmasEntity, List<GrammarsEntity>>>  help(List<GrammarsEntity> newGramList,
                                                                        int x,ArrayList<String> words,
                                                                        int i, ArrayList<LemmasEntity> lemma){
        Map<Integer, Map<LemmasEntity, List<GrammarsEntity>>> listGrammars = new HashMap<>();
        GrammarsEntity gramFemale = grammarsService.findById("femn");
        GrammarsEntity gramMale = grammarsService.findById("masc");
        int y =0;
        boolean sexBool=true;
        while (sexBool && y < newGramList.size()) {
            if(newGramList.get(y).equals(gramFemale) || newGramList.get(y).equals(gramMale)){
                if(newGramList.get(y).equals(gramFemale)) {
                    ClassNameHere class2 = new ClassNameHere();
                    boolean res = class2.infoBox(words.get(x) + " - женского рода?", "Вопрос");
                    if (res) {
                        Map<LemmasEntity,List<GrammarsEntity>> map= new HashMap<>();
                        map.put(lemma.get(i), newGramList);
                        listGrammars.put(x,map);
                        sexBool=false;
                    }
                    else {
                        sexBool = false;
                    }
                }
                else {
                    if (newGramList.get(y).equals(gramMale)) {
                        ClassNameHere class2 = new ClassNameHere();
                        boolean res = class2.infoBox(words.get(x) + " - мужского рода?", "Вопрос");
                        if (res) {
                            Map<LemmasEntity, List<GrammarsEntity>> map = new HashMap<>();
                            map.put(lemma.get(i), newGramList);
                            listGrammars.put(x, map);
                            sexBool = false;
                        }
                        else {
                            sexBool = false;
                        }
                    }
                }
            }
            else {
                y++;
            }
        }
        return listGrammars;
    }

    private  Map<Integer,Map<LemmasEntity,List<GrammarsEntity>>> findingAllGrammarsLemmas(ArrayList<ArrayList<LemmasEntity>> lemmas) {
        Map<Integer,Map<LemmasEntity,List<GrammarsEntity>>> result = new HashMap<>();
        for (int i = 0; i < lemmas.size(); i++) {
            List<LemmasEntity> lemma = lemmas.get(i);
            Map<LemmasEntity,List<GrammarsEntity>> gramm = new HashMap<>();
            for (int j = 0; j < lemma.size(); j++) {
                gramm.put(lemma.get(j),lemma.get(j).getGrammars());
            }
            result.put(i,gramm);
        }
        return result;
    }

    private  ArrayList<String[]> Syntax(String sentence) throws InitRussianParserException, FailedParsingException {
        RussianParser parser = new RussianParser("/home/maria/IdeaProjects/diploma/src/main/java/com/diploma/law/res/models/russian-utf8.par","/home/maria/IdeaProjects/diploma/src/main/java/com/diploma/law/res/models","/home/maria/IdeaProjects/diploma/src/main/java/com/diploma/law/res/models/russian.mco");
        List<String> list = new ArrayList<>();
        list=parser.parse(sentence);
        ArrayList<String[]> newlist = new ArrayList<>();
        for(int i=0;i<=list.size()-1;i++){
            list.get(i).trim();
            String[] atr = list.get(i).split("\t");
            newlist.add(atr);
        }
        return newlist;
    }

    private String findingSentenceWithObject(List<String> sentences, ObjectsEntity object){
        List<LemmasEntity> objectsLemma = object.getLemmas();
        ArrayList<String> res= new ArrayList<>();
        for (int i = 0; i < objectsLemma.size(); i++) {
            for (int j = 0; j < sentences.size(); j++) {
                if (sentences.get(j).indexOf(objectsLemma.get(i).getTitle())>0){
                    res.add(sentences.get(j));
                }
            }
        }
        return res.get(0);
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
    private ObjectsEntity getObject(ArrayList<LemmasEntity> lemmas) {
        Set<ObjectsEntity> set = new HashSet<ObjectsEntity>();
        for (int i = 0; i < lemmas.size(); i++) {
            for (int j = 0; j < lemmas.get(i).getObjects().size(); j++) {
                set.add(lemmas.get(i).getObjects().get(j));
            }
        }
        ObjectsEntity[] result = set.toArray(new ObjectsEntity[set.size()]);
        ObjectsEntity res = new ObjectsEntity();
        if(result.length!=0) {
             res= result[0];
        }
        else {
            res=null;
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

    private static ArrayList<ArrayList<LemmasEntity>> getAllLemmasForNouns(ArrayList<List<WordformsEntity>> wordformsEntities) {
        ArrayList<ArrayList<LemmasEntity>> res = new ArrayList<>();
        for (int i = 0; i < wordformsEntities.size(); i++) {
            Set<LemmasEntity> set = new HashSet<>();
            for (int j = 0; j < wordformsEntities.get(i).size(); j++) {
                set.add(wordformsEntities.get(i).get(j).getLemma());
            }
            LemmasEntity[] result = set.toArray(new LemmasEntity[set.size()]);
            ArrayList<LemmasEntity> lem = new ArrayList<LemmasEntity>();
            for (int j = 0; j < result.length; j++) {
                lem.add(result[j]);
            }
            res.add(lem);
        }

        return res;
    }

    private ArrayList<List<WordformsEntity>> getAllWordFormsForNouns(ArrayList<String> words) {
        ArrayList<List<WordformsEntity>> wordformsEntities = new ArrayList<>();
        for (int i = 0; i < words.size(); i++) {
            List<WordformsEntity> list = wordForms.FindByTitle(words.get(i));
            wordformsEntities.add(list);
        }
        return wordformsEntities;
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

    private ArrayList<String> getWordsFromText(List<String> sentences) {
        ArrayList<String> words = new ArrayList<String>();
        for (int i = 0; i < sentences.size(); i++) {
            String sentence = sentences.remove(i);
            sentence=sentence.replaceAll("[^A-Za-zА-Яа-я0-9,Ё,ё]", " ");
            sentence=sentence.replaceAll(",", " ");
            sentence=sentence.replaceAll(":", " ");
            sentence=sentence.replaceAll(";", " ");
            sentence=sentence.replaceAll("  ", " ");
            sentences.add(i,sentence);
            String[] word = sentences.get(i).split(" ");
            for (int j = 0; j < word.length; j++) {
                words.add(word[j].trim());
            }
        }
        return words;
    }

    private ArrayList<String> textSegmentation(String text){
        text=text.toLowerCase(new Locale("ru"));
        String[] sentences = text.split("\\.");
        for (int i = 0; i < sentences.length; i++) {
            sentences[i]=sentences[i].trim();
            if(sentences[i].length()<2 && sentences[i].length()>0){
                sentences[i]=sentences[i]+"."+sentences[i+1];
                sentences[i+1]=sentences[i+1].trim();
                if(i+1<sentences.length && sentences[i+1].length()<2 && sentences[i+1].length()>0){
                    sentences[i]=sentences[i]+"."+sentences[i+2];
                    for (int j=i+1; j<sentences.length-2;j++) {
                        sentences[j] = sentences[j + 2];
                    }
                    sentences[sentences.length-2]="";
                    sentences[sentences.length-1]="";
                }
                else{
                    for (int j=i+1; j<sentences.length-1;j++){
                        sentences[j]=sentences[j+1];
                    }
                    sentences[sentences.length-1]="";
                }
            }
        }
        ArrayList<String> res = new ArrayList<>();
        for (int i = 0; i < sentences.length; i++) {
            if(sentences[i]!=""){
                res.add(sentences[i]);
            }
        }
        return res;
    }
}
