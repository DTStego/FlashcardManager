package application.screens.accountSettings;

import application.Main;
import application.User;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
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
    private Label errorLabel;

    private String errorMessage = "";

    @FXML
    void onCancelClick(MouseEvent event) {
        Main.loadBigScreen(event,"screens/home/homeScreen.fxml", "Home");
    }

    /** Enable the security answer text-box if input is detected in the security question text-box */
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
        Main.loadSmallScreen(event, "screens/login/LoginScreen.fxml", "Flashcard Manager");
    }

    @FXML
    void applyBtn(ActionEvent event)
    {
        errorMessage = "";

        boolean usernameViolation = hasUsernameViolation();
        boolean passwordViolation = hasPasswordViolation();
        boolean securityViolation = hasSecurityViolation();

        if (!usernameViolation && !passwordViolation && !securityViolation)
        {
            boolean userChanged = false;
            if (!usernameField.getText().isEmpty())
            {
                Main.userDatabase.deleteUser(Main.currentUser);
                Main.currentUser.setUsername(usernameField.getText());
                Main.userDatabase.addUser(Main.currentUser);
                userChanged = true;
            }

            if (!newPassField.getText().isEmpty() && !confirmPassField.getText().isEmpty())
            {
                Main.currentUser.setPassword(newPassField.getText());
                Main.userDatabase.updateUser(Main.currentUser);
                userChanged = true;
            }

            if (!secQuestionField.getText().isEmpty() && !secAnswerField.getText().isEmpty())
            {
                Main.currentUser.setSecurityQuestion(secQuestionField.getText());
                Main.currentUser.setSecurityAnswer(secAnswerField.getText());
                Main.userDatabase.updateUser(Main.currentUser);
                userChanged = true;
            }

            if (userChanged)
            {
                try
                {
                    Thread.sleep(750);
                } catch (InterruptedException ex)
                {
                    ex.printStackTrace();
                }

                Main.loadSmallScreen(event, "screens/login/LoginScreen.fxml", "Flashcard Manager");
            }
        } else
        {
            errorLabel.setText(errorMessage);
        }
    }

    private boolean hasUsernameViolation()
    {
        if (!usernameField.getText().isEmpty() && !Main.userDatabase.contains(usernameField.getText()))
        {
            return false;
        } else if (Main.userDatabase.contains(usernameField.getText()))
        {
            errorMessage += "Username is taken. Choose another one!\n";
            return true;
        } else
        {
            return false;
        }
    }

    private boolean hasPasswordViolation()
    {
        String newPassword = newPassField.getText();
        String confirmationPassword = confirmPassField.getText();

        if (newPassword.isEmpty() && confirmationPassword.isEmpty())
            return false;

        if (!newPassword.isEmpty() && !confirmationPassword.isEmpty())
        {
            if (newPassword.equals(confirmationPassword))
            {
                return false;
            } else
            {
                errorMessage += "Passwords do not match\n";
                return true;
            }
        }

        errorMessage += "No password input.\n";
        return true;
    }

    private boolean hasSecurityViolation()
    {
        if (!secQuestionField.getText().isEmpty())
        {
            if (!secAnswerField.getText().isEmpty())
            {
                return false;
            } else
            {
                errorMessage += "No security answer detected.";
                return true;
            }
        }

        if (secQuestionField.getText().isEmpty() && !secAnswerField.getText().isEmpty())
        {
            errorMessage += "Did not detect a security question. An answer was detected...\n";
            return true;
        }

        return false;
    }
}
