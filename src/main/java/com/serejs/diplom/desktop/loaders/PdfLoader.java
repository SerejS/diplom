package com.serejs.diplom.desktop.loaders;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.serejs.diplom.desktop.text.container.Format;

import java.net.URI;
import java.util.Arrays;

public class PdfLoader extends AbstractLoader {
    private final Format format;

    public PdfLoader(Format format) {
        this.format = format;
    }

    @Override
    public void load(URI uri) throws Exception {
        PdfReader reader = new PdfReader(uri.toString());

        var textBook = new StringBuilder();
        var strategy = new SimpleTextExtractionStrategy();
        for (int i = 1; i < reader.getNumberOfPages(); i++) {
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
