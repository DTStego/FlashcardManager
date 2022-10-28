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

    /*
        Issue: If a User class in userList changes its username or other variable that hashCode()
               or equals() was dependent on, it would mess up the HashSet, i.e., using mutable/changeable
               variables for the equals() method is "iffy".

        Solution: Stop mutating the objects while they are in a hash set.
                  Remove them before you mutate them, put them back in later.
        If we change a user's username, password, etc., remove them from the userList first and add them after!
     */

    public User(String username, String password, String securityQuestion, String securityAnswer)
    {
        this.username = username;
        this.password = password;
        this.securityQuestion = securityQuestion;
        this.securityAnswer = securityAnswer;
    }

    /* TODO
    Implement Hashcode/Equals method so Users are stored in a hashmap database
    */

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getSecurityQuestion()
    {
        return securityQuestion;
    }

    public void setSecurityQuestion(String securityQuestion)
    {
        this.securityQuestion = securityQuestion;
    }

    public String getSecurityAnswer()
    {
        return securityAnswer;
    }

    public void setSecurityAnswer(String securityAnswer)
    {
        this.securityAnswer = securityAnswer;
    }
}
