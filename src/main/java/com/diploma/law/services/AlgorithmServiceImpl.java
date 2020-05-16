package com.diploma.law.services;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;

import com.diploma.law.dto.MainDto;
import com.diploma.law.models.ArticlesEntity;
import com.diploma.law.models.ClarifyingFactsEntity;
import com.diploma.law.models.ProblemsEntity;
import com.diploma.law.models.UsersEntity;
import com.github.oxaoo.mp4ru.exceptions.FailedParsingException;
import com.github.oxaoo.mp4ru.exceptions.InitRussianParserException;
import com.github.oxaoo.mp4ru.syntax.RussianParser;

@Service("AlgorithmService")
@Transactional
public class AlgorithmServiceImpl implements AlgorithmService
{
    @Autowired
    private WordFormsService wordFormsService;

    @Autowired
    private ParseService     parseService;

    @Autowired
    UserService              userService;

    @Autowired
    TaskService              taskService;

    //            if(!articles.isEmpty()) {
    //    ArrayList<ArrayList<String>> subject = algorithm.getVictimAndSubject(problem.getText(),articles.get(0));
    //    List<String> subjects= subject.get(0);
    //    List<String> victims= subject.get(1);
    //    subjects=question(subjects," субъект");
    //    victims=question(victims," потерпевший");
    //    if(subjects.isEmpty()){
    //        subjects.add("Не определён");
    //    }
    //    if(victims.isEmpty()){
    //        victims.add("Не определён");
    //    }
    //    model.addAttribute("subjects", subjects);
    //    model.addAttribute("victims", victims);
    //    problem.setSubject(subjects.get(0));
    //    problem.setVictims(victims.get(0));

    @Override
    public Model qualifyOffense(ProblemsEntity problem, Model result, BindingResult bindingResult)
    {
        if (problem != null)
        {
            MainDto mainDto = parseService.parseText(problem.getText());
            List<ArticlesEntity> articles = getObject(bindingResult, mainDto);
            UsersEntity user = userService.getUser();
            getVictimAndSubject(mainDto);

            problem.setArticle(articles);
            problem.setUsersByUser(user);
            problem.setText(problem.getText());

            result.addAttribute("articles", articles.get(0));

            taskService.saveTask(problem);
        }
        return result;
    }

    @Override
    public List<ArticlesEntity> getObject(BindingResult bindingResult, MainDto mainDto)
    {
        List<ArticlesEntity> articles = new ArrayList<>();
        if (mainDto.getObject() == null)
        {
            bindingResult.rejectValue("title", "error.title",
                    "*Невозможно квалифицировать данное преступление. Пожалуйса, переформулируйте его другими словами.");
        }
        else
        {
            List<ClarifyingFactsEntity> clarifyingFacts = mainDto.getClarifyingFacts();
            if (!CollectionUtils.isEmpty(clarifyingFacts))
            {
                for (ClarifyingFactsEntity factsEntity : clarifyingFacts)
                {
                    articles.add(factsEntity.getCorpus().getArticle());
                }
            }
            else
            {
                List<ClarifyingFactsEntity> clarifyingFactsObject = mainDto.getObject().getClarifyingfacts();
                for (ClarifyingFactsEntity clarifyingFact : clarifyingFactsObject)
                {
                    if (clarifyingFact.getLemma() == null) articles.add(clarifyingFact.getCorpus().getArticle());
                }
            }
        }
        return articles;
    }

