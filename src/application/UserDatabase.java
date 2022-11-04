package application;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;

// UserDatabase is initialized at the start of the program from an external file/system
public class UserDatabase implements Serializable
{
    private final HashMap<String, User> userList;

    // UserDataBase should be built at startup through a UserPersistence interface implementation
    public UserDatabase(HashMap<String, User> userList)
    {
        this.userList = userList;
    }

    /** Adds a User to the userList and saves the list using a UserPersistence implementation defined in Main */
    public void addUser(User user)
    {
        userList.put(user.getUsername(), user);
        Main.userPersistence.save(userList);
    }

    /** Deletes a User in the userList and saves the list using a UserPersistence implementation defined in Main */
    public void deleteUser(User user)
    {
        userList.remove(user.getUsername());
        Main.userPersistence.save(userList);
    }

    /** Method Overloading using a username instead of an actual User object */
    public void deleteUser(String username)
    {
        userList.remove(username.toLowerCase(Locale.ROOT));
        Main.userPersistence.save(userList);
    }

    public User getUser(String username)
    {
        return userList.get(username.toLowerCase(Locale.ROOT));
    }
    public boolean contains(String username)
    {
        return userList.containsKey(username.toLowerCase(Locale.ROOT));
    }

    /** Returns the userList in UserDatabase */
    // Be careful not to use the HashSet's add and remove methods (Use the ones provided by UserDatabase)
    public HashMap<String, User> getUserList()
    {
        return userList;
    }
}
