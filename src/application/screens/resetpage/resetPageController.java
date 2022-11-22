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

    // Get username from input and if exists, set equal to var in controller
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

    @FXML
    void returnToLogin(ActionEvent event)
    {
        // Return to the login screen
        Main.loadScreen(event, "screens/login/LoginScreen.fxml", "Login");
    }

    //take answer input, verify, then show password area
    @FXML
    void submitAnswer(ActionEvent event)
    {
        String answer = userAnswer.getText();
        if (currentUser!=null) {
            if (answer.equals(currentUser.getSecurityAnswer()))
            {
                //Display password creation
                passCreation.setOpacity(1);
            } else {
                displayError("* The answer is incorrect, please try again.");
            }
        }
        else {
            displayError("* User not found, please enter a valid username and click \"Find User\"");
        }
    }

    // Verify new password equals each other, set password on user, return to login page
    @FXML
    void submitNewPass(ActionEvent event)
    {
        String newPassString = newPass.getText();
        String newPassConfirmString = newPassConfirm.getText();

        if (newPassString.equals(newPassConfirmString) && newPassString.length()!=0)
        {   // Can add requirements for password
            currentUser.setPassword(newPassString);
            Main.userDatabase.updateUser(currentUser);
            removeError();
            returnToLogin(event);
        } else
        {
            //display error, passwords don't match
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
