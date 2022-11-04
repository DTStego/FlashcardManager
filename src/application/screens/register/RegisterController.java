package application.screens.register;

import application.Main;
import application.User;
import application.managers.Notebook;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;

public class RegisterController {
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

    /** Return to login page*/
    @FXML
    void returnToLogin(ActionEvent event) throws IOException {
        //return to login page
        loadScreen(event, "../login/LoginScreen.fxml", "Login");
    }

    /** If all fields are not empty and the username is unique, add new user to userList */
    @FXML
    void registerUser(ActionEvent event) {
        if(isValid()) {
            Main.userDatabase.addUser(new User(usernameInput.getText(), passwordInput.getText(), security.getText(), secAnswer.getText(), new Notebook()));
            loadScreen(event, "../login/LoginScreen.fxml", "Login");
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
            System.out.println("RegisterController: Target FXML file not found");
        }
    }
    /** Returns true if username is unique and not in the database */
    boolean isValid() {
        //Reset errorMsg
        String errorMsg = "";
        // Invalid variable is incremented if any fields are empty or if the username is not unique
        int invalid = 0;
        // Check if username is not blank and is unique
        if (usernameInput.getLength() == 0 || Main.userDatabase.contains(usernameInput.getText())) {
            errorMsg += "* Invalid username, select a different username\n";
            invalid++;
        }
        // Check if password is not blank
        if (passwordInput.getLength()==0) {
            errorMsg += "* Password must be of length greater than 0\n";
            invalid++;
        }
        // Check if security question is not blank
        if (security.getLength()==0) {
            errorMsg += "* Please set a security question\n";
            invalid++;
        }
        // Check if security answer is not blank
        if (secAnswer.getLength()==0) {
            errorMsg += "* Please input the answer to the security question\n";
            invalid++;
        }
        // If invalid variable is not 0, then display error message and return false
        if(invalid!=0) {
            errorLBL.setText(errorMsg);
            return false;
        }
        // User is valid, return true
        else {
            errorLBL.setText("");   //this is purely optional and can be removed
            return true;
        }
        /* Old code without that checks if valid without sending error message if invalid */
//        if(usernameInput.getLength()!=0 && passwordInput.getLength()!=0 && security.getLength()!=0 && secAnswer.getLength()!=0) {
//            //Return true if username is not in userDatabase
//            return !Main.userDatabase.getUserList().containsKey(usernameInput.getText());
//        }
    }
}
