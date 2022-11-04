package application;

import application.managers.Notebook;

import java.io.Serializable;
import java.util.Locale;
import java.util.Objects;

public class User implements Serializable
{
    // The password and securityAnswer variables should generally be passed into an encryptor
    // before being set to the instance variables. The encryptor will be provided in a later release.

    // Usernames should be unique!
    private String username;
    private String password;
    private String securityQuestion;
    private String securityAnswer;
    private final Notebook notebook;

    /*
        Issue: If a User class in userList changes its username or other variable that hashCode()
               or equals() was dependent on, it would mess up the HashSet, i.e., using mutable/changeable
               variables for the equals() method is "iffy".

        Solution: Stop mutating the objects while they are in a hash set.
                  Remove them before you mutate them, put them back in later.
        If we change a user's username, password, etc., remove them from the userList first and add them after!
     */

    public User(String username, String password, String securityQuestion, String securityAnswer, Notebook notebook)
    {
        this.username = username.toLowerCase(Locale.ROOT);
        this.password = password;
        this.securityQuestion = securityQuestion;
        this.securityAnswer = securityAnswer;
        this.notebook = notebook;
    }

    public String getUsername()
    {
        if (username.length() == 1)
        {
            return username.toUpperCase(Locale.ROOT);
        }
        else
        {
            return username.substring(0, 1).toUpperCase(Locale.ROOT) + username.substring(1);
        }
    }

    public void setUsername(String username)
    {
        this.username = username.toLowerCase();
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

    public Notebook getNotebook()
    {
        return notebook;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return username.equals(user.username) && password.equals(user.password)
                && securityQuestion.equals(user.securityQuestion) && securityAnswer.equals(user.securityAnswer);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(username, password, securityQuestion, securityAnswer);
    }
}
