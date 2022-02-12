package com.serejs.diplom.desktop.ui.controllers;

import com.serejs.diplom.desktop.ui.App;
import com.serejs.diplom.desktop.ui.controllers.abstarts.RootController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;

public class ResultController extends RootController implements Initializable {
    @FXML private Button finishButton;
    @FXML private TextArea area;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        area.setWrapText(true);

        try {
            area.setText(App.getResult());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //area.setText("Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt. Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem. Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur? Quis autem vel eum iure reprehenderit qui in ea voluptate velit esse quam nihil molestiae consequatur, vel illum qui dolorem eum fugiat quo voluptas nulla pariatur?");
    }

    public void goProjectOverview() {anotherPage(finishButton, "project-overview.fxml");}
}
