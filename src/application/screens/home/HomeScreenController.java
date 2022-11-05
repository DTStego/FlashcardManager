package application.screens.home;

import java.util.List;

import application.Main;
import application.User;
import application.managers.Course;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class HomeScreenController {

    @FXML
    private TabPane courseList;

    @FXML
    private Button deleteCourseBtn;

    @FXML
    private Button newCourseBtn;

    @FXML
    private Button renameCourseBtn;

    @FXML
    private VBox rename;

    @FXML
    private TextField newCourseNameInput;



    @FXML
    void onDeleteTabClick(MouseEvent event) {
        if (courseList.getTabs().size() > 0) {
            int selectedIndex = courseList.getSelectionModel().getSelectedIndex();

            //Finds current Tab
            Tab currentTab = courseList.getTabs().get(selectedIndex);
            String currentName = currentTab.getText();

            //Iterates through notebook courselist to find course to delete
            List<Course> currentCourseList = Main.currentUser.getNotebook().getCourseList();
            Course currentCourse = null;

            for (Course course : currentCourseList) {
                if (course.getName().equals(currentName)) {
                    currentCourse = course; 
                } 
            } 

            if (currentCourse == null) {
                //Show error
            } else {
                //Remove in notebook, then remove in UI
                Main.currentUser.getNotebook().getCourseList().remove(currentCourse);          
                courseList.getTabs().remove(selectedIndex);
                updateUser();
            }
        }
    }

    @FXML
    void onNewTabClick(MouseEvent event) {
        Tab blankTab = new Tab();
        String blankTabName = "test tab " + courseList.getTabs().size();
        blankTab.setText(blankTabName);
        courseList.getTabs().add(blankTab);
        courseList.getSelectionModel().select(blankTab);

        Course newCourse = new Course(null, blankTabName);
        Main.currentUser.getNotebook().getCourseList().add(newCourse);

        updateUser();
        nameTab();
    }

    @FXML
    void onRenameTabClick(MouseEvent event) {
        if (!courseList.getTabs().isEmpty()) {
            nameTab();
        } 
    }

    void nameTab() {
        rename.setVisible(true);
    }

    @FXML
    void submitNewCourseName(MouseEvent event) {
        if (newCourseNameInput.getText() != null) {
            Course currentCourse = null;

            int selectedIndex = courseList.getSelectionModel().getSelectedIndex();
            Tab currentTab = courseList.getTabs().get(selectedIndex);

            String currentName = currentTab.getText();
            String newName = newCourseNameInput.getText();
            
            //Find in notebook and rename file 
            List<Course> currentCourseList = Main.currentUser.getNotebook().getCourseList();

            for (Course course : currentCourseList) {
                if (course.getName().equals(currentName)) {
                    currentCourse = course; 
                } 
            } 

            if (currentCourse == null) {
                //Show error
            } else {

                currentCourse.rename(newName);
                //Rename in UI
                currentTab.setText(newName);
                newCourseNameInput.clear();
                rename.setVisible(false);
                updateUser();
                }
        } else {
            //show error of course name is empty
        }
    }

    @FXML
    void signOut(MouseEvent event) {
        Main.loadScreen(event, "screens/login/LoginScreen.fxml", "LoginScreen");
    }


    public void updateUser() {
        Main.userDatabase.updateUser(Main.currentUser);
    }
}
