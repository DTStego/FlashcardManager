package application.screens.home;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TopicPaneController {

    @FXML
    private Pane topicPane;

    @FXML
    void initialize() {
    }

    @FXML
    void onDelTopicClicked(MouseEvent event) {
        ((VBox) topicPane.getParent()).getChildren().remove(topicPane);
    }

    @FXML
    void onTopicBtnClicked(MouseEvent event) {
        //open topic page
    }
}
