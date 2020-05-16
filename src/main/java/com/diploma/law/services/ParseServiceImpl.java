package com.diploma.law.services;

import java.util.*;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.diploma.law.dto.MainDto;
import com.diploma.law.models.ClarifyingFactsEntity;
import com.diploma.law.models.LemmasEntity;
import com.diploma.law.models.ObjectsEntity;
import com.diploma.law.models.WordformsEntity;

@Transactional
@Service
public class ParseServiceImpl implements ParseService
{

    @Autowired
    private WordFormsService wordFormsService;

    @Override
    public MainDto parseText(String text)
    {
        MainDto mainDto = new MainDto();
        if (StringUtils.isNotEmpty(text))
        {
            mainDto.setText(text);
            ArrayList<String> sentences = getSentences(text);
            mainDto.setSentences(sentences);
            ArrayList<String> words = getWords(sentences);
            mainDto.setWords(words);
            ArrayList<WordformsEntity> wordForms = getWordForms(words);
            mainDto.setWordForms(wordForms);
            ArrayList<LemmasEntity> lemmas = getLemmas(wordForms);
            mainDto.setLemmas(lemmas);
            ObjectsEntity object = getObject(lemmas);
            mainDto.setObject(object);
            if (object != null)
            {
                mainDto.setMainSentence(getMainSentence(sentences, object));
                List<ClarifyingFactsEntity> clarifyingFacts = getClarifyingFacts(lemmas, object.getClarifyingfacts());
                mainDto.setClarifyingFacts(clarifyingFacts);
            }
        }
        return mainDto;
    }

    private List<ClarifyingFactsEntity> getClarifyingFacts(ArrayList<LemmasEntity> lemmas, List<ClarifyingFactsEntity> clarifyingFacts)
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

    private static ArrayList<LemmasEntity> getLemmas(ArrayList<WordformsEntity> wordformsEntities)
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

    private ArrayList<WordformsEntity> getWordForms(ArrayList<String> words)
    {
        ArrayList<WordformsEntity> wordForms = new ArrayList<>();
        for (String word : words)
        {
            List<WordformsEntity> list = wordFormsService.FindByTitle(word);
            wordForms.addAll(list);
        }
        return wordForms;
    }

    private ArrayList<String> getWords(List<String> sentences)
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

    private ArrayList<String> getSentences(String text)
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

    private String getMainSentence(List<String> sentences, ObjectsEntity object)
    {
        List<LemmasEntity> objectsLemma = object.getLemmas();
        ArrayList<String> res = new ArrayList<>();
        for (LemmasEntity lemmasEntity : objectsLemma)
        {
            for (String sentence : sentences)
            {
                if (sentence.indexOf(lemmasEntity.getTitle()) > 0)
                {
                    res.add(sentence);
                }
            }
        }
        return res.get(0);
    }
}
