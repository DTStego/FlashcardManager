package application.screens.topic;

import application.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class TopicScreenController {

    @FXML
    private Label topicPageTitle;

    public void setTitle(String title) {
        topicPageTitle.setText(title);
    }

    @FXML
    void onBackBtnClicked(MouseEvent event) {
        Main.loadScreen(event, "screens/home/homeScreen.fxml", "Home");
    }
}
