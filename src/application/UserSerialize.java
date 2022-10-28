package application;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;

public class UserSerialize implements UserPersistence
{
    /**
     * Stores the userList in a "saves" folder through serialization
     * Creates a "saves" folder and "UserList.txt" file if none are found
     * @param userList HashSet<User> userList to be stored by serializing
     */
    @Override
    public void save(HashSet<User> userList)
    {

    }

    @Override
    public HashSet<User> retrieve()
    {
        return null;
    }

    /**
     * Creates a Folder/File structure in the Application package if none is found
     * Saves to ../My Documents/FlashcardManager/saves/userList.txt
     * @return true if file was created; false if file already exists
     */
    @Override
    public boolean createSaveFile() throws IOException
    {
        // Path of save folder in documents, e.g., (For Windows) "../My Documents/FlashcardManager/saves/"
        String saveLocation = FileSystemView.getFileSystemView().getDefaultDirectory().getPath()
                .concat(File.separatorChar + "FlashcardManager" + File.separatorChar + "saves");

        File file = new File(saveLocation);

        // Attempt to create the directory "FlashcardManager/saves"
        // Cannot combine with .createNewFile statement because method creates files instead
        file.mkdirs();

        // Adjust file pointer to the actual .txt file (Which stores the userList database)
        file = new File(saveLocation.concat(File.separatorChar + "userList.txt"));

        // Return if successful
        return file.createNewFile();
    }
}
