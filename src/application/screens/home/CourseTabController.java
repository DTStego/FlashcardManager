package application.screens.home;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

public class CourseTabController {

    @FXML
    private VBox topicList;

    public void addTopic(Node topic) {
        topicList.getChildren().add(topic);
    }

    public int getTopicListSize() {return topicList.getChildren().size();}
}
