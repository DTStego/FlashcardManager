package application.screens.resetpage;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;
import java.util.HashMap;

import application.Main;
import application.User;

public class resetPageController {
    private String securityQuestion, username;
    private User currentUser;

    @FXML
    private Label errormsg;

    @FXML
    private Button back;

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

    //get username from input and if exists, set equal to var in controller
    @FXML
    void submitUsername(ActionEvent event) {
        username = userInput.getText();
        HashMap<String, User> userList = Main.userDatabase.getUserList(); //Currently main database is empty
        User user = userList.get(username);

        if (user != null) {
            currentUser = user;
            securityQuestion = currentUser.getSecurityQuestion();
            securityQ.setText(securityQuestion);
            removeError();
        } else {
            displayError("Username is not valid.");
        }

        
    }

    @FXML
    void returnToLogin(ActionEvent event) {
        //return to loginpage
        loadScreen(event, "../login/LoginScreen.fxml");
    }

    //take answer input, verify, then show password area
    @FXML
    void submitAnswer(ActionEvent event) {
        String answer = userAnswer.getText();
        if (answer.equals(currentUser.getSecurityAnswer())) {
            //Display password creation
            passCreation.setOpacity(1);
        } else {
            displayError("Answer is incorrect.");
        }
    }

    //verify new password equals each other, set password on user, return to login page
    @FXML
    void submitNewPass(ActionEvent event) {
        String newPassString = newPass.getText();
        String newPassConfirmString = newPassConfirm.getText();

        if (newPassString.equals(newPassConfirmString)) { //can add requirements for password
            currentUser.setPassword(newPassString);
            removeError();
            returnToLogin(event);
        } else {
            //display error, passwords don't match
            displayError("Passwords don't match.");
        }
    }

    void displayError(String error) {
        errormsg.setOpacity(1);
        errormsg.setText(error);
    }
    
    void removeError() {
        errormsg.setOpacity(0);
    }

    private void loadScreen(Event event, String fxmlLocation) {
        try {
            Stage primaryStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource(fxmlLocation));
            Scene scene = new Scene(root, Main.screenWidth * Main.screenScale, Main.screenHeight * Main.screenScale);

            primaryStage.setTitle("Register");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.out.println("FXML file not found");
        }
    }

}
