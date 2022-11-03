package application.loginPage.resetpage;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class testResetPage extends Application
{
    
    @Override
    public void start(Stage primaryStage)
    {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("resetpageUI.fxml"));

            Scene scene = new Scene(root, 600, 400);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}