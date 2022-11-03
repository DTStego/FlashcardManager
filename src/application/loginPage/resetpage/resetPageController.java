package application.loginPage.resetpage;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.text.Text;

import java.util.HashMap;

import application.Main;
import application.User;
import application.UserDatabase;

public class resetPageController {
    private String securityQuestion, username;
    private User currentUser;
    private UserDatabase database; 
    private HashMap<String, User> userList = database.getUserList();

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

    //get username from input and if exists, set equal to var in controller
    @FXML
    void submitUsername(ActionEvent event) {
        username = userInput.getText();

        //securityQ.setText("username: " + username); (Test input)
        
        //if username exists, recieve securityQuestion
        //else send error
        if (userList.get(username) != null) {
            currentUser = userList.get(username);
            securityQuestion = currentUser.getSecurityQuestion();
            securityQ.setText(securityQuestion);
        } else {
            //display error of username doesn't exist
        }

        
    }

    @FXML
    void returnToLogin(ActionEvent event) {

    }

    //take answer input, verify, then show password area
    @FXML
    void submitAnswer(ActionEvent event) {
        String answer = userAnswer.getText();
        if (answer.equals(currentUser.getSecurityAnswer())); //---> display new password creation
    }

    //verify new password equals each other, set password on user, return to login page
    @FXML
    void submitNewPass(ActionEvent event) {
        String newPassString = newPass.getText();
        String newPassConfirmString = newPassConfirm.getText();

        if (newPassString.equals(newPassConfirmString)) { //can add requirements for password
            currentUser.setPassword(newPassString);
            returnToLogin(event);
        }
    }

}
