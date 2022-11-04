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
import java.util.HashMap;

public class LoginScreenController {

    @FXML
    private Label ErrorMsg;

    @FXML
    private Hyperlink forgotPassBtn;

    @FXML
    private Hyperlink registerBtn;

    @FXML
    private Button loginBtn;

    @FXML
    private TextField passTextBox;

    @FXML
    private TextField usernameTextBox;

    @FXML
    void onForgotPassClick(MouseEvent event) {
        loadScreen(event, "screens/resetpage/resetpageUI.fxml");
    }

    @FXML
    void onLoginClick(MouseEvent event) {
        if (verifyUser()) {
            //go to next page
        }
    }

    @FXML
    void onUsernameKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            if (verifyUser()) {
                //go to next page
            }
        }
    }

    @FXML
    void onPassKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            if (verifyUser()) {
                //go to next page
            }
        }
    }

    @FXML
    void onRegisterClick(MouseEvent event) {
        loadScreen(event, "screens/register/register.fxml");
    }

    private boolean verifyUser() {
        ErrorMsg.setOpacity(1);
        if (usernameTextBox.getText().equals("")) {
            ErrorMsg.setText("Please enter a username");
            return false;
        }
        if (passTextBox.getText().equals("")) {
            ErrorMsg.setText("Please enter a password");
            return false;
        }

        HashMap<String, User> userList = Main.userDatabase.getUserList();
        User user = userList.get(usernameTextBox.getText());

        if (user != null) {
            if (user.getPassword().equals(passTextBox.getText())) {
                System.out.println("Login successful");
                ErrorMsg.setOpacity(0);
                return true;
            } else {
                ErrorMsg.setText("Password is incorrect");
                return false;
            }
        } else {
            ErrorMsg.setText("Username does not exist");
            return false;
        }
    }

    private void loadScreen(Event event, String fxmlLocation) {
        try {
            Stage primaryStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            Parent root = FXMLLoader.load(Main.class.getResource(fxmlLocation));
            Scene scene = new Scene(root, Main.screenWidth * Main.screenScale, Main.screenHeight * Main.screenScale);

            primaryStage.setTitle("Register");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.out.println("FXML file not found");
        }
    }
}
