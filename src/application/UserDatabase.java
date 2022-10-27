package application;

import java.io.Serializable;
import java.util.HashSet;

// UserDatabase is initialized at the start of the program from an external file/system
public class UserDatabase implements Serializable
{
    /*
        Issue: If a User class in userList changes its username or other variable that hashCode()
               or equals() was dependent on, it would mess up the HashSet, i.e., using mutable/changeable
               variables for the equals() method is "iffy".

        Solution: Stop mutating the objects while they are in a hash set.
                  Remove them before you mutate them, put them back in later.
        If we change a user's username, password, etc., remove them from the userList first and add them after!
     */
    private HashSet<User> userList;

    public UserDatabase()
    {

    }

    public void addUser()
    {

    }

    public void deleteUser(User user)
    {

    }

    public void changePassword(User user, String newPassword)
    {

    }
}
