package com.diploma.law.services;

import com.pullenti.morph.MorphGender;
import com.pullenti.morph.MorphLang;
import com.pullenti.ner.*;
import com.pullenti.ner.core.*;
import com.pullenti.ner.keyword.KeywordReferent;
import com.pullenti.ner.weapon.WeaponAnalyzer;
import com.pullenti.unisharp.Stopwatch;
import com.pullenti.unisharp.Utils;

public class a
{

    public static void main(String[] args) throws Exception
    {
        Stopwatch sw = Utils.startNewStopwatch();
        // инициализация - необходимо проводить один раз до обработки текстов
        System.out.print("Initializing ... ");
        // инициализируются движок и все имеющиеся анализаторы
        Sdk.initialize(MorphLang.ooBitor(MorphLang.RU, MorphLang.EN));
        sw.stop();
        System.out.println("OK (by " + ((int)sw.getElapsedMilliseconds()) + " ms), version " + ProcessorService.getVersion());
        // анализируемый текст
        String txt = "Петров Алексей отдал пистолет Александру Сергеевичу.";
        System.out.println("Text: " + txt);
        // запускаем обработку на пустом процессоре (без анализаторов NER)
        AnalysisResult are = ProcessorService.getEmptyProcessor().process(new SourceOfAnalysis(txt), null, null);
        System.out.print("Noun groups: ");
        // перебираем токены
        for (Token t = are.firstToken; t != null; t = t.getNext())
        {
            // выделяем именную группу с текущего токена
            NounPhraseToken npt = NounPhraseHelper.tryParse(t, NounPhraseParseAttr.NO, 0);
            // не получилось
            if (npt == null) continue;
            // получилось, выводим в нормализованном виде
            System.out.print("[" + npt.getSourceText() + "=>" + npt.getNormalCaseText(null, true, MorphGender.UNDEFINED, false) + "] ");
            // указатель на последний токен именной группы
            t = npt.getEndToken();
        }
        try (Processor proc = ProcessorService.createProcessor())
        {
            // анализируем текст
            AnalysisResult ar = proc.process(new SourceOfAnalysis(txt), null, null);
            // результирующие сущности
            System.out.println("\r\n==========================================\r\nEntities: ");
            for (Referent e : ar.getEntities())
            {
                System.out.println(e.getTypeName() + ": " + e.toString());
                for (Slot s : e.getSlots())
                {
                    System.out.println(" " + s.getTypeName() + ": " + s.getValue());
                }
            }
            // пример выделения именных групп
            System.out.println("\r\n==========================================\r\nNoun groups: ");
            for (Token t = ar.firstToken; t != null; t = t.getNext())
            {
                // токены с сущностями игнорируем
                if (t.getReferent() != null) continue;
                // пробуем создать именную группу
                NounPhraseToken npt = NounPhraseHelper.tryParse(t, NounPhraseParseAttr.ADJECTIVECANBELAST, 0);
                // не получилось
                if (npt == null) continue;
                System.out.println(npt);
                // указатель перемещаем на последний токен группы
                t = npt.getEndToken();
            }
        }
            try (Processor proc = ProcessorService.createSpecificProcessor(WeaponAnalyzer.ANALYZER_NAME))
        {
            AnalysisResult ar = proc.process(new SourceOfAnalysis(txt), null, null);
            System.out.println("\r\n==========================================\r\nKeywords1: ");
            for (Referent e : ar.getEntities())
            {
                System.out.println(e);
            }

            System.out.println("\r\n==========================================\r\nKeywords2: ");
            for (Token t = ar.firstToken; t != null; t = t.getNext())
            {
                if (t instanceof ReferentToken)
                {
                    KeywordReferent kw = Utils.cast(t.getReferent(), KeywordReferent.class);
                    if (kw == null) continue;
                    String kwstr = MiscHelper.getTextValueOfMetaToken(Utils.cast(t, ReferentToken.class),
                            GetTextAttr.of((GetTextAttr.FIRSTNOUNGROUPTONOMINATIVESINGLE.value()) | (GetTextAttr.KEEPREGISTER.value())));
                    System.out.println(kwstr + " = " + kw);
                }
            }
        }
        System.out.println("Over!");
    }

}
