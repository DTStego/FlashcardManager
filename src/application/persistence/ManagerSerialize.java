package application.persistence;

import application.managers.Manager;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;

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

    /**
     * Creates a Folder/File structure in the Application package if none is found
     * Saves to ../My Documents/FlashcardManager/saves/notebook.txt. Does not overwrite.
     * @return true if file was created; false if file already exists
     */
    @Override
    public boolean createSaveFile()
    {
        // Attempt to create the directory "FlashcardManager/saves"
        // Cannot combine with .createNewFile statement because method creates files instead
        File file = new File(saveFolder);
        file.mkdirs();

        // Adjust file pointer to the actual .txt file (Which stores notebook database)
        file = new File(saveFolder.concat(File.separatorChar + "notebook.txt"));

        // Return true if notebook.txt was created through the createNewFile() method
        boolean success = false;

        try
        {
            success = file.createNewFile();
        } catch (IOException ex)
        {
            ex.printStackTrace();
        }

        return success;
    }
}
