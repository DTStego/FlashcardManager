package application.screens.accountSettings;

import application.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class AccountSettingsController {

    @FXML
    private Button cancelBtn;

    @FXML
    void onCancelClick(MouseEvent event) {
        Main.loadScreen(event,"screens/home/homeScreen.fxml", "Home");
    }

}