    @Override
    public ArrayList<ArrayList<String>> getVictimAndSubject(MainDto mainDto)
    {
        ArrayList<ArrayList<String>> subject = new ArrayList<>();
        ArrayList<String[]> syntax = syntax(mainDto.getMainSentence());
        //            List<String> list = new ArrayList<>();
        //            list.add(mainSentences);
        //            ArrayList<String> wordsSentence = getWordsFromText(list);
        //            ArrayList<List<WordformsEntity>> wordformsSentence = getAllWordFormsForNouns(wordsSentence);
        //            ArrayList<ArrayList<LemmasEntity>> lemmasSentence = getAllLemmasForNouns(wordformsSentence);
        //            Map<Integer, Map<LemmasEntity, List<GrammarsEntity>>> grammsAll = findingAllGrammarsLemmas(lemmasSentence);
        //            ArrayList<ArrayList<LemmasEntity>> lemmaNouns = findingAllLemmasWhichAreNouns(grammsAll);
        //            Map<Integer, Map<LemmasEntity, List<GrammarsEntity>>> grammLemm = findingAllGrammarsLemmas(lemmaNouns);
        //            ArrayList<ArrayList<LemmasEntity>> newNouns = checkNouns(grammLemm, lemmaNouns);
        //            Map<Integer, Map<LemmasEntity, List<GrammarsEntity>>> grammarsLemmasNouns1 = findingAllGrammarsLemmasWhichAreNouns(newNouns,
        //                    wordsSentence);
        //            Map<Integer, Map<LemmasEntity, List<GrammarsEntity>>> grammarsLemmasNouns = delete(grammarsLemmasNouns1);
        //            int numberOfVerb = findLemmaWhichIsObject(object, lemmasSentence);
        //            subject = findSubject(numberOfVerb, syntax, grammarsLemmasNouns);

        return subject;
    }

    private ArrayList<String[]> syntax(String sentence)
    {
        ArrayList<String[]> result = new ArrayList<>();
        try
        {
            RussianParser parser = new RussianParser(
                    "/home/maria/diploma/src/main/java/com/diploma/law/res/models/russian-utf8.par",
                    "/home/maria/diploma/src/main/java/com/diploma/law/res/models",
                    "/home/maria/diploma/src/main/java/com/diploma/law/res/models/russian.mco");
            List<String> list = parser.parse(sentence);
            for (int i = 0; i <= list.size() - 1; i++)
            {
                list.get(i).trim();
                String[] atr = list.get(i).split("\t");
                result.add(atr);
            }
        }
        catch (InitRussianParserException | FailedParsingException exc)
        {
            exc.printStackTrace();
        }
        return result;
    }

    //    private ArrayList<ArrayList<String>> findSubject (int numberOfVerb, ArrayList<String[]> syntax,
    //                                                      Map<Integer, Map<LemmasEntity, List<GrammarsEntity>>> grammarsLemmasNouns){
    //        List<Integer> res = new ArrayList<>();
    //        String[] verbSyntax = syntax.get(numberOfVerb);
    //        ArrayList<String[]> nouns = getNouns(grammarsLemmasNouns,syntax);
    //        if(verbSyntax[7].equals("ROOT") || verbSyntax[7].equals("сент-соч") || verbSyntax[7].equals("соч-союзн") || verbSyntax[7].equals("подч-союзн")) {
    //            res=formList(nouns,true,grammarsLemmasNouns);
    //        }
    //        if(verbSyntax[7].equals("пасс-анал")){
    //            res=formList(nouns,false,grammarsLemmasNouns);
    //        }
    //        ArrayList<ArrayList<String>> result = subjectVictims(res,grammarsLemmasNouns);
    //        return result;
    //    }

    //    private ArrayList<ArrayList<String>> subjectVictims(List<Integer> list, Map<Integer, Map<LemmasEntity, List<GrammarsEntity>>> grammarsLemmasNouns) {
    //        ArrayList<ArrayList<String>> res = new ArrayList<>();
    //        ArrayList<String> subjects = new ArrayList<>();
    //        ArrayList<String> victims = new ArrayList<>();
    //        if (list.contains(-1)) {
    //            subjects=getSubj(list,grammarsLemmasNouns,-1);
    //            victims=getVict(list,grammarsLemmasNouns,-1);
    //        } else {
    //            victims = getSubj(list, grammarsLemmasNouns, -2);
    //            subjects = getVict(list, grammarsLemmasNouns, -2);
    //        }
    //        res.add(subjects);
    //        res.add(victims);
    //        return res;
    //    }

