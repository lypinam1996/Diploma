package com.diploma.law.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import com.diploma.law.dto.MainDto;
import com.diploma.law.dto.ParserObject;
import com.diploma.law.dto.WordDto;
import com.diploma.law.models.GrammarsEntity;
import com.diploma.law.models.ProblemsEntity;
import com.pullenti.ner.AnalysisResult;
import com.pullenti.ner.Processor;
import com.pullenti.ner.ProcessorService;
import com.pullenti.ner.SourceOfAnalysis;
import com.pullenti.ner.person.PersonAnalyzer;
import com.pullenti.semantic.*;

@Service
@Transactional
public class GetSubjectServiceImpl implements GetSubjectService
{

    @Autowired
    private SearchOptionalThingsService optionalThingsService;

    @Override
    public void getSubject(MainDto mainDto, ProblemsEntity problemsEntity)
    {
        List<String> names = optionalThingsService.getNamesForSubject(mainDto.getText());
        List<String> persons = optionalThingsService.getNames(mainDto.getText());
        ArrayList<SemObject> objects = prepareText(mainDto.getText(), names);
        ArrayList<SemLink> links = getLinks(mainDto, objects);
        String subject = StringUtils.EMPTY;
        List<String> victims = new ArrayList<>();
        String victim = "";
        for (SemLink link : links)
        {
            String word = link.getTarget().toString();
            if (link.typ == SemLinkType.AGENT)
            {
                subject = findPerson(persons, subject, link);
            }
            else
            {
                victim = findPerson(persons, subject, link);
                if (StringUtils.isEmpty(victim))
                {
                    for (Map.Entry<Integer, List<WordDto>> entry : mainDto.getWordMap().entrySet())
                    {
                        victims.addAll(findVictim(entry, word));
                    }
                }
            }
        }
        if (!victims.isEmpty())
        {
            victim = " " + victims.get(0);
        }
        problemsEntity.setVictims(victim);
        problemsEntity.setSubject(subject);

    }

    private List<String> findVictim(Map.Entry<Integer, List<WordDto>> entry, String word)
    {
        String victim = StringUtils.EMPTY;
        String object = StringUtils.EMPTY;
        for (WordDto wordDto : entry.getValue())
        {
            if (StringUtils.equals(word, wordDto.getWords()))
            {
                List<GrammarsEntity> grammars = wordDto.getGrammars();
                for (GrammarsEntity grammar : grammars)
                {
                    if (grammar.getIdGrammar().equals("anim"))
                    {
                        victim = victim + word + " ";
                    }
                    else
                    {
                        object = object + word + " ";
                    }
                }

            }
        }

        return Arrays.asList(victim, object);
    }

    private String findPerson(List<String> persons, String subject, SemLink link)
    {
        for (String person : persons)
        {
            if (person.toLowerCase().contains(link.getTarget().toString().toLowerCase()))
            {
                subject = subject + person + " ";
            }
        }
        return subject;
    }

    private ArrayList<SemLink> getLinks(MainDto mainDto, ArrayList<SemObject> objects)
    {
        ArrayList<SemLink> links = new ArrayList<>();
        for (SemObject object : objects)
        {
            for (WordDto wordDto : mainDto.getWordMap().get(mainDto.getMainSentenceInt()))
            {
                if (object.tokens != null && !object.tokens.isEmpty())
                {
                    if (object.tokens.get(0).getBeginToken().getSourceText().equals(wordDto.getWords()) && wordDto.isObject())
                    {
                        links.addAll(object.linksFrom);
                        links.addAll(object.linksTo);
                    }
                }
            }
        }
        return links;
    }

