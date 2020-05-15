package com.diploma.law.services;

import com.diploma.law.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import javax.transaction.Transactional;
import java.util.*;

@Service("AlgorithmService")
@Transactional
public class AlgorithmServiceImpl implements AlgorithmService
{
    @Autowired
    WordFormsService wordFormsService;

    @Override
    public List<ArticlesEntity> qualifyOffense(ProblemsEntity problem, BindingResult bindingResult)
    {
        String text = problem.getText();
        List<ArticlesEntity> articles = new ArrayList<>();
        List<String> sentences = textSegmentation(text);
        ArrayList<String> words = getWordsFromText(sentences);
        ArrayList<WordformsEntity> wordForms = getAllWordForms(words);
        ArrayList<LemmasEntity> lemmas = getAllLemmas(wordForms);
        ObjectsEntity object = getObject(lemmas);
        if (object == null)
        {
            bindingResult.rejectValue("title", "error.title",
                    "*Невозможно квалифицировать данное преступление. Пожалуйса, переформулируйте его другими словами.");
        }
        else
        {
            //нашли все доп слова, которые подходят под объект жизнь человека
            List<ClarifyingFactsEntity> clarifyingFacts = object.getClarifyingfacts();
            //проверяет есть ли леммы, которые подходят под под статьи
            if (findingAllClarifyingFactsThatOccurInTheText(lemmas, clarifyingFacts).size() != 0)
            {
                List<ClarifyingFactsEntity> cf = findingAllClarifyingFactsThatOccurInTheText(lemmas, clarifyingFacts);//находит эти леммы
                for (ClarifyingFactsEntity clarifyingFactsEntity : cf)
                {
                    articles.add(clarifyingFactsEntity.getCorpus().getArticle());
                }
            }
            else
            {
                for (ClarifyingFactsEntity clarifyingFact : clarifyingFacts)
                {
                    if (clarifyingFact.getLemma() == null) articles.add(clarifyingFact.getCorpus().getArticle());
                }
            }
        }
        return articles;
    }

    private List<ClarifyingFactsEntity> findingAllClarifyingFactsThatOccurInTheText(ArrayList<LemmasEntity> lemmas,
            List<ClarifyingFactsEntity> clarifyingFacts)
    {
        List<ClarifyingFactsEntity> result = new ArrayList<>();
        for (LemmasEntity lemma : lemmas)
        {
            for (ClarifyingFactsEntity clarifyingFact : clarifyingFacts)
            {
                if (clarifyingFact.getLemma() != null && lemma.getIdLemma() == clarifyingFact.getLemma().getIdLemma())
                {
                    result.add(clarifyingFact);
                }
            }
        }
        return result;
    }

    private ObjectsEntity getObject(ArrayList<LemmasEntity> lemmas)
    {
        Set<ObjectsEntity> set = new HashSet<>();
        for (LemmasEntity lemma : lemmas)
        {
            set.addAll(lemma.getObjects());
        }
        ObjectsEntity[] result = set.toArray(new ObjectsEntity[set.size()]);
        if (result.length != 0)
        {
            return result[0];
        }
        else
        {
            return null;
        }
    }

    private static ArrayList<LemmasEntity> getAllLemmas(ArrayList<WordformsEntity> wordformsEntities)
    {
        Set<LemmasEntity> set = new HashSet<>();
        ArrayList<LemmasEntity> res = new ArrayList<>();
        for (WordformsEntity wordformsEntity : wordformsEntities)
        {
            set.add(wordformsEntity.getLemma());
        }
        LemmasEntity[] result = set.toArray(new LemmasEntity[set.size()]);
        Collections.addAll(res, result);
        return res;
    }

    private ArrayList<WordformsEntity> getAllWordForms(ArrayList<String> words)
    {
        ArrayList<WordformsEntity> wordForms = new ArrayList<>();
        for (String word : words)
        {
            List<WordformsEntity> list = wordFormsService.FindByTitle(word);
            wordForms.addAll(list);
        }
        return wordForms;
    }

    private ArrayList<String> getWordsFromText(List<String> sentences)
    {
        ArrayList<String> words = new ArrayList<>();
        for (int i = 0; i < sentences.size(); i++)
        {
            String sentence = sentences.remove(i);
            sentence = sentence.replaceAll("[^A-Za-zА-Яа-я0-9 Ёё]", " ");
            sentence = sentence.replaceAll(",", " ");
            sentence = sentence.replaceAll(":", " ");
            sentence = sentence.replaceAll(";", " ");
            sentence = sentence.replaceAll(" {2}", " ");
            sentences.add(i, sentence);
            String[] word = sentences.get(i).split(" ");
            for (String s : word)
            {
                words.add(s.trim());
            }
        }
        return words;
    }

    private ArrayList<String> textSegmentation(String text)
    {
        text = text.toLowerCase(new Locale("ru"));
        String[] sentences = text.split("\\.");
        for (int i = 0; i < sentences.length; i++)
        {
            sentences[i] = sentences[i].trim();
            if (sentences[i].length() == 1)
            {
                sentences[i] = sentences[i] + "." + sentences[i + 1];
                sentences[i + 1] = sentences[i + 1].trim();
                if (i + 1 < sentences.length && sentences[i + 1].length() == 1)
                {
                    sentences[i] = sentences[i] + "." + sentences[i + 2];
                    if (sentences.length - 2 - i + 1 >= 0)
                        System.arraycopy(sentences, i + 1 + 2, sentences, i + 1, sentences.length - 2 - i + 1);
                    sentences[sentences.length - 2] = "";
                }
                else
                {
                    if (sentences.length - 1 - i + 1 >= 0)
                        System.arraycopy(sentences, i + 1 + 1, sentences, i + 1, sentences.length - 1 - i + 1);
                }
                sentences[sentences.length - 1] = "";
            }
        }
        ArrayList<String> res = new ArrayList<>();
        for (String sentence : sentences)
        {
            if (!sentence.equals(""))
            {
                res.add(sentence);
            }
        }
        return res;
    }

}