    //    private ArrayList<String> getSubj(List<Integer> list,
    //                                      Map<Integer,Map<LemmasEntity, List<GrammarsEntity>>> grammarsLemmasNouns,
    //                                      int number) {
    //        ArrayList<String> subjects = new ArrayList<>();
    //        int index = list.indexOf(number);
    //        for (int i = 0; i < index; i++) {
    //            subjects=helpSubjVict(list,grammarsLemmasNouns,subjects,i);
    //        }
    //        return subjects;
    //    }

    //    private ArrayList<String> helpSubjVict(List<Integer> list,
    //                                           Map<Integer,Map<LemmasEntity, List<GrammarsEntity>>> grammarsLemmasNouns,
    //                                           ArrayList<String> subjects,int i){
    //        if (list.get(i) == -11) {
    //            int t = subjects.indexOf(subjects.get(subjects.size()-1));
    //            String title2 = subjects.remove(t);
    //            title2=title2.substring(0, 1).toUpperCase() + title2.substring(1);
    //            title2 =   subjects.remove(t-1)+ " "+title2;
    //            title2=title2.substring(0, 1).toUpperCase() + title2.substring(1);
    //            subjects.add(title2);
    //        } else {
    //            if(list.get(i)==-22) {
    //                int t = subjects.indexOf(subjects.get(subjects.size()-1));
    //                String title3 = subjects.remove(t);
    //                title3=title3.substring(0, 1).toUpperCase() + title3.substring(1);
    //
    //                title3 =  subjects.remove(t-1)+ " "+title3;
    //                title3=title3.substring(0, 1).toUpperCase() + title3.substring(1);
    //
    //                title3= subjects.remove(t-2)+ " "+title3;
    //                title3=title3.substring(0, 1).toUpperCase() + title3.substring(1);
    //                subjects.add(title3);
    //            }
    //            Collection<Integer> numbers = grammarsLemmasNouns.keySet();
    //            for (Integer key : numbers) {
    //                if (list.get(i).equals(key)) {
    //                    Map<LemmasEntity, List<GrammarsEntity>> map = grammarsLemmasNouns.get(key);
    //                    Collection<LemmasEntity> title = map.keySet();
    //                    for (LemmasEntity key2 : title) {
    //                        subjects.add(key2.getTitle());
    //                    }
    //                }
    //            }
    //        }
    //        return subjects;
    //    }

    //    private ArrayList<String> getVict(List<Integer> list,
    //                                      Map<Integer,Map<LemmasEntity, List<GrammarsEntity>>> grammarsLemmasNouns,int number) {
    //        ArrayList<String> victims = new ArrayList<>();
    //        int index = list.indexOf(number);
    //        for (int i = index + 1; i < list.size(); i++) {
    //            victims=helpSubjVict(list,grammarsLemmasNouns,victims,i);
    //        }
    //        return victims;
    //    }

    //    private List<Integer> formList( ArrayList<String[]> nouns, boolean ok,
    //                                    Map<Integer,Map<LemmasEntity, List<GrammarsEntity>>> grammarsLemmasNouns){
    //        List<Integer> res = new ArrayList<>();
    //        ArrayList<String[]> pred = getPred(nouns);
    //        for (int i = 0; i < pred.size(); i++) {
    //            int predInt=Integer.parseInt(pred.get(i)[0]);
    //            res.add(predInt);
    //            List<Integer> help= helpFormList(nouns,predInt,grammarsLemmasNouns);
    //            res.addAll(help);
    //            for (int j = 0; j < res.size(); j++) {
    //                for (int x = 0; x < nouns.size(); x++) {
    //                    if(!nouns.isEmpty() && res.get(i)>0) {
    //                        String[] synt = nouns.get(x);
    //                        if (Integer.parseInt(synt[0])-1 == res.get(j)) {
    //                            nouns.remove(synt);
    //                        }
    //                    }
    //                }
    //            }
    //        }
    //        if(ok==true) {
    //            res.add(-1);
    //        }
    //        else {
    //            res.add(-2);
    //        }
    //        if(!nouns.isEmpty()) {
    //            int predInt2 = -1;
    //            if (nouns.size() > 2) {
    //                predInt2 = Integer.parseInt(nouns.get(0)[0]);
    //                res.add(predInt2);
    //                res.addAll(helpFormList(nouns, predInt2, grammarsLemmasNouns));
    //            } else {
    //                predInt2 = Integer.parseInt(nouns.get(0)[0]);
    //                res.add(predInt2);
    //            }
    //        }
    //        return res;
    //    }

