package application;

import java.io.Serializable;
import java.util.HashSet;

// UserDatabase is initialized at the start of the program from an external file/system
public class UserDatabase implements Serializable
{
    private HashSet<User> userList;

    // UserDataBase should be built at startup through a UserPersistence interface implementation
    public UserDatabase(HashSet<User> userList)
    {
        this.userList = userList;
    }

    public void addUser(User user)
    {
        userList.add(user);

        /* TODO
           Add code to serialize or save the database after a new user is added to the database
         */
    }

    public void deleteUser(User user)
    {
        userList.remove(user);

        /* TODO
           Add code to serialize or save the database after a user is deleted from the database
         */
    }

    public HashSet<User> getUserList()
    {
        return userList;
    }
}
