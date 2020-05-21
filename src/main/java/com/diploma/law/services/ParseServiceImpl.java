package com.diploma.law.services;

import java.util.*;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.diploma.law.dto.MainDto;
import com.diploma.law.dto.WordDto;
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
    @Autowired
    private GrammarsService  grammarsService;

    @Override
    public MainDto parseText(String text)
    {
        MainDto mainDto = new MainDto();
        if (StringUtils.isNotEmpty(text))
        {
            mainDto.setText(text);
            List<String> sentences = getSentences(text);
            mainDto.setSentences(sentences);
            Map<Integer, List<WordDto>> wordsMap = parseSentences(sentences);
            mainDto.setWordMap(wordsMap);
            List<LemmasEntity> allLemmas = findAllLemmas(wordsMap);
            mainDto.setAllLemas(allLemmas);
            ObjectsEntity object = getObject(allLemmas);
            mainDto.setObject(object);
            if (object != null)
            {
                getMainSentence(sentences, object, mainDto);
                List<ClarifyingFactsEntity> clarifyingFacts = getClarifyingFacts(allLemmas, object.getClarifyingfacts());
                mainDto.setClarifyingFacts(clarifyingFacts);
            }
        }
        return mainDto;
    }

    private List<LemmasEntity> findAllLemmas(Map<Integer, List<WordDto>> wordsMap)
    {
        List<LemmasEntity> allLemmas = new ArrayList<>();
        for (Map.Entry<Integer, List<WordDto>> entry : wordsMap.entrySet())
        {
            List<WordDto> wordDtos = entry.getValue();
            for (WordDto wordDto : wordDtos)
            {
                allLemmas.add(wordDto.getLemmas());
            }
        }
        return allLemmas;
    }

    private Map<Integer, List<WordDto>> parseSentences(List<String> sentences)
    {
        Map<Integer, List<WordDto>> wordsMap = new HashMap<>();
        for (int i = 0; i < sentences.size(); i++)
        {
            List<WordDto> wordDtos = new ArrayList<>();
            List<String> words = getWords(sentences.get(i));
            for (String word : words)
            {
                WordDto wordDto = new WordDto();
                wordDto.setWords(word);
                List<WordformsEntity> wordForms = wordFormsService.FindByTitle(word);
                if (!wordForms.isEmpty())
                {
                    wordDto.setWordForms(wordForms.get(0));
                    LemmasEntity lemma = wordForms.get(0).getLemma();
                    wordDto.setLemmas(lemma);
                    wordDto.setObject(!lemma.getObjects().isEmpty());
                    wordDto.setGrammars(grammarsService.findById(lemma.getIdLemma()));
                    wordDtos.add(wordDto);
                }
            }
            wordsMap.put(i + 1, wordDtos);
        }
        return wordsMap;
    }

    private List<ClarifyingFactsEntity> getClarifyingFacts(List<LemmasEntity> lemmas, List<ClarifyingFactsEntity> clarifyingFacts)
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

    private ObjectsEntity getObject(List<LemmasEntity> lemmas)
    {
        for (LemmasEntity lemma : lemmas)
        {
            if (!lemma.getObjects().isEmpty())
            {
                return lemma.getObjects().get(0);
            }
        }
        return null;
    }

    private List<String> getWords(String sentence)
    {
        List<String> words = new ArrayList<>();
        sentence = sentence.replaceAll("[^A-Za-zА-Яа-я0-9 Ёё]", " ");
        sentence = sentence.replaceAll(",", " ");
        sentence = sentence.replaceAll(":", " ");
        sentence = sentence.replaceAll(";", " ");
        sentence = sentence.replaceAll(" {2}", " ");
        String[] word = sentence.split(" ");
        for (String s : word)
        {
            words.add(s.trim());
        }
        return words;
    }

    private List<String> getSentences(String text)
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
        List<String> res = new ArrayList<>();
        for (String sentence : sentences)
        {
            if (!sentence.equals(""))
            {
                res.add(sentence);
            }
        }
        return res;
    }

    private void getMainSentence(List<String> sentences, ObjectsEntity object, MainDto mainDto)
    {
        List<LemmasEntity> objectsLemma = object.getLemmas();
        for (LemmasEntity lemmasEntity : objectsLemma)
        {
            for (WordformsEntity wordformsEntity : lemmasEntity.getWordforms())
            {

                for (int i = 0; i < sentences.size(); i++)
                {
                    if (sentences.get(i).indexOf(wordformsEntity.getTitle()) > 0)
                    {
                        mainDto.setMainSentenceInt(i + 1);
                        mainDto.setMainSentence(sentences.get(i));
                        return;
                    }
                }
            }
        }
    }
}
