package application;

import java.io.Serializable;

public class User implements Serializable
{
    // The password and securityAnswer variables should generally be passed into an encryptor
    // before being set to the instance variables. The encryptor will be provided in a later release.

    // Usernames should be unique!
    private String username;
    private String password;
    private String securityQuestion;
    private String securityAnswer;

    public User(String username, String password, String securityQuestion, String securityAnswer)
    {
        this.username = username;
        this.password = password;
        this.securityQuestion = securityQuestion;
        this.securityAnswer = securityQuestion;
    }

    /* TODO
    Implement Getter/Setter Methods
    Implement Hashcode/Equals method so Users are stored in a hashmap database
     */
}
