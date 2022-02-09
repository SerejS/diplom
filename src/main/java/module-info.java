module diplom.desktop.ui {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;

    exports com.serejs.diplom.desktop.ui;
    opens com.serejs.diplom.desktop.ui to javafx.fxml;
    opens com.serejs.diplom.desktop.containers;
    exports com.serejs.diplom.desktop.ui.controllers;
    opens com.serejs.diplom.desktop.ui.controllers to javafx.fxml;
}