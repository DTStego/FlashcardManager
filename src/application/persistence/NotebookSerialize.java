package application.persistence;

import application.managers.Notebook;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;

public class NotebookSerialize implements NotebookPersistence, ObjectSerialize
{
    private final String saveFolder = FileSystemView.getFileSystemView().getDefaultDirectory().getPath()
            .concat(File.separatorChar + "FlashcardManager" + File.separatorChar + "saves");

    /**
     * Stores the notebook in a "saves" folder in a user's document folder through serialization.
     * Creates a "saves" folder and "notebook.txt" file if none are found.
     * @param manager Notebook to be stored in notebook.txt
     */
    @Override
    public void save(Notebook manager)
    {
        // Point to the notebook.txt file in the save folder and if it doesn't exist create it
        File saveFile = new File(saveFolder.concat(File.separatorChar + "notebook.txt"));
        if (!saveFile.exists())
        {
            createSaveFile();
        }

        try
        {
            // Write the Notebook object into the notebook.txt file
            ObjectOutputStream outputStream = new ObjectOutputStream(Files.newOutputStream(saveFile.toPath()));
            outputStream.writeObject(manager);
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * Checks if a new save file can be created. If createSaveFile() returns true, indicates there is no saved notebook.
     * The method will then return an empty list. Otherwise, read from the byte stream if it exists in the file.
     * @return a Notebook object that contains a user's notebook with courses and topics
     */
    @Override
    public Notebook retrieve()
    {
        // If the creation of a save file in the save folder was successful,
        // that indicates there is no saved data. Therefore, return an empty Notebook using the empty constructor.
        if (createSaveFile())
        {
            return new Notebook();
        }

        // Point to a notebook text file that should exist (createSaveFile() returned false)
        File saveFile = new File(saveFolder.concat(File.separatorChar + "notebook.txt"));

        // Double-check the existence of the save file and confirm it actually has data
        if (saveFile.exists() && saveFile.length() != 0)
        {
            try
            {
                // Read the data from the file and return the Notebook data
                ObjectInputStream inputStream = new ObjectInputStream(Files.newInputStream(saveFile.toPath()));
                return (Notebook) inputStream.readObject();
            } catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        // Return a new notebook if there is a notebook.txt file, but it is blank.
        return new Notebook();
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

        // Adjust file pointer to the actual .txt file (Which stores the notebook database)
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