    private ArrayList<SemObject> prepareText(String text, List<String> names)
    {
        for (String name : names)
        {
            name.trim();
            text = StringUtils.replace(text, name, StringUtils.substring(name, name.indexOf(" "), name.lastIndexOf(" ")));
        }
        ArrayList<SemObject> objects = new ArrayList<>();
        try (Processor proc = ProcessorService.createSpecificProcessor(PersonAnalyzer.ANALYZER_NAME))
        {
            AnalysisResult arNew = proc.process(new SourceOfAnalysis(text), null, null);
            SemDocument doc = SemanticService.process(arNew, null);

            for (SemBlock block : doc.blocks)
            {
                for (SemFragment semFragment : block.fragments)
                {
                    objects.addAll(semFragment.getGraph().objects);
                }
            }
        }
        return objects;
    }

    @Override
    public void getVictimAndSubject(MainDto mainDto, ProblemsEntity problemsEntity)
    {
        ParserObject[] syntaxObjects = syntax(mainDto.getMainSentence());
        addSyntax(mainDto, syntaxObjects);
        findNouns(mainDto);
        findSubject(mainDto, problemsEntity);

    }

    private void findNouns(MainDto mainDto)
    {
        List<WordDto> words = mainDto.getWordMap().get(mainDto.getMainSentenceInt());
        List<WordDto> result = new ArrayList<>();
        for (WordDto wordDto : words)
        {
            List<GrammarsEntity> grammars = wordDto.getGrammars();
            for (GrammarsEntity entity : grammars)
            {
                if (entity.getIdGrammar().equals("anim"))
                {
                    wordDto.setNoun(true);
                }
            }
            result.add(wordDto);
        }
        mainDto.getWordMap().replace(mainDto.getMainSentenceInt(), result);

    }

    private ParserObject[] syntax(String sentence)
    {
        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl = "http://localhost:2000/parse?text=" + sentence;
        return restTemplate.getForObject(fooResourceUrl, ParserObject[].class);
    }

    private void addSyntax(MainDto mainDto, ParserObject[] parserObjects)
    {
        List<WordDto> words = mainDto.getWordMap().get(mainDto.getMainSentenceInt());
        List<WordDto> result = new ArrayList<>();
        for (WordDto wordDto : words)
        {
            for (ParserObject parserObject : parserObjects)
            {
                if (wordDto.getWords().equals(parserObject.getForm().toLowerCase()))
                {
                    wordDto.setParserObject(parserObject);
                    result.add(wordDto);
                }
            }
        }
        mainDto.getWordMap().replace(mainDto.getMainSentenceInt(), result);
    }

    private void findSubject(MainDto mainDto, ProblemsEntity problemsEntity)
    {
        List<WordDto> words = mainDto.getWordMap().get(mainDto.getMainSentenceInt());
        WordDto verb = null;
        List<WordDto> nouns = new ArrayList<>();
        for (WordDto word : words)
        {
            if (word.isObject())
            {
                verb = word;
            }
            else if (word.isNoun())
            {
                nouns.add(word);
            }
        }
        if (verb != null && !CollectionUtils.isEmpty(nouns))
        {
            ParserObject verbSyntax = verb.getParserObject();
            String property = verbSyntax.getDepRel();
            String subject = org.apache.commons.lang.StringUtils.EMPTY;
            String victim = org.apache.commons.lang.StringUtils.EMPTY;
            if (property.equals("ROOT") || property.equals("сент-соч") || property.equals("соч-союзн") || property.equals("подч-союзн"))
            {
                for (WordDto wordDto : nouns)
                {

                    if (wordDto.getParserObject().getDepRel().equals("предик"))
                    {
                        subject = subject + " " + wordDto.getLemmas().getTitle();
                    }
                    else
                    {
                        victim = victim + " " + wordDto.getLemmas().getTitle();
                    }
                }
            }
            else if (property.equals("пасс-анал"))
            {
                for (WordDto wordDto : nouns)
                {
                    if (wordDto.getParserObject().getDepRel().equals("предик"))
                    {
                        victim = victim + " " + wordDto.getLemmas().getTitle();
                    }
                    else
                    {
                        subject = subject + " " + wordDto.getLemmas().getTitle();
                    }
                }
            }
            problemsEntity.setSubject(subject);
            problemsEntity.setVictims(victim);
        }
    }
}
