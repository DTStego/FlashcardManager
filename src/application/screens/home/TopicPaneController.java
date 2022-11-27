package application.screens.home;

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

    public Pane getTopicPane() {return topicPane;}
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

    private void attemptSave() {
        String text = editNameField.getText();
        if (text.equals("")) {
            //error empty name field
        } else {
            topicBtn.setText(text);
        }
        disableEdit();
    }
    @FXML
    void initialize() {disableEdit();}

    @FXML
    void onDelTopicClicked(MouseEvent event) {
        ((VBox) topicPane.getParent()).getChildren().remove(topicPane);
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
            attemptSave();
        } else if (event.getCode() == KeyCode.ESCAPE) {
            disableEdit();
        }
    }

    @FXML
    void onSaveBtnClicked(MouseEvent event) {
        attemptSave();
    }
}
