package application.screens.home;

import application.managers.Course;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

public class CourseTabController {
    @FXML
    private VBox topicList;

    private Course course;

    public void addTopic(Node topic) {
        topicList.getChildren().add(topic);
    }

    public int getTopicListSize() {return topicList.getChildren().size();}

    public void setCourse(Course course) {
        if (this.course == null)
            this.course = course;
        else
            System.out.println("Can only set course once");
    }
}
