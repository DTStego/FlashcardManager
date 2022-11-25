package application.screens.home;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TopicPaneController {

    @FXML
    private TextField editNameField;

    @FXML
    private Button saveBtn;

    @FXML
    private Pane topicPane;

    private void enableEdit() {
        editNameField.setDisable(false);
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
    @FXML
    void initialize() {
        disableEdit();
    }

    @FXML
    void onDelTopicClicked(MouseEvent event) {
        ((VBox) topicPane.getParent()).getChildren().remove(topicPane);
    }

    @FXML
    void onEditBtnClicked(MouseEvent event) {
        enableEdit();
    }

    @FXML
    void onTopicBtnClicked(MouseEvent event) {
        //open topic page
    }

    @FXML
    void onEditKeyPress(KeyEvent event) {

    }

    @FXML
    void onSaveBtnClicked(MouseEvent event) {

    }
}
