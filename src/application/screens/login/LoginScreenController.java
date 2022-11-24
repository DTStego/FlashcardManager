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
    @FXML
    void onForgotPassClick(MouseEvent event)
    {
        Main.loadScreen(event, "screens/resetpage/resetpageUI.fxml", "Reset Page");
    }
    @FXML
    void onLoginClick(MouseEvent event)
    {
        if (verifyUser(event))
        {
            //go to next page
        }
    }
    @FXML
    void onUsernameKeyPress(KeyEvent event)
    {
        if (event.getCode() == KeyCode.ENTER)
        {
            if (verifyUser(event))
            {
                //go to next page
            }
        }
    }
    @FXML
    void onPassKeyPress(KeyEvent event)
    {
        if (event.getCode() == KeyCode.ENTER)
        {
            if (verifyUser(event))
            {
                //go to next page
            }
        }
    }
    @FXML
    void onRegisterClick(MouseEvent event)
    {
        Main.loadScreen(event, "screens/register/register.fxml", "Register");
    }
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
            return false;
        }

        User user = Main.userDatabase.getUser(usernameTextBox.getText());

        if (user != null)
        {
            if (user.getPassword().equals(passTextBox.getText()))
            {
                System.out.println("* Login successful");
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
