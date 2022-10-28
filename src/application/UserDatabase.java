package application;

import java.io.Serializable;
import java.util.HashSet;

// UserDatabase is initialized at the start of the program from an external file/system
public class UserDatabase implements Serializable
{
    private final HashSet<User> userList;

    // UserDataBase should be built at startup through a UserPersistence interface implementation
    public UserDatabase(HashSet<User> userList)
    {
        this.userList = userList;
    }

    /** Adds a User to the userList and saves the list using a UserPersistence implementation defined in Main */
    public void addUser(User user)
    {
        userList.add(user);
        Main.userPersistence.save(userList);
    }

    /** Deletes a User in the userList and saves the list using a UserPersistence implementation defined in Main */
    public void deleteUser(User user)
    {
        userList.remove(user);
        Main.userPersistence.save(userList);
    }

    /** Returns the userList in UserDatabase */
    // Be careful not to use the HashSet's add and remove methods (Use the ones provided by UserDatabase)
    public HashSet<User> getUserList()
    {
        return userList;
    }
}
