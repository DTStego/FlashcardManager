package application.screens.login;

import application.Main;
import application.User;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginScreenController {

    @FXML
    private Label ErrorMsg;
    @FXML
    private TextField passTextBox;
    @FXML
    private TextField usernameTextBox;
    @FXML
    void onForgotPassClick(MouseEvent event) {
        loadScreen(event, "../resetpage/resetpageUI.fxml", "Reset Page");
    }
    @FXML
    void onLoginClick(MouseEvent event) {
        if (verifyUser(event)) {
            //go to next page
        }
    }
    @FXML
    void onUsernameKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            if (verifyUser(event)) {
                //go to next page
            }
        }
    }
    @FXML
    void onPassKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            if (verifyUser(event)) {
                //go to next page
            }
        }
    }
    @FXML
    void onRegisterClick(MouseEvent event) {
        loadScreen(event, "../register/register.fxml", "Register");
    }
    private boolean verifyUser(Event event) {
        ErrorMsg.setOpacity(1);
        if (usernameTextBox.getText().equals("")) {
            ErrorMsg.setText("* Please enter a username");
            return false;
        }
        if (passTextBox.getText().equals("")) {
            ErrorMsg.setText("* Please enter a password");
            return false;
        }

        User user = Main.userDatabase.getUser(usernameTextBox.getText());

        if (user != null) {
            if (user.getPassword().equals(passTextBox.getText())) {
                System.out.println("* Login successful");
                loadScreen(event, "../home/homeScreen.fxml", "Home");
                ErrorMsg.setOpacity(0);
                return true;
            } else {
                ErrorMsg.setText("* Password is incorrect, please try again");
                return false;
            }
        } else {
            ErrorMsg.setText("* Username does not exist, please try again");
            return false;
        }
    }

    private void loadScreen(Event event, String fxmlLocation, String stageTitle) {
        try {
            Stage primaryStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource(fxmlLocation));
            Scene scene = new Scene(root, Main.screenWidth * Main.screenScale, Main.screenHeight * Main.screenScale);

            primaryStage.setTitle(stageTitle);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.out.println("LoginScreenController: Target FXML file not found");
        }
    }
}
