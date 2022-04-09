package com.serejs.diplom.desktop.utils;

import com.serejs.diplom.desktop.analyze.Analyzer;
import com.serejs.diplom.desktop.enums.SourceType;
import com.serejs.diplom.desktop.loaders.*;
import com.serejs.diplom.desktop.text.container.*;
import com.serejs.diplom.desktop.ui.states.State;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import static java.util.stream.Collectors.groupingBy;

public class Processing {
    public static String getResult() throws Exception {
        setTestData();

        ///Извлечение фрагментов
        var mainFragments = new FragmentMap();
        for (Source source : State.getSources()) {
            ContentLoader loader;
            switch (source.getSourceType()) {
                case EPUB -> loader = new EpubLoader();
                case FB2 -> loader = new Fb2Loader();
                case PDF -> loader = new PdfLoader(State.getCustomSources().get(source));
                case CUSTOM -> loader = new CustomLoader(State.getCustomSources().get(source));
                case WEB -> loader = new WebLoader();
                default -> {
                    System.err.println("Тип литературы не определен: " + source.getUri());
                    continue;
                }
            }

            var localThemes = State.getThemes().stream()
                    .filter(t -> t.getTypes().contains(source.getLitType()))
                    .toList();
            if (localThemes.isEmpty()) continue;

            loader.load(source.getUri());
            var contents = loader.getContent();

            for (String key : contents.keySet()) {
                var content = contents.get(key);

                Theme theme = Analyzer.getTheme(content, localThemes);
                if (theme == null) continue;

                System.out.println(source.getUri() + " " + loader.getContent().size());

                Fragment fragment = new Fragment(content, theme, source.getLitType(), loader.getAttachments(key));
                if (fragment.getConcentration() < Settings.getMinConcentration()) {
                    //fragment = AutoSummarizer.summarize(fragment);
                    if (fragment.getConcentration() < Settings.getMinConcentration()) continue;
                }
                mainFragments.put(key, fragment);
            }

        }

        //Обработка
        mainFragments.recalculateThemes();
        Analyzer.alignment(mainFragments);

        State.setFragments(mainFragments);

        return getPlainResult(mainFragments);
    }

    ///Text of MdFile
    public static String getMdResult(FragmentMap mainFragments, File file) throws Exception {
        //Вывод результата
        var result = new StringBuilder();

        // Группировка в формате темы / типы / фрагменты
        var groupedThemes = mainFragments.keySet().stream().collect(
                groupingBy(k -> mainFragments.get(k).getTheme(),
                        groupingBy(k -> mainFragments.get(k).getType())));

        groupedThemes.forEach((theme, groupedTypes) -> {
            result.append("# Тема: ").append(theme.getTitle()).append('\n');

            groupedTypes.forEach((type, keys) -> {
                result.append("## Тип литературы: ").append(type.getTitle()).append("\n");
                keys.forEach(key -> {
                    //Подготовка текста для вывода содержания
                    var fragment = mainFragments.get(key);
                    result.append("### ").append(key).append('\n');
                    result.append("Концентрация ключевых слов: ").append(fragment.getConcentration()).append('\n');
                    result.append(fragment.getContent()).append('\n');

                    //Сохранение приложений
                    fragment.saveAttachments(State.getOutputDirectory());

                });
                result.append("\n");

            });
            result.append("\n");
        });
        FileUtils.writeStringToFile(file, result.toString(), Charset.defaultCharset());
        return result.toString();
    }


    public static String getPlainResult(FragmentMap mainFragments) {
        //Вывод результата
        var result = new StringBuilder();

        result.append("Количество найденных фрагментов: ").append(mainFragments.size()).append('\n');

        // Группировка в формате темы / типы / фрагменты
        var groupedThemes = mainFragments.keySet().stream().collect(
                groupingBy(k -> mainFragments.get(k).getTheme(),
                        groupingBy(k -> mainFragments.get(k).getType())));

        groupedThemes.forEach((theme, groupedTypes) -> {
            result.append(theme.getTitle()).append('\n');

            groupedTypes.forEach((type, keys) -> {
                result.append("Тип литературы: ").append(type.getTitle()).append("\n");
                keys.forEach(key -> {
                    //Подготовка текста для вывода содержания
                    var fragment = mainFragments.get(key);
                    result.append(fragment.getType()).append(" / ").append(key);
                    result.append(": ").append(fragment.getConcentration()).append('\n');
                    result.append(fragment.getContent(), 0, 200).append('\n');

                    //Сохранение приложений
                    fragment.saveAttachments(State.getOutputDirectory());

                });
                result.append("\n");

            });
            result.append("\n");
        });

        return result.toString();
    }


