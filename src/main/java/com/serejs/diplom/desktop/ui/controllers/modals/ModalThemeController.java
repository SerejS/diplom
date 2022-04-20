package com.serejs.diplom.desktop.ui.controllers.modals;

import com.serejs.diplom.desktop.text.container.LiteratureType;
import com.serejs.diplom.desktop.text.container.Theme;
import com.serejs.diplom.desktop.ui.alerts.ErrorAlert;
import com.serejs.diplom.desktop.ui.controllers.abstracts.ModalController;
import com.serejs.diplom.desktop.ui.controllers.abstracts.TableViewController;
import com.serejs.diplom.desktop.ui.controllers.pages.ThemeController;
import com.serejs.diplom.desktop.ui.states.State;
import com.serejs.diplom.desktop.utils.Settings;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.controlsfx.control.CheckComboBox;

import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;

public class ModalThemeController extends ModalController<Theme> {
    @FXML
    private TextField titleTheme;
    @FXML
    private Slider slider;
    @FXML
    private ComboBox<Theme> themeBox;
    @FXML
    private Label percentLabel;

    @FXML
    private CheckComboBox<LiteratureType> typeBox;
    @FXML
    private TextArea areaNGrams;


    @Override
    public void setParent(TableViewController<Theme> parent) {
        this.parent = parent;
        if (themeBox != null && parent instanceof ThemeController controller) {
            themeBox.getItems().addAll(controller.getItems());

            double percent;
            var items = parent.getItems();
            if (items.isEmpty()) percent = 100.;
            else percent = (1 - items.stream().mapToDouble(Theme::getPercent).sum()) * 100;
            slider.setMax(percent);
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        areaNGrams.setWrapText(true);
        typeBox.getItems().addAll(State.getLitTypes());

        slider.addEventHandler(EventType.ROOT, event ->
            percentLabel.setText(Math.round(slider.getValue()) + "%")
        );

        closeInit();
    }



    //Добавление темы в таблицу
    public void addTheme() {
        if (parent == null) return;

        var textNGrams = areaNGrams.getText();
        var types = typeBox.getCheckModel().getCheckedItems();

        //Валидация
        if (titleTheme.getText().isEmpty()) {
            ErrorAlert.info("Не введено название темы");
            return;
        }
        if (types.isEmpty()) {
            ErrorAlert.info("Не выбран хотя бы один тип литературы");
            return;
        }
        if (slider.getValue() == 0) {
            ErrorAlert.info("Процентное отношение темы не может быть 0%");
            return;
        }
        if (textNGrams.split(",").length < Settings.getMinKeyNGrams()) {
            ErrorAlert.info("Количество ключевых n-грамм меньше минимально допустимого");
            return;
        }


        if (obj == null) {
            obj = new Theme(themeBox.getValue(), titleTheme.getText(), slider.getValue(), textNGrams, new HashSet<>(types));
            parent.addRow(obj);
            return;
        }

        obj.setRoot(themeBox.getValue());
        obj.setTitle(titleTheme.getText());
        obj.setPercent(slider.getValue() / 100.);
        obj.setKeyNGrams(textNGrams);

        parent.updateRows();
    }


    @Override
    public void setObject(Theme theme) {
        this.obj = theme;

        var startPercent = theme.getPercent() * 100;
        titleTheme.setText(theme.getTitle());
        slider.setValue(startPercent);
        slider.setMax(slider.getMax() + startPercent);

        percentLabel.setText(Math.round(startPercent) + "%");


        if (theme.getRoot() != null) themeBox.setValue(theme.getRoot());
        theme.getTypes().forEach(type -> typeBox.getCheckModel().check(type));
        themeBox.getItems().removeAll(theme);

        areaNGrams.setText(theme.getTextKeyNGrams());
    }
}