    //    private List<Integer> helpFormList(ArrayList<String[]> nouns, int predInt,
    //                                       Map<Integer,Map<LemmasEntity, List<GrammarsEntity>>> grammarsLemmasNouns){
    //        List<Integer> res = new ArrayList<>();
    //        GrammarsEntity gramName = grammarsService.findById("Name");
    //        GrammarsEntity gramSurn = grammarsService.findById("Surn");
    //        GrammarsEntity gramPatr = grammarsService.findById("Patr");
    //        Collection<Integer> numbers = grammarsLemmasNouns.keySet();
    //        for (Integer key : numbers) {
    //            if(key==predInt-1 || key==predInt+1){
    //                Map<LemmasEntity, List<GrammarsEntity>> map = grammarsLemmasNouns.get(key);
    //                Collection<LemmasEntity> title = map.keySet();
    //                for (LemmasEntity key2 : title) {
    //                    List<GrammarsEntity>  gramms = key2.getGrammars();
    //                    if(gramms.contains(gramName) || gramms.contains(gramSurn) || gramms.contains(gramPatr)){
    //                        res.add(key);
    //
    //                    }
    //                }
    //            }
    //        }
    //        if(res.size()==1){
    //            res.add(-11);
    //        }
    //        else {
    //            if (res.size() == 2) {
    //                res.add(-22);
    //            }
    //        }
    //        return res;
    //    }

    //    private ArrayList<String[]> getPred (ArrayList<String[]> nouns){
    //        ArrayList<String[]> subject = new ArrayList<>();
    //        for (int i = 0; i < nouns.size(); i++) {
    //            if(nouns.get(i)[7].equals("предик")){
    //                subject.add(nouns.get(i));
    //            }
    //        }
    //        return subject;
    //    }

    //    private ArrayList<String[]> getNouns(Map<Integer,Map<LemmasEntity, List<GrammarsEntity>>> grammarsLemmasNouns,
    //                                         ArrayList<String[]> syntax){
    //        Set<Integer> numbers = grammarsLemmasNouns.keySet();
    //        Integer[] numb=numbers.toArray(new Integer[numbers.size()]);
    //        ArrayList<String[]> res = new ArrayList<>();
    //        for (int i = 0; i < numb.length; i++) {
    //            res.add(syntax.get(numb[i]));
    //        }
    //        return res;
    //    }

    //    private int findLemmaWhichIsObject (ObjectsEntity object, ArrayList<ArrayList<LemmasEntity>> lemmasList){
    //        int res= -1;
    //        List<LemmasEntity> lemmasObject = object.getLemmas();
    //        for (int x = 0; x < lemmasList.size(); x++) {
    //            ArrayList<LemmasEntity> lemmas=lemmasList.get(x);
    //            for (int i = 0; i < lemmas.size(); i++) {
    //                for (int j = 0; j < lemmasObject.size(); j++) {
    //                    if (lemmas.get(i).getTitle().equals(lemmasObject.get(j).getTitle())) {
    //                        res = x;
    //                    }
    //                }
    //            }
    //        }
    //        return res;
    //    }