    public static void setTestData() throws URISyntaxException {
        var themes = State.getThemes();

        var type1 = new LiteratureType("Общий", true);
        var type2 = new LiteratureType("Учебник", true);
        var type3 = new LiteratureType("Мемуары", false);

        var theme1Text = """
                история, историческая наука, предмет, дисциплина, определение, вспомогательные исторические дисциплины, место истории, общественная дисциплиа, представители, отечественная наука, этногенез славян, ранняя история славян, проблема достоверности, проблема прародины, структура политической власти, политическая власть, социальная стратиграфия, религия, религиозные верования, формирование объединений, древнерусское государство, политика, экономика, социальные факторы, география, культура, восточные славяне, образование государства восточных славян, теории происхождения, норманнская теория, критика, внутренняя политика, внешняя политика киевских князей,причины распада, Галицко-Волынские земли, Владимиро-Суздальские земли, Новгородские земли, рыцарская агрессия с запада, рыцари, агрессия, монголо-татарское нашествие, образование Древнерусского государства, крещение руси, влияние крещения, развитие политической сферы, развитие социальной сферы, развитие духовной сферы, развитие общества,Монголо-татарское нашествие, нашествие, поражения Руси, причины поражения, влияние поражения, рыцарские ордена, рыцари, территории Восточной Европы, XII, XIII, XIV, 12, 13, 14, Князь Александр Невский, Александр Невский, Ханы, ханы Золотой Орды, золотая орда, Батый, Узбек, Тохтамыш
                """;


        var theme2Text = """
                Иван III, государь всея Руси, причины, предпосылки, цена, значение, формирования единого централизованного Московского государства, центролизованное гостударство, Московское государство, Москва, XV, XVI, Венчание на царство Ивана Грозного, Иван 4, Иван IV, Иван Грозный, формирование идеологии, самодержавие, Опричнина, внешняя политика Ивана Грозного, ливонская война, Поход Ермака, Ермака, этапы смутного времени, смутное время, Земский собор, 1613, начало правления Романовых, Романовы, Михаил Романов, окончание гражданской войны, гражданская война, сословно-представительной монархия, особенности монархии, Политический строй, политический строй России, приказная система, особенности российского самодержавия, самодержавие, Смута в России, смута, Бунташный век, Реформы Никона, Никон, церковный раскол, государственные и общественные деятели XVII века, 12 век, 12, Морозов, Матвеев, Голицын, Присоединение Левобережной Украины, Левобережная Украина, Украина, 15, 16
                                
                """;

        var theme3Text = """
                Предпосылки реформ Петра I, Петр I, Петр 1, реформы, особенности модернизационного процесса в России, модернизация, Северная война, итоги северной войны, изменение места России, Российская империя, провозглашение империей, дворцовые перевороты, Бироновщина, Елизавета Петровна, Семилетняя война, 1762, воцарение Екатерины II, Екатерина 2, Екатерина II, реформы Екатерины Великой, Екатерина Великая, восстание Емельяна Пугачева, Павел I, Павел 1, Личность Павла I, Политика Павла I, основные направления политики Павла, Выход к Черному морю, выход России к черному морю, Речь Посполитая, вхождение украинских земель, украинские земли, белорусские земели,состав империи, Реформы в России, проблемы реформ, консерваторы, либералы, борьба консервативных и либеральных, XIX, 19,  Экономическое развитие страны, внешняя политика Российской империи в XIX веке, Витте, модернизация экономики России, Финансовая реформа, земельный вопрос в России, начало XX века, Реформы Столыпина, Столыпин, Мятеж реформаторов, мятеж, Общественное движение, Николая I, Николай 1, революционеры, Расстановка политических сил, многопартийная система, деятельность дум, I государственная дума, II Государственная дума, российский парламентаризм, кризис третьеиюньской монархии, третьиюньская монархия, Борьба за передел мира, передел мира, основные фронты, итоги войны, участие России в войне, 1917, Февральская революция, Февральская революция 1917 года, отречение Николая II, Николай II, Временное правительство, Петроградский совет, совет рабочих,совет солдатских депутатов, солдатские депутаты, депутаты, приход большевиков к власти, большивики, революция, создание советского государства, советское государство, 20, 18, XX, XVIII
                """;

        var types = new HashSet<LiteratureType>();
        types.add(type2);

        var th1 = new Theme(null, "Русь IX-XIV вв.",
                0.3, theme1Text, types);
        var th2 = new Theme(null, "Московское государство в XV-XVII вв.",
                0.3, theme2Text, types);
        var th3 = new Theme(null, "Российская империя XVIII – нач. XX века: этапы становления и развития.",
                0.3, theme3Text, types);


        String rootPath = System.getenv().get("resourcePath");
        //var customSource = new Source(new URI(rootPath + "myText.txt"), SourceType.CUSTOM, type1);
        var mainSources = new ArrayList<>(List.of(new Source[]{
                new Source(new URI(rootPath + "seneka.fb2"), SourceType.FB2, type3),
                new Source(new URI(rootPath + "genrikh8.fb2"), SourceType.FB2, type3),
                new Source(new URI(rootPath + "gegel.epub"), SourceType.EPUB, type3),
                new Source(new URI(rootPath + "kultura.fb2"), SourceType.FB2, type2),
                new Source(new URI(rootPath + "!tatishev.fb2"), SourceType.FB2, type2),
                new Source(new URI(rootPath + "histRusXX.fb2"), SourceType.FB2, type2),
                new Source(new URI(rootPath + "froyanov.fb2"), SourceType.FB2, type2),
                //customSource
        }));

        themes = new LinkedList<>();
        themes.add(th1);
        themes.add(th2);
        themes.add(th3);

        State.setSources(mainSources);
    }
}
