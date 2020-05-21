package com.diploma.law.services;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.pullenti.morph.MorphLang;
import com.pullenti.ner.*;
import com.pullenti.ner._org.OrganizationAnalyzer;
import com.pullenti.ner.address.AddressAnalyzer;
import com.pullenti.ner.address.AddressReferent;
import com.pullenti.ner.core.GetTextAttr;
import com.pullenti.ner.core.MiscHelper;
import com.pullenti.ner.date.DateAnalyzer;
import com.pullenti.ner.date.DateReferent;
import com.pullenti.ner.decree.DecreeAnalyzer;
import com.pullenti.ner.geo.GeoAnalyzer;
import com.pullenti.ner.money.MoneyAnalyzer;
import com.pullenti.ner.person.PersonAnalyzer;
import com.pullenti.ner.person.PersonReferent;
import com.pullenti.ner.phone.PhoneAnalyzer;
import com.pullenti.ner.uri.UriAnalyzer;
import com.pullenti.ner.weapon.WeaponAnalyzer;
import com.pullenti.ner.weapon.WeaponReferent;
import com.pullenti.semantic.SemDocument;
import com.pullenti.semantic.SemanticService;
import com.pullenti.unisharp.Stopwatch;
import com.pullenti.unisharp.Utils;

@Transactional
@Service
public class SearchOptionalThingsServiceImpl implements SearchOptionalThingsService
{

    private SearchOptionalThingsServiceImpl()
    {
        try
        {
            Stopwatch sw = Utils.startNewStopwatch();
            Sdk.initialize(MorphLang.ooBitor(MorphLang.RU, MorphLang.EN));
            sw.stop();
            ProcessorService.initialize(MorphLang.ooBitor(MorphLang.RU, MorphLang.EN));
            MoneyAnalyzer.initialize();
            UriAnalyzer.initialize();
            PhoneAnalyzer.initialize();
            DateAnalyzer.initialize();
            GeoAnalyzer.initialize();
            AddressAnalyzer.initialize();
            OrganizationAnalyzer.initialize();
            PersonAnalyzer.initialize();
            DecreeAnalyzer.initialize();
            SemanticService.initialize();
        }
        catch (Exception exc)
        {

        }
    }

    @Override
    public String getDate(String text)
    {
        try (Processor proc = ProcessorService.createSpecificProcessor(DateAnalyzer.ANALYZER_NAME))
        {
            AnalysisResult ar = proc.process(new SourceOfAnalysis(text), null, null);
            for (Token t = ar.firstToken; t != null; t = t.getNext())
            {
                DateReferent kw = Utils.cast(t.getReferent(), DateReferent.class);
                if (kw == null) continue;
                return MiscHelper.getTextValueOfMetaToken(Utils.cast(t, ReferentToken.class),
                        GetTextAttr.of((GetTextAttr.FIRSTNOUNGROUPTONOMINATIVESINGLE.value()) | (GetTextAttr.KEEPREGISTER.value())));
            }
        }
        return StringUtils.EMPTY;
    }

    @Override
    public String getAddress(String text)
    {
        try (Processor proc = ProcessorService.createSpecificProcessor(AddressAnalyzer.ANALYZER_NAME))
        {
            AnalysisResult ar = proc.process(new SourceOfAnalysis(text), null, null);
            SemDocument doc = SemanticService.process(ar, null);
            for (Token t = ar.firstToken; t != null; t = t.getNext())
            {
                AddressReferent kw = Utils.cast(t.getReferent(), AddressReferent.class);
                if (kw == null) continue;
                return MiscHelper.getTextValueOfMetaToken(Utils.cast(t, ReferentToken.class),
                        GetTextAttr.of((GetTextAttr.FIRSTNOUNGROUPTONOMINATIVESINGLE.value()) | (GetTextAttr.KEEPREGISTER.value())));
            }
        }
        return StringUtils.EMPTY;
    }

    @Override
    public String getWeapon(String text)
    {
        try (Processor proc = ProcessorService.createSpecificProcessor(WeaponAnalyzer.ANALYZER_NAME))
        {
            AnalysisResult ar = proc.process(new SourceOfAnalysis(text), null, null);
            for (Token t = ar.firstToken; t != null; t = t.getNext())
            {
                WeaponReferent kw = Utils.cast(t.getReferent(), WeaponReferent.class);
                if (kw == null) continue;
                return MiscHelper.getTextValueOfMetaToken(Utils.cast(t, ReferentToken.class),
                        GetTextAttr.of((GetTextAttr.FIRSTNOUNGROUPTONOMINATIVESINGLE.value()) | (GetTextAttr.KEEPREGISTER.value())));
            }
        }
        return StringUtils.EMPTY;
    }

    @Override
    public List<String> getNames(String text)
    {
        List<String> names = new ArrayList<>();
        try (Processor proc = ProcessorService.createSpecificProcessor(PersonAnalyzer.ANALYZER_NAME))
        {
            AnalysisResult ar = proc.process(new SourceOfAnalysis(text), null, null);
            for (Token t = ar.firstToken; t != null; t = t.getNext())
            {
                PersonReferent kw = Utils.cast(t.getReferent(), PersonReferent.class);
                if (kw == null) continue;

                String fio = kw.toString();
                names.add(fio);
            }
        }
        return names;
    }

    @Override
    public List<String> getNamesForSubject(String text)
    {
        List<String> names = new ArrayList<>();
        try (Processor proc = ProcessorService.createSpecificProcessor(PersonAnalyzer.ANALYZER_NAME))
        {
            AnalysisResult ar = proc.process(new SourceOfAnalysis(text), null, null);
            for (Token t = ar.firstToken; t != null; t = t.getNext())
            {
                PersonReferent kw = Utils.cast(t.getReferent(), PersonReferent.class);
                if (kw == null) continue;
                String fio = kw.getOccurrence().get(0).toString();
                names.add(fio);
            }
        }
        return names;
    }

}
