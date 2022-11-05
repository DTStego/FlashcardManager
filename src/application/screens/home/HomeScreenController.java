package application.screens.home;

import java.util.List;

import application.Main;
import application.managers.Course;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class HomeScreenController {

    @FXML
    private TabPane courseList;
    @FXML
    private VBox rename;
    @FXML
    private TextField newCourseNameInput;
    @FXML
    private Text errorTxt;

   @FXML
   public void initialize() {
        List<Course> currentCourseList = Main.currentUser.getNotebook().getCourseList();

        //Goes through courses and displays tabs for them
        for (Course course : currentCourseList) {
            Tab tempTab = new Tab();
            String tempTabName = course.getName();
            tempTab.setGraphic(new Label(tempTabName));
            courseList.getTabs().add(tempTab);        
        } 
   }

    @FXML
    void onDeleteTabClick(MouseEvent event) {
        if (courseList.getTabs().size() > 0) {
            int selectedIndex = courseList.getSelectionModel().getSelectedIndex();

            //Finds current Tab
            Tab currentTab = courseList.getTabs().get(selectedIndex);

            Label currentTabText = (Label)currentTab.getGraphic();
            String currentName = currentTabText.getText();

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
                displayError("There are no courses to delete");
            } else {
                //Remove in notebook, then remove in UI
                rename.setVisible(false);
                removeError();
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
        blankTab.setGraphic(new Label(blankTabName));
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
        } else {
            displayError("There are no courses to rename");
        }
    }

    void nameTab() {
        rename.setVisible(true);
    }

    @FXML
    void submitNewCourseName(MouseEvent event) {
        if (!"".equals(newCourseNameInput.getText())) {
            Course currentCourse = null;

            int selectedIndex = courseList.getSelectionModel().getSelectedIndex();
            Tab currentTab = courseList.getTabs().get(selectedIndex);

            Label currentTabText = (Label)currentTab.getGraphic();
            String currentName = currentTabText.getText();
            String newName = newCourseNameInput.getText();
            
            //Find in notebook and rename file 
            List<Course> currentCourseList = Main.currentUser.getNotebook().getCourseList();

            for (Course course : currentCourseList) {
                if (course.getName().equals(currentName)) {
                    currentCourse = course; 
                } 
            } 

            if (currentCourse == null) {
                displayError("There are no courses to rename");
            } else {
                removeError();

                currentCourse.rename(newName);
                
                //Rename in UI
                currentTabText.setText(newName);
                newCourseNameInput.clear();
                rename.setVisible(false);
                updateUser();
                }
        } else {
            displayError("Please enter a name for this course.");
        }
    }

    @FXML
    void signOut(MouseEvent event) {
        Main.loadScreen(event, "screens/login/LoginScreen.fxml", "LoginScreen");
    }


    public void updateUser() {
        Main.userDatabase.updateUser(Main.currentUser);
    }

    public void displayError(String text) {
        errorTxt.setVisible(true);
        errorTxt.setText(text);
    }

    public void removeError() {
        errorTxt.setVisible(false);
    }
}
