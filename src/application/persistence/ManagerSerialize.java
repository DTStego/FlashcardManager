package application.persistence;

import application.managers.Manager;

import javax.swing.filechooser.FileSystemView;
import java.io.File;

public class ManagerSerialize implements ManagerPersistence, ObjectSerialize
{
    // Path of save folder in documents, e.g., (For Windows) "../My Documents/FlashcardManager/saves/"
    private final String saveFolder = FileSystemView.getFileSystemView().getDefaultDirectory().getPath()
            .concat(File.separatorChar + "FlashcardManager" + File.separatorChar + "saves");

    @Override
    public void save(Manager manager)
    {

    }

    @Override
    public Manager retrieve()
    {
        return null;
    }

    @Override
    public boolean createSaveFile()
    {
        return false;
    }
}
