package com.serejs.diplom.desktop.utils;

import net.steppschuh.markdowngenerator.table.Table;
import net.steppschuh.markdowngenerator.table.TableRow;
import org.jsoup.nodes.Element;

public class MarkDown {
    public static String mdTable(Element table) {
        var builder = new Table.Builder();

        var rows = table.select("tr");
        for (Element row : rows) {
            var cells = row.select("td, th").stream().map(Element::text)
                    .filter(str -> !str.isEmpty()).toList();

            var tableRow = new TableRow<>(cells);
            builder.addRow(tableRow);
        }
        return builder.build().toString();
    }
}
