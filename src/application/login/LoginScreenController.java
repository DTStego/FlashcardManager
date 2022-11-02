package application.login;

import application.Main;
import application.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.util.HashMap;

public class LoginScreenController {

    @FXML
    private Label ErrorMsg;

    @FXML
    private Button backBtn;

    @FXML
    private Hyperlink forgotPassBtn;

    @FXML
    private Button loginBtn;

    @FXML
    private TextField passTextBox;

    @FXML
    private TextField usernameTextBox;

    @FXML
    void onBackBtnClick(MouseEvent event) {

    }

    @FXML
    void onForgotPassClick(MouseEvent event) {

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

}
