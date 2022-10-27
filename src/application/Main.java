package application;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application
{
    // Size of screen, initialized on program startup and is scaled using screenScale
    public static double screenWidth;
    public static double screenHeight;
    // Default scale for size of stage; Is multiplied with screenWidth and screenHeight
    public static double screenScale = 0.75;

    // Initialized in main() using a UserPersistence implementation
    public static UserDatabase userDatabase;

    @Override
    public void start(Stage primaryStage)
    {
        Group root = new Group();
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

        // UserDatabase should end up being empty or retrieved from a file or sql database
        UserPersistence lookup = new UserSerialize();
        userDatabase = new UserDatabase(lookup.retrieve());

        launch(args);
    }
}