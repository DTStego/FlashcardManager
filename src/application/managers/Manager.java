package application.managers;

import java.io.Serializable;
import java.util.List;

public interface Manager extends Serializable
{
    boolean add(Manager manager);
    boolean delete(Manager manager);
    boolean rename(Manager manager, String newName);
    List<? extends Manager> display();
}
