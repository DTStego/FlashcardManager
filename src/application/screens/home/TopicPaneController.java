package application.screens.home;

import application.Main;
import application.managers.Course;
import application.managers.Topic;
import application.screens.home.HomeScreenController;
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
    private Button topicName;

    @FXML
    void initialize() {
    }

    @FXML
    void onDelTopicClicked(MouseEvent event) {
        ((VBox) topicPane.getParent()).getChildren().remove(topicPane);
/*        Course currentCourse = HomeScreenController.getCurrentCourse();
        Topic currentTopic = null;

        for (Topic topic : currentCourse.getTopicList()) {
            if (topic.getName().equals(topicName.toString())) {
                currentTopic = topic;
            }
        }

        currentCourse.getTopicList().remove(currentTopic); 
        //Delete inside of main User class too */
    }

    @FXML
    void onTopicBtnClicked(MouseEvent event) {
        //open topic page
    }
}
