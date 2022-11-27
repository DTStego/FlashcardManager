package application.screens.home;

import application.Main;
import application.managers.Course;
import application.managers.Topic;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TopicPaneController {

    @FXML
    private Button topicBtn;

    @FXML
    private TextField editNameField;

    @FXML
    private Button saveBtn;

    @FXML
    private Pane topicPane;

    private Course course;
    private Topic topic;

    public Pane getTopicPane() {return topicPane;}

    public void setTopicAndCourse(Topic topic, Course course) {
        if (this.topic == null && this.course == null) {
            this.topic = topic;
            this.course = course;
        } else {
            System.out.println("Can only set topic and course once");
        }

    }

    private void enableEdit() {
        editNameField.setDisable(false);
        editNameField.setText(topicBtn.getText());
        editNameField.setVisible(true);
        editNameField.requestFocus();
        saveBtn.setDisable(false);
        saveBtn.setVisible(true);
    }

    private void disableEdit() {
        editNameField.setDisable(true);
        editNameField.setVisible(false);
        saveBtn.setDisable(true);
        saveBtn.setVisible(false);
    }

    public void setTopicName(String name) {topicBtn.setText(name);}

    private void attemptNameSave() {
        String text = editNameField.getText();
        if (text.equals("")) {
            //error empty name field
        } else {
            setTopicName(text);
            this.topic.setName(text);
        }
        disableEdit();
        Main.userDatabase.updateUser(Main.currentUser);
    }
    @FXML
    void initialize() {disableEdit();}

    @FXML
    void onDelTopicClicked(MouseEvent event) {
        course.delete(topic);
        ((VBox) topicPane.getParent()).getChildren().remove(topicPane);
        Main.userDatabase.updateUser(Main.currentUser);
    }

    @FXML
    void onEditBtnClicked(MouseEvent event) {enableEdit();}

    @FXML
    void onTopicBtnClicked(MouseEvent event) {
        //open topic page
    }

    @FXML
    void onEditKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            attemptNameSave();
        } else if (event.getCode() == KeyCode.ESCAPE) {
            disableEdit();
        }
    }

    @FXML
    void onSaveBtnClicked(MouseEvent event) {
        attemptNameSave();
    }
}
