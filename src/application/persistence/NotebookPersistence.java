package application.persistence;

import application.managers.Notebook;

public interface NotebookPersistence
{
    void save(Notebook manager);
    Notebook retrieve();
}
