package application;

import application.managers.Notebook;
import application.persistence.UserPersistence;
import application.persistence.UserSerialize;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import javax.swing.filechooser.FileSystemView;
import java.io.IOException;

public class Main extends Application
{
    // Size of screen, initialized on program startup and is scaled using screenScale
    public static double screenWidth;
    public static double screenHeight;
    // Default scale for size of stage; Is multiplied with screenWidth and screenHeight
    public static double screenScale = 0.75;

    // Initialized in main() using a UserPersistence implementation
    public static UserDatabase userDatabase;
    // Static variable contained in main to change implementation (Serial, SQL, etc.)
    public static UserPersistence userPersistence;
    public static User currentUser;

    @Override
    public void start(Stage primaryStage) throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getResource("login/LoginScreen.fxml"));
        Scene scene = new Scene(root, screenWidth * screenScale, screenHeight * screenScale);

        primaryStage.setTitle("Flashcard Manager");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args)
    {
        // At startup, sets the values of screenWidth and screenHeight to monitor size
        Rectangle2D bounds = Screen.getPrimary().getBounds();
        screenWidth = bounds.getWidth();
        screenHeight = bounds.getHeight();

        // Creates a UserPersistence object with a chosen implementation and retrieves and stores a userList
        userPersistence = new UserSerialize();
        userDatabase = new UserDatabase(userPersistence.retrieve());

        User testUser = new User("Username1", "Pass1","s1","a1", new Notebook());

        System.out.println(FileSystemView.getFileSystemView().getDefaultDirectory().getPath());
        userDatabase.addUser(testUser);
        launch(args);
    }
}