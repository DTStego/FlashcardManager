package application;

import java.io.Serializable;
import java.util.HashSet;

// UserDatabase is initialized at the start of the program from an external file/system
public class UserDatabase implements Serializable
{
    private HashSet<User> userList;

    public UserDatabase()
    {

    }

    public void addUser(User user)
    {
        userList.add(user);
    }

    public void deleteUser(User user)
    {
        userList.remove(user);
    }
}
