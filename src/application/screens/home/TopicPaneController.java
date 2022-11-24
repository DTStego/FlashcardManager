package application.screens.home;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TopicPaneController {

    void initialize() {

    }

    @FXML
    void onDelTopicClicked(MouseEvent event) {
        Node topic = (Node) event.getSource();
        ((VBox) topic.getParent().getParent()).getChildren().remove(topic.getParent());
    }

    @FXML
    void onTopicBtnClicked(MouseEvent event) {
        //open topic page
    }
}
