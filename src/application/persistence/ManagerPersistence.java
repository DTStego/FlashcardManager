package application.persistence;

import application.managers.Manager;

public interface ManagerPersistence
{
    void save(Manager manager);
    Manager retrieve();
}
