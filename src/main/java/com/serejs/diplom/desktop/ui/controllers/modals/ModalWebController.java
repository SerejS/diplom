package com.serejs.diplom.desktop.ui.controllers.modals;

import com.serejs.diplom.desktop.text.container.LiteratureType;
import com.serejs.diplom.desktop.ui.controllers.pages.WebSearchController;
import com.serejs.diplom.desktop.ui.controllers.abstracts.ModalController;
import com.serejs.diplom.desktop.ui.controllers.abstracts.TableViewController;
import com.serejs.diplom.desktop.ui.states.State;
import com.serejs.diplom.desktop.utils.GoogleSearchEngine;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class ModalWebController extends ModalController<GoogleSearchEngine> {
    @FXML
    private TextField cx;
    @FXML
    private TextField key;
    @FXML
    private ComboBox<LiteratureType> litTypes;

    private TableViewController<GoogleSearchEngine> parent;

    @Override
    public void setParent(TableViewController<GoogleSearchEngine> parent) {
        this.parent = parent;

        litTypes.getItems().removeAll(parent.getItems().stream().map(GoogleSearchEngine::getType).toList());
    }

    public void addEngine() throws URISyntaxException {
        if (parent instanceof WebSearchController parent) {
            var engine = new GoogleSearchEngine(
                    cx.getText(),
                    key.getText(),
                    litTypes.getSelectionModel().getSelectedItem());

            parent.addRow(engine);
            State.addEngine(engine);
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        litTypes.getItems().addAll(State.getLitTypes());
        closeInit();
    }

    @Override
    public void setObject(GoogleSearchEngine engine) {

    }
}
