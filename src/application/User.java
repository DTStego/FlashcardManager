package application;

import application.managers.Notebook;
import edu.sjsu.yazdankhah.crypto.util.PassUtil;

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
        PassUtil security = new PassUtil();

        this.username = username.toLowerCase(Locale.ROOT);
        this.password = security.encrypt(password);
        this.securityQuestion = securityQuestion;
        this.securityAnswer = security.encrypt(securityAnswer);
        this.notebook = notebook;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username.toLowerCase();
    }

    /**
     * @return String that is decrypted using a PassUtil object
     */
    public String getPassword()
    {
        PassUtil security = new PassUtil();
        return security.decrypt(password);
    }

    public void setPassword(String password)
    {
        PassUtil security = new PassUtil();
        this.password = security.encrypt(password);
    }

    public String getSecurityQuestion()
    {
        return securityQuestion;
    }

    public void setSecurityQuestion(String securityQuestion)
    {
        this.securityQuestion = securityQuestion;
    }

    /**
     * @return String that is decrypted using a PassUtil object
     */
    public String getSecurityAnswer()
    {
        PassUtil security = new PassUtil();
        return security.decrypt(securityAnswer);
    }

    public void setSecurityAnswer(String securityAnswer)
    {
        PassUtil security = new PassUtil();
        this.securityAnswer = security.encrypt(securityAnswer);
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
        return getUsername().equals(user.username) && getPassword().equals(user.password)
                && getSecurityQuestion().equals(user.securityQuestion) && getSecurityAnswer().equals(user.securityAnswer);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(username, password, securityQuestion, securityAnswer);
    }
}
