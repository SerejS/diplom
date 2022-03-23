module diplom.desktop.ui {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires morph;
    requires russian;
    requires epublib.core;
    requires org.jsoup;
    requires fb2parser;
    requires org.apache.httpcomponents.httpcore;
    requires org.json;
    requires org.apache.httpcomponents.httpclient;
    requires java.desktop;
    requires static lombok;


    exports com.serejs.diplom.desktop.ui;
    opens com.serejs.diplom.desktop.ui to javafx.fxml;
    opens com.serejs.diplom.desktop.text.container;
    opens com.serejs.diplom.desktop.utils;
    exports com.serejs.diplom.desktop.ui.controllers;
    opens com.serejs.diplom.desktop.ui.controllers to javafx.fxml;
    exports com.serejs.diplom.desktop.ui.controllers.modals;
    opens com.serejs.diplom.desktop.ui.controllers.modals to javafx.fxml;
    exports com.serejs.diplom.desktop.ui.controllers.abstracts;
    opens com.serejs.diplom.desktop.ui.controllers.abstracts to javafx.fxml;
}