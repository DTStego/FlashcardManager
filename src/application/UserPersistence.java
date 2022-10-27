package application;

import java.util.HashSet;

public interface UserPersistence
{
    void save(HashSet<User> user);
    HashSet<User> retrieve();
}
