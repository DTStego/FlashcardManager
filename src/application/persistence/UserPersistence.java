package application.persistence;

import application.User;

import java.util.HashMap;
import java.util.HashSet;

public interface UserPersistence
{
    void save(HashMap<String, User> user);
    HashMap<String, User> retrieve();
}
