package application.screens.home;

import application.Main;
import application.managers.Course;
import application.managers.Notebook;
import application.managers.Topic;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HomeScreenController {
    @FXML
    private TabPane courseTabPane;
    @FXML
    private Button renameBtn;
    @FXML
    private TextField renameTxtField;
    @FXML
    private Button deleteBtn;
    @FXML
    private Button newTopicBtn;

    private final Notebook notebook = Main.currentUser.getNotebook();
    private Course currentCourse;
    private Topic currentTopic;
    private Tab currentTab;
    private TabPane currentTabPane = courseTabPane;

   @FXML
   public void initialize()
   {
       if (!notebook.getCourseList().isEmpty())
       {
           for (Course course : notebook.getCourseList())
           {
               createCourseTab(course);
           }
       }
   }

    void rotateTab(Tab tab)
    {
        Label tempLabel = new Label(tab.getText());
        tempLabel.setRotate(90);

        StackPane tempPane = new StackPane(new Group(tempLabel));
        tempPane.setRotate(90);
        tempPane.setRotate(90);

        tab.setGraphic(tempPane);
        tab.setText("");
    }

    @FXML
    void createNewCourseBtn()
    {
        Course course = new Course(new ArrayList<>(), "Untitled Course");
        notebook.getCourseList().add(course);

        createCourseTab(course);
    }

    private void createCourseTab(Course course)
    {
        Tab newTab = new Tab();
        newTab.setText(course.getName());

        rotateTab(newTab);
        courseTabPane.getTabs().add(newTab);
        newTab.setOnSelectionChanged(event ->
        {
            if (newTab.isSelected())
            {
                currentCourse = course;
                currentTab = newTab;
                currentTabPane = courseTabPane;
                makeCourseOptionsVisible();
            }
        });

        updateUser();
    }

    @FXML
    void deleteBtn()
    {
        notebook.delete(currentCourse);
        currentTabPane.getTabs().remove(currentTab);

        updateUser();
    }

    /** Make the rename, delete, and add new topic buttons visible **/
    void makeCourseOptionsVisible()
    {
        renameBtn.setVisible(true);
        renameBtn.setDisable(false);

        renameTxtField.setVisible(true);
        renameTxtField.setDisable(false);

        deleteBtn.setVisible(true);
        deleteBtn.setDisable(false);

        newTopicBtn.setVisible(true);
        newTopicBtn.setDisable(false);
    }

    Course getCurrentCourse() {
        Course currentCourse = null;

        int selectedIndex = courseTabPane.getSelectionModel().getSelectedIndex();
        Tab currentTab = courseTabPane.getTabs().get(selectedIndex);

        Label currentTabText = (Label)currentTab.getGraphic();
        String currentName = currentTabText.getText();
        
        //Find in notebook and rename file 
        List<Course> currentCourseList = Main.currentUser.getNotebook().getCourseList();

        for (Course course : currentCourseList) {
            if (course.getName().equals(currentName)) {
                currentCourse = course; 
            } 
        }

        return currentCourse;
    }
    @FXML
    void onAccountSettingsBtnClick(ActionEvent event) {
        Main.loadScreen(event,"screens/accountSettings/accountSettingsScreen.fxml", "AccountSettings");
    }
    @FXML
    void signOut(ActionEvent event) {
        Main.loadScreen(event, "screens/login/LoginScreen.fxml", "LoginScreen");
    }

    public void updateUser()
    {
        Main.userDatabase.updateUser(Main.currentUser);
    }
}
