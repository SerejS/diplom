package com.serejs.diplom.desktop.ui.controllers.modals;

import com.serejs.diplom.desktop.ui.controllers.abstarts.RootController;
import com.serejs.diplom.desktop.ui.controllers.abstarts.TableViewController;
import javafx.fxml.Initializable;

public abstract class ModalController<T> extends RootController implements Initializable {
    protected TableViewController<T> parent;

    public abstract void setParent(TableViewController<T> parentController);
}
