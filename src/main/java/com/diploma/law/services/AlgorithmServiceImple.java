package com.diploma.law.services;

import com.diploma.law.DAO.ArticleDAO;
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
        ArrayList<ObjectsEntity> objects = getAllObjects(lemmas);
        if (objects.isEmpty()) {
            bindingResult
                    .rejectValue("title", "error.title",
                            "*Не возможно квалифицировать данное преступление. Пожалуйса, переформулируйте его другими словами.");
        } else {
            List<ClarifyingFactsEntity> listclarifyingfacts = findingAllClarifyingFactsThatBelongToTheObject(objects);//нашли все доп слова, которые подходят под объект жизнь человека
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

  /*  private List<String> findSubject (LemmasEntity lemmaVerb,int verb, ArrayList<String[]>
        syntax, List < WordformsEntity > newNouns){
        List<String> res = new ArrayList<>();
        ArrayList<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < newNouns.size(); i++) {
            numbers.add(findVerb(syntax, newNouns.get(i)));
        }
        List<GrammarsEntity> grammarsVerb = lemmaVerb.getGrammars();
        GrammarsEntity passive = grammarsService.findById("pssv");
        ArrayList<String[]> sub = new ArrayList<>();
        ArrayList<String[]> others = new ArrayList<>();
        for (int i = 0; i < numbers.size(); i++) {
            if (syntax.get(numbers.get(i))[7].equals("предик")) {
                sub.add(syntax.get(numbers.get(i)));
            } else {
                    others.add(syntax.get(numbers.get(i)));
                }
            }
            for (int i = 0; i < grammarsVerb.size(); i++) {
                if (grammarsVerb.get(i).equals(passive)) {
                    res.add(others.get(0)[1]);
                    res.add(sub.get(0)[1]);
                }
            }
            if (res.isEmpty()) {
                res.add(sub.get(0)[1]);
                res.add(others.get(0)[1]);
            }
            return res;
        }*/

      /*  private ArrayList<Integer> findingMinInerval (ArrayList < Integer > nouns,int verb, ArrayList<String[]>syntax){
            ArrayList<Integer> numbers = new ArrayList<>();
            for (int i = 0; i < nouns.size(); i++) {
                String[] synt = syntax.get(nouns.get(i));
                String[] verbs = syntax.get(verb);
                int count = Integer.parseInt(synt[6]) - Integer.parseInt(verbs[6]);
                if (count < 0) {
                    count = count * -1;
                }
                numbers.add(count);
            }
            return numbers;
        }*/

       /* private List<WordformsEntity> findwordformsNouns
        (List < LemmasEntity > lemmas, List < WordformsEntity > wordformsNouns){
            List<WordformsEntity> wordforms = new ArrayList<>();
            for (int i = 0; i < lemmas.size(); i++) {
                wordforms.add(findWordFormWhichIsObject(lemmas.get(i), wordformsNouns));
            }
            return wordforms;
        }

        private int findVerb (ArrayList < String[]>syntax, WordformsEntity object){
            int verb = -1;
            for (int i = 0; i < syntax.size(); i++) {
                if (syntax.get(i)[1].equals(object.getTitle())) {
                    verb = i;
                }
            }
            return verb;
        }

        private LemmasEntity findLemmaWhichIsObject (List < ObjectsEntity > object, ArrayList < LemmasEntity > lemmas){
            LemmasEntity res = new LemmasEntity();
            List<LemmasEntity> lemmasObject = object.get(0).getLemmas();
            for (int i = 0; i < lemmas.size(); i++) {
                for (int j = 0; j < lemmasObject.size(); j++) {
                    if (lemmas.get(i).getTitle().equals(lemmasObject.get(j).getTitle())) {
                        res = lemmas.get(i);
                    }
                }
            }
            return res;
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



    private List<LemmasEntity> checkNouns (Map<LemmasEntity,List<GrammarsEntity>> gramm, String sentence,List<LemmasEntity> nouns){
        List<LemmasEntity> newLemma = new ArrayList<>();
        GrammarsEntity init = grammarsService.findById("Init");
        GrammarsEntity abbr = grammarsService.findById("Abbr");
        for (int i = 0; i < nouns.size(); i++) {
            List<GrammarsEntity> grammars = gramm.get(nouns.get(i));
            if(grammars.contains(init) || grammars.contains(abbr)){
                if(sentence.indexOf(nouns.get(i).getTitle()+".")>=0) {
                    newLemma.add(nouns.get(i));
                }
            }
            else{
                newLemma.add(nouns.get(i));
            }
        }
        return newLemma;
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


    private Map<WordformsEntity,List<GrammarsEntity>> findingAllGrammarsWordForms(ArrayList<WordformsEntity> words) {
        Map<WordformsEntity,List<GrammarsEntity>> listGrammars = new HashMap<>();
        for (int i = 0; i < words.size(); i++) {
            List<GrammarsEntity> newGramList = new ArrayList<>();
            newGramList=(words.get(i).getGrammars());
            listGrammars.put(words.get(i),newGramList);
        }
        return listGrammars;
    }

    private String[] findingAllSentencesObjects(String[] sentenses, List<ObjectsEntity> object){
        List<LemmasEntity> objectsLemma = object.get(0).getLemmas();
        String[] res= new String[1];
        int k=0;
        for (int i = 0; i < objectsLemma.size(); i++) {
            for (int j = 0; j < sentenses.length; j++) {
                if (sentenses[j].indexOf(objectsLemma.get(i).getTitle())>0){
                    res[k]=sentenses[j];
                    k++;
                }
            }
        }
        return res;
    }*/





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

    private List<String> textSegmentation(String text){
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
        List<String> res = new ArrayList<>();
        for (int i = 0; i < sentences.length; i++) {
            if(sentences[i]!=""){
                res.add(sentences[i]);
            }
        }
        return res;
    }
}
