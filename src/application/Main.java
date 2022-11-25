package application;

import application.persistence.UserPersistence;
import application.persistence.UserSerialize;
import javafx.application.Application;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

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
        Parent root = FXMLLoader.load(getClass().getResource("screens/login/LoginScreen.fxml"));
        Scene scene = new Scene(root, screenWidth * screenScale, screenHeight * screenScale);

        primaryStage.setTitle("Flashcard Manager");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
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

        launch(args);
    }

    /**
     * Global method to switch to another screen from an existing one. Generally used in
     * conjunction with a MouseEvent, i.e., when a user clicks on a button to go to another screen.
     * @param event Possibly an FXML MouseEvent which is used to retrieve the stage
     * @param fxmlLocation String that represents the file location of an FXML file
     * @param stageTitle String to change the window's title, e.g., "Home Page"
     */
    public static void loadScreen(Event event, String fxmlLocation, String stageTitle)
    {
        try
        {
            Stage primaryStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            Parent root = FXMLLoader.load(Main.class.getResource(fxmlLocation));
            Scene scene = new Scene(root, Main.screenWidth * Main.screenScale, Main.screenHeight * Main.screenScale);

            primaryStage.setTitle(stageTitle);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e)
        {
            System.out.println(fxmlLocation + ": Target FXML file not found");
        }
    }
}