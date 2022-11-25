package application.screens.login;

import application.Main;
import application.User;
import application.persistence.UserSerialize;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class LoginScreenController
{
    @FXML
    private Label ErrorMsg;
    @FXML
    private TextField passTextBox;
    @FXML
    private TextField usernameTextBox;

    /**
     * Activates when a user clicks on the "Forgot password?" prompt on the login screen.
     * Switches the scene to the reset page.
     */
    @FXML
    void onForgotPassClick(MouseEvent event)
    {
        Main.loadScreen(event, "screens/resetpage/resetpageUI.fxml", "Reset Page");
    }

    /**
     * Activates when a user clicks on the register prompt on the login screen.
     * Switches the scene to the register page.
     */
    @FXML
    void onRegisterClick(MouseEvent event)
    {
        Main.loadScreen(event, "screens/register/register.fxml", "Register");
    }

    @FXML
    void onLoginClick(MouseEvent event)
    {
        if (verifyUser(event))
        {
            //go to next page
        }
    }

    /**
     * Activates when a user presses a button while focused on the username text-box.
     * Attempts to verify user through the verifyUser() method.
     */
    @FXML
    void onUsernameKeyPress(KeyEvent event)
    {
        if (event.getCode() == KeyCode.ENTER)
        {
            verifyUser(event);
        }
    }

    /**
     * Activates when a user presses a button while focused on the password text-box.
     * Attempts to verify user through the verifyUser() method.
     */
    @FXML
    void onPassKeyPress(KeyEvent event)
    {
        if (event.getCode() == KeyCode.ENTER)
        {
            verifyUser(event);
        }
    }

    /**
     * Attempts to verify the credentials from the username and password text-boxes
     * @param event Event object used to locate the primary stage for Main.java's loadScreen() method.
     * @return boolean dependent on if the user was verified in the database or not.
     */
    private boolean verifyUser(Event event)
    {
        ErrorMsg.setOpacity(1);
        if (usernameTextBox.getText().equals(""))
        {
            ErrorMsg.setText("* Please enter a username");
            return false;
        }
        if (passTextBox.getText().equals(""))
        {
            ErrorMsg.setText("* Please enter a password");
            // Focuses on the password text-box if none is detected and a username was entered.
            passTextBox.requestFocus();
            return false;
        }

        // Attempts to locate the user in the database
        User user = Main.userDatabase.getUser(usernameTextBox.getText());

        if (user != null)
        {
            // If the user is verified, advance to the home screen, otherwise, display error messages.
            if (user.getPassword().equals(passTextBox.getText()))
            {
                Main.currentUser = user;
                Main.loadScreen(event, "screens/home/homeScreen.fxml", "Home");
                ErrorMsg.setOpacity(0);
                return true;
            } else
            {
                ErrorMsg.setText("* Password is incorrect, please try again");
                return false;
            }
        } else
        {
            ErrorMsg.setText("* Username does not exist, please try again");
            return false;
        }
    }

    /** Opens the save folder location */
    @FXML
    void openSaveLocation(MouseEvent event)
    {
        try
        {
            Desktop.getDesktop().open(new File(UserSerialize.saveFolder));
        } catch (IOException ioException)
        {
            System.out.println("Critical Error: No Save File: " + ioException.getMessage());
        }
    }
}