    //    private ArrayList<ArrayList<LemmasEntity>> findingAllLemmasWhichAreNouns(Map<Integer,Map<LemmasEntity,List<GrammarsEntity>>> grammars) {
    //        ArrayList<ArrayList<LemmasEntity>> result = new ArrayList<>();
    //        Collection<Integer> collection = grammars.keySet();
    //        for (Integer key : collection) {
    //            Map<LemmasEntity, List<GrammarsEntity>> map = grammars.get(key);
    //            int k =0;
    //            ArrayList<LemmasEntity> res = new ArrayList<>();
    //            ArrayList<LemmasEntity> keys = new ArrayList<LemmasEntity>(map.keySet());
    //            for (int i = 0; i < keys.size(); i++) {
    //                LemmasEntity keyLemm = keys.get(i);
    //                List<GrammarsEntity> gram = map.get(keyLemm);
    //                k = 0;
    //                int count=0;
    //                for (int j = 0; j < gram.size(); j++) {
    //                    if (gram.get(j).getIdGrammar().equals("NOUN") || gram.get(j).getIdGrammar().equals("anim")) {
    //                        k++;
    //                    }
    //                    else{
    //                        if(gram.get(j).getIdGrammar().equals("Anum") || gram.get(j).getIdGrammar().equals("Apro")
    //                                || gram.get(j).getIdGrammar().equals("NPRO") || gram.get(j).getIdGrammar().equals("NUMR")){
    //                            count++;
    //                        }
    //                    }
    //                }
    //                if (k == 2 || count>0) {
    //                    res.add(keyLemm);
    //                }
    //            }
    //            result.add(res);
    //        }
    //        return result;
    //    }

    //    private ArrayList<ArrayList<LemmasEntity>> checkNouns (Map<Integer,Map<LemmasEntity,List<GrammarsEntity>>> gramm, ArrayList<ArrayList<LemmasEntity>> lemm){
    //        GrammarsEntity init = grammarsService.findById("Init");
    //        GrammarsEntity abbr = grammarsService.findById("Abbr");
    //        ArrayList<ArrayList<LemmasEntity>> newLemm = new ArrayList<>();
    //        for(int i=0;i<lemm.size();i++){
    //            ArrayList<LemmasEntity> res = new ArrayList<>();
    //            for(int j=0;j<lemm.get(i).size();j++) {
    //                Set<Integer> intg = gramm.keySet();
    //                for (Integer intgs : intg) {
    //                    Map<LemmasEntity, List<GrammarsEntity>> map = gramm.get(intgs);
    //                    if (!map.isEmpty()) {
    //                        Set<LemmasEntity> lemmasEntities = map.keySet();
    //                        if (lemmasEntities.contains(lemm.get(i).get(j))) {
    //                            for (LemmasEntity lemms : lemmasEntities) {
    //                                List<GrammarsEntity> grammars = map.get(lemms);
    //                                if (!grammars.contains(init) && !grammars.contains(abbr)) {
    //                                    res.add(lemm.get(i).get(j));
    //                                }
    //                            }
    //                        }
    //                    }
    //                }
    //            }
    //            newLemm.add(res);
    //        }
    //        return newLemm;
    //    }

