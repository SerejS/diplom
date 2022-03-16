package com.serejs.diplom.desktop.ui.controllers.modals;

import com.serejs.diplom.desktop.text.container.LiteratureType;
import com.serejs.diplom.desktop.text.container.Theme;
import com.serejs.diplom.desktop.ui.App;
import com.serejs.diplom.desktop.ui.controllers.ThemeController;
import com.serejs.diplom.desktop.ui.controllers.abstracts.ModalController;
import com.serejs.diplom.desktop.ui.controllers.abstracts.TableViewController;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class ModalThemeController extends ModalController<Theme> {
    @FXML
    private TextField titleTheme;
    @FXML
    private Slider slider;
    @FXML
    private ComboBox<Theme> themeBox;

    @FXML
    private VBox keyWordsPane;
    @FXML
    private TextField competitionName;
    @FXML
    private ComboBox<LiteratureType> typeBox;
    @FXML
    private Button addCompetitionButton;


    private Map<LiteratureType, Pair<String, TextArea>> groups = new HashMap<>();

    @Override
    public void setParent(TableViewController<Theme> parent) {
        this.parent = parent;
        if (themeBox != null && parent instanceof ThemeController controller) {
            themeBox.getItems().addAll(controller.getItems());

            double percent;
            var items = parent.getItems();
            if (items.isEmpty())  percent = 100.;
            else percent = (1 - items.stream().mapToDouble(Theme::getPercent).sum()) * 100;
            slider.setMax(percent);
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        typeBox.getItems().addAll(App.getTypes());

        //Добавление для темы ключевые слова к типу литературы
        addCompetitionButton.setOnMouseClicked(event -> {
            var pane = keyWordsPane.getChildren();
            var model = typeBox.getSelectionModel();
            var selected = model.getSelectedItem();

            var name = competitionName.getText();
            if (name.isEmpty()) return;

            pane.add(pane.size() - 2,
                    typeGroup(name, selected, ""));
            model.clearSelection();
            typeBox.getItems().remove(selected);
            competitionName.clear();
        });

        closeInit();
    }


    //Создание группы элементов на тип литераутры
    private VBox typeGroup(String labelName, LiteratureType type, String areaContent) {
        var label = new Label(labelName);

        var types = new ComboBox<LiteratureType>();
        types.getItems().addAll(App.getTypes());
        types.getSelectionModel().select(type);

        var del = new Button("Удалить");

        var hBox = new HBox(label, types, del);
        hBox.setSpacing(50);
        hBox.setAlignment(Pos.CENTER);

        var area = new TextArea(areaContent);
        area.setMinHeight(100);
        area.setWrapText(true);

        groups.put(type, new Pair<>(competitionName.getText(), area));

        var group = new VBox(hBox, area);
        del.setOnMouseClicked(event -> {
            keyWordsPane.getChildren().removeAll(group);
            groups.remove(type);

            typeBox.getItems().add(types.getSelectionModel().getSelectedItem());
        });

        return group;
    }


    //Добавление темы в таблицу
    public void addTheme() {
        if (parent == null) return;

        var competitions = new HashMap<LiteratureType, Pair<String, Set<String>>>();
        groups.forEach((key, pair) -> {
            var keyNGrams = getKeywords(pair.getValue());
            competitions.put(key, new Pair<>(pair.getKey(), keyNGrams));
        });

        if (obj == null) {
            obj = new Theme(themeBox.getValue(), titleTheme.getText(), slider.getValue(), competitions);
            parent.addRow(obj);
            return;
        }

        obj.setRoot(themeBox.getValue());
        obj.setTitle(titleTheme.getText());
        obj.setPercent(slider.getValue() / 100.);
        obj.setMapKeyNGrams(competitions);

        parent.updateRows();
    }

    private Set<String> getKeywords(TextArea area) {
        return Arrays.stream(area.getText().split(","))
                .map(String::trim)
                .collect(Collectors.toSet());
    }


    //(Deprecated) Установка полей для изменения значений темы
    @Override
    public void setObject(Theme theme) {
        this.obj = theme;

        titleTheme.setText(theme.getTitle());
        slider.setValue(theme.getPercent() * 100);
        if (theme.getRoot() != null) themeBox.setValue(theme.getRoot());

        theme.getMapKeyNGrams().forEach((key, pair) -> {
                var keywords = new StringBuilder();
                pair.getValue().forEach(word -> keywords.append(word).append(", "));

                var pane = keyWordsPane.getChildren();
                pane.add(
                        pane.size() - 2,
                        typeGroup(pair.getKey(), key, keywords.toString())
                );

            }
        );
    }
}
