package application.screens.resetpage;

import application.Main;
import application.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class resetPageController
{
    private String securityQuestion, username;
    private User currentUser;

    @FXML
    private Label errormsg;
    @FXML
    private Text securityQ;

    @FXML
    private TextField userAnswer;

    @FXML
    private TextField userInput;

    @FXML
    private TextField newPass;

    @FXML
    private TextField newPassConfirm;

    @FXML
    private VBox passCreation;

    @FXML
    void returnToLogin(ActionEvent event)
    {
        // Return to the login screen
        Main.loadSmallScreen(event, "screens/login/LoginScreen.fxml", "Login");
    }

    /** Attempts to retrieve the user from a database to then display the security question */
    @FXML
    void submitUsername(ActionEvent event)
    {
        User user = Main.userDatabase.getUser(userInput.getText());

        if (user != null)
        {
            currentUser = user;
            securityQuestion = currentUser.getSecurityQuestion();
            securityQ.setText(securityQuestion);
            removeError();
        } else
        {
            displayError("* Username does not exist, please try again.");
        }
    }

    /**
     * Verifies the inputted security answer and shows the text-boxes to reset the password.
     * Otherwise, display error messages that explain the issues.
     */
    @FXML
    void submitAnswer(ActionEvent event)
    {
        String answer = userAnswer.getText();
        if (currentUser != null) {
            if (answer.equals(currentUser.getSecurityAnswer()))
            {
                // Display password creation
                passCreation.setOpacity(1);
            } else {
                displayError("* The answer is incorrect, please try again.");
            }
        }
        else {
            displayError("* User not found, please enter a valid username and click \"Find User\"");
        }
    }


    /**
     * Verifies that the new password and the confirmation of the new password match
     * and reset the user's password. Then, return to the login screen.
     */
    @FXML
    void submitNewPass(ActionEvent event)
    {
        String newPassString = newPass.getText();
        String newPassConfirmString = newPassConfirm.getText();

        if (newPassString.equals(newPassConfirmString) && newPassString.length()!=0)
        {
            // Can add requirements for password
            currentUser.setPassword(newPassString);
            Main.userDatabase.updateUser(currentUser);
            removeError();
            returnToLogin(event);
        } else
        {
            // Display error, passwords don't match.
            displayError("* Passwords do not match or a new password was not inputted, please try again.");
        }
    }

    void displayError(String error)
    {
        errormsg.setOpacity(1);
        errormsg.setText(error);
    }

    void removeError()
    {
        errormsg.setOpacity(0);
    }
}