    //    private  Map<Integer,Map<LemmasEntity,List<GrammarsEntity>>> findingAllGrammarsLemmasWhichAreNouns(ArrayList<ArrayList<LemmasEntity>> lemmas, ArrayList<String> words) {
    //        Map<Integer, Map<LemmasEntity, List<GrammarsEntity>>> listGrammars = new HashMap<>();
    //        GrammarsEntity gramName = grammarsService.findById("Name");
    //        GrammarsEntity gramSurn = grammarsService.findById("Surn");
    //        GrammarsEntity gramPatr = grammarsService.findById("Patr");
    //        for (int x = 0; x < lemmas.size(); x++) {
    //            ArrayList<LemmasEntity> lemma = lemmas.get(x);
    //            int i = 0;
    //            boolean lemmBool = true;
    //            while (lemmBool && (i < lemma.size())) {
    //                List<GrammarsEntity> newGramList = lemma.get(i).getGrammars();
    //                int j = 0;;
    //                while (lemmBool && j < newGramList.size()) {
    //                    if (newGramList.get(j).equals(gramName) ||
    //                            newGramList.get(j).equals(gramSurn) ||
    //                            newGramList.get(j).equals(gramPatr)) {
    //                        if (newGramList.get(j).equals(gramName)) {
    //                            ClassNameHere class1 = new ClassNameHere();
    //                            boolean result = class1.infoBox(words.get(x) + " - имя человека, описание которого присутствует в данном тексте?", "Вопрос");
    //                            if (result) {
    //                                Map<Integer, Map<LemmasEntity, List<GrammarsEntity>>> res = help(newGramList,x,words,i,lemma);
    //                                if (res.size() != 0) {
    //                                    listGrammars.putAll(res);
    //                                    lemmBool = false;
    //                                }
    //                                else{
    //                                    j++;
    //                                }
    //                            }
    //                            else{
    //                                j++;
    //                            }
    //                        } else {
    //                            if (newGramList.get(j).equals(gramSurn)) {
    //                                ClassNameHere class1 = new ClassNameHere();
    //                                boolean result = class1.infoBox(words.get(x) + " - фамилия человека, описание которого присутствует в данном тексте?", "Вопрос");
    //                                if (result) {
    //                                    Map<Integer, Map<LemmasEntity, List<GrammarsEntity>>> res = help(newGramList,x,words,i,lemma);
    //                                    if(res.size()!=0){
    //                                        listGrammars.putAll(res);
    //                                        lemmBool = false;
    //                                    }
    //                                    else{
    //                                        j++;
    //                                    }
    //                                }
    //                                else{
    //                                    j++;
    //                                }
    //                            } else {
    //                                if (newGramList.get(j).equals(gramPatr)) {
    //                                    ClassNameHere class1 = new ClassNameHere();
    //                                    boolean result = class1.infoBox(words.get(x) + " - отчество человека, описание которого присутствует в данном тексте?", "Вопрос");
    //                                    if (result) {
    //                                        Map<Integer, Map<LemmasEntity, List<GrammarsEntity>>> res = help(newGramList,x,words,i,lemma);
    //                                        if(res.size()!=0){
    //                                            listGrammars.putAll(res);
    //                                            lemmBool = false;
    //                                        }
    //                                        else{
    //                                            j++;
    //                                        }
    //                                    }
    //                                    else{
    //                                        j++;
    //                                    }
    //                                }
    //                            }
    //                        }
    //                    } else {
    //                        j++;
    //                    }
    //                }
    //                if (j == newGramList.size()) {
    //                    Map<LemmasEntity, List<GrammarsEntity>> map = new HashMap<>();
    //                    map.put(lemma.get(i), newGramList);
    //                    listGrammars.put(x, map);
    //                }
    //                i++;
    //            }
    //        }
    //        return listGrammars;
    //    }

