package application.screens.register;

import application.Main;
import application.User;
import application.managers.Notebook;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class RegisterController
{
    @FXML
    private TextField usernameInput;
    @FXML
    private TextField passwordInput;
    @FXML
    private TextField security;
    @FXML
    private TextField secAnswer;
    @FXML
    private Label errorLBL;

    /** Return to the login page */
    @FXML
    void returnToLogin(ActionEvent event)
    {
        // Return to login page
        Main.loadSmallScreen(event, "screens/login/LoginScreen.fxml", "Login");
    }

    /** If all fields are not empty and the username is unique, add new user to userList */
    @FXML
    void registerUser(ActionEvent event)
    {
        if(isValid())
        {
            Main.userDatabase.addUser(new User(usernameInput.getText(), passwordInput.getText(), security.getText(), secAnswer.getText(), new Notebook()));
            Main.loadSmallScreen(event, "screens/login/LoginScreen.fxml", "Login");
        }
    }

    /** Returns true if username is unique and not in the database */
    boolean isValid()
    {
        // Reset errorMsg
        String errorMsg = "";

        // Invalid variable is incremented if any fields are empty or if the username is not unique
        int invalid = 0;

        // Check if username is not blank and is unique
        if (usernameInput.getLength() == 0 || Main.userDatabase.contains(usernameInput.getText()))
        {
            errorMsg += "* Invalid username, select a different username\n";
            invalid++;
        }

        // Check if password field is not blank
        if (passwordInput.getLength() == 0)
        {
            errorMsg += "* Password must be of length greater than 0\n";
            invalid++;
        }

        // Check if security question is not blank
        if (security.getLength() == 0)
        {
            errorMsg += "* Please set a security question\n";
            invalid++;
        }

        // Check if security answer is not blank
        if (secAnswer.getLength() == 0)
        {
            errorMsg += "* Please input the answer to the security question\n";
            invalid++;
        }

        // If invalid variable is not 0, then display error message and return false
        if (invalid != 0)
        {
            errorLBL.setText(errorMsg);
            return false;
        }
        // User is valid, return true
        else
        {
            return true;
        }
    }
}
