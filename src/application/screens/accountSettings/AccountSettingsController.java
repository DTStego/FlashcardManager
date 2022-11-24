package application.screens.accountSettings;

import application.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class AccountSettingsController
{
    @FXML
    private Button cancelBtn;

    @FXML
    private Button applyBtn;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField newPassField;

    @FXML
    private PasswordField confirmPassField;

    @FXML
    private TextField secQuestionField;

    @FXML
    private Label secAnswerLbl;

    @FXML
    private TextField secAnswerField;

    @FXML
    void onCancelClick(MouseEvent event) {
        Main.loadScreen(event,"screens/home/homeScreen.fxml", "Home");
    }

    @FXML
    void revealSecurityAnswerElements()
    {
        if (secQuestionField.getText().isEmpty())
        {
            secAnswerLbl.setDisable(true);
            secAnswerField.setDisable(true);
        }
        else
        {
            secAnswerLbl.setDisable(false);
            secAnswerField.setDisable(false);
        }
    }
}