    //    private Map<Integer, Map<LemmasEntity, List<GrammarsEntity>>>  help(List<GrammarsEntity> newGramList,
    //                                                                        int x,ArrayList<String> words,
    //                                                                        int i, ArrayList<LemmasEntity> lemma){
    //        Map<Integer, Map<LemmasEntity, List<GrammarsEntity>>> listGrammars = new HashMap<>();
    //        GrammarsEntity gramFemale = grammarsService.findById("femn");
    //        GrammarsEntity gramMale = grammarsService.findById("masc");
    //        int y =0;
    //        boolean sexBool=true;
    //        while (sexBool && y < newGramList.size()) {
    //            if(newGramList.get(y).equals(gramFemale) || newGramList.get(y).equals(gramMale)){
    //                if(newGramList.get(y).equals(gramFemale)) {
    //                    ClassNameHere class2 = new ClassNameHere();
    //                    boolean res = class2.infoBox(words.get(x) + " - женского рода?", "Вопрос");
    //                    if (res) {
    //                        Map<LemmasEntity,List<GrammarsEntity>> map= new HashMap<>();
    //                        map.put(lemma.get(i), newGramList);
    //                        listGrammars.put(x,map);
    //                        sexBool=false;
    //                    }
    //                    else {
    //                        sexBool = false;
    //                    }
    //                }
    //                else {
    //                    if (newGramList.get(y).equals(gramMale)) {
    //                        ClassNameHere class2 = new ClassNameHere();
    //                        boolean res = class2.infoBox(words.get(x) + " - мужского рода?", "Вопрос");
    //                        if (res) {
    //                            Map<LemmasEntity, List<GrammarsEntity>> map = new HashMap<>();
    //                            map.put(lemma.get(i), newGramList);
    //                            listGrammars.put(x, map);
    //                            sexBool = false;
    //                        }
    //                        else {
    //                            sexBool = false;
    //                        }
    //                    }
    //                }
    //            }
    //            else {
    //                y++;
    //            }
    //        }
    //        return listGrammars;
    //    }

    //    private  Map<Integer,Map<LemmasEntity,List<GrammarsEntity>>> findingAllGrammarsLemmas(ArrayList<ArrayList<LemmasEntity>> lemmas) {
    //        Map<Integer,Map<LemmasEntity,List<GrammarsEntity>>> result = new HashMap<>();
    //        for (int i = 0; i < lemmas.size(); i++) {
    //            List<LemmasEntity> lemma = lemmas.get(i);
    //            Map<LemmasEntity,List<GrammarsEntity>> gramm = new HashMap<>();
    //            for (int j = 0; j < lemma.size(); j++) {
    //                gramm.put(lemma.get(j),lemma.get(j).getGrammars());
    //            }
    //            result.put(i,gramm);
    //        }
    //        return result;
    //    }

    //    private static ArrayList<ArrayList<LemmasEntity>> getAllLemmasForNouns(ArrayList<List<WordformsEntity>> wordformsEntities) {
    //        ArrayList<ArrayList<LemmasEntity>> res = new ArrayList<>();
    //        for (int i = 0; i < wordformsEntities.size(); i++) {
    //            Set<LemmasEntity> set = new HashSet<>();
    //            for (int j = 0; j < wordformsEntities.get(i).size(); j++) {
    //                set.add(wordformsEntities.get(i).get(j).getLemma());
    //            }
    //            LemmasEntity[] result = set.toArray(new LemmasEntity[set.size()]);
    //            ArrayList<LemmasEntity> lem = new ArrayList<LemmasEntity>();
    //            for (int j = 0; j < result.length; j++) {
    //                lem.add(result[j]);
    //            }
    //            res.add(lem);
    //        }
    //
    //        return res;
    //    }

    //    private ArrayList<List<WordformsEntity>> getAllWordFormsForNouns(ArrayList<String> words) {
    //        ArrayList<List<WordformsEntity>> wordformsEntities = new ArrayList<>();
    //        for (int i = 0; i < words.size(); i++) {
    //            List<WordformsEntity> list = wordFormsService.FindByTitle(words.get(i));
    //            wordformsEntities.add(list);
    //        }
    //        return wordformsEntities;
    //    }

    //    private Map<Integer, Map<LemmasEntity, List<GrammarsEntity>>>  delete(Map<Integer, Map<LemmasEntity, List<GrammarsEntity>>> grammarsLemmasNouns){
    //        Collection<Integer> collection = grammarsLemmasNouns.keySet();
    //        for (Integer key : collection) {
    //            Map<LemmasEntity, List<GrammarsEntity>> map = grammarsLemmasNouns.get(key);
    //            if(map.isEmpty()){
    //                grammarsLemmasNouns.remove(map);
    //            }
    //        }
    //        return grammarsLemmasNouns;
    //    }
}
