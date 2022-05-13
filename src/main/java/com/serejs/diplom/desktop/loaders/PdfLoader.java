package com.serejs.diplom.desktop.loaders;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.serejs.diplom.desktop.text.container.FormatLiterature;
import com.serejs.diplom.desktop.text.container.Literature;

import java.util.Arrays;

public class PdfLoader extends AbstractLoader {

    @Override
    public void load(Literature literature) throws Exception {
        if (!(literature instanceof FormatLiterature formatSource)) return;

        var format = formatSource.getFormat();
        //Перенос страниц выделения в инфтерфейс источника
        var startPage = 1;

        PdfReader reader = new PdfReader(literature.getUri().toString());

        var textBook = new StringBuilder();
        var strategy = new SimpleTextExtractionStrategy();

        for (int i = startPage; i < reader.getNumberOfPages(); i++) {
            textBook.append(PdfTextExtractor.getTextFromPage(reader, i, strategy));
        }

        var resultContent = Arrays.stream(
                        textBook.toString()
                                .replaceAll("\n", " ")
                                .split("\\s"))
                .map(String::trim)
                .filter(el -> !el.isEmpty())
                .reduce("", (acc, el) -> acc + " " + el.trim());

        var sections = resultContent.split(format.getPrev());
        for (var section : sections) {
            var midIndex = section.indexOf(format.getMid());
            if (midIndex == -1) continue;

            var endIndex = section.lastIndexOf(format.getAfter());
            if (endIndex == -1) endIndex = section.length();

            var title = section.substring(0, midIndex);
            var content = section.substring(midIndex, endIndex);

            fragments.put(title, content);
        }

        reader.close();
    }
}
