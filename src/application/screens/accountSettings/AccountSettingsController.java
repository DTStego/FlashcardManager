package application.screens.accountSettings;

import application.Main;
import application.User;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.util.Map;

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
    private Button confirmDeletionBtn;

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


    /** Reveals the "CONFIRM DELETION" button */
    @FXML
    void updateDeletionVisibility()
    {
        confirmDeletionBtn.setVisible(true);
        confirmDeletionBtn.setDisable(false);
    }

    /** Delete the account and return to the login page */
    @FXML
    void deleteAccount(ActionEvent event)
    {
        Main.userDatabase.deleteUser(Main.currentUser);
        Main.loadScreen(event, "screens/login/LoginScreen.fxml", "Flashcard Manager");
    }
}
