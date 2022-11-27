package application.screens.home;

import application.Main;
import application.managers.Course;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.List;

public class HomeScreenController {

    @FXML
    private TabPane courseList;

    @FXML
    private Text errorTxt;

    @FXML
    private TextField newCourseNameInput;

    @FXML
    private VBox rename;

   @FXML
   public void initialize() throws IOException {
        List<Course> currentCourseList = Main.currentUser.getNotebook().getCourseList();

        //Goes through courses and displays tabs for them
        for (Course course : currentCourseList) {
            Tab tempTab = new Tab();
            String tempTabName = course.getName();
            tempTab.setGraphic(new Label(tempTabName));
            Parent root = FXMLLoader.load(Main.class.getResource("screens/home/courseTab.fxml"));
            tempTab.setContent(root);
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
    void onNewTabClick(MouseEvent event) throws IOException{
        Tab blankTab = new Tab();
        String blankTabName = "Blank Course " + courseList.getTabs().size();
        blankTab.setGraphic(new Label(blankTabName));
        courseList.getTabs().add(blankTab);
        courseList.getSelectionModel().select(blankTab);

        Course newCourse = new Course(null, blankTabName);
        Main.currentUser.getNotebook().getCourseList().add(newCourse);

        Parent root = FXMLLoader.load(Main.class.getResource("screens/home/courseTab.fxml"));
        blankTab.setContent(root);

        updateUser();
        nameTab();
    }

    @FXML
    void onNewTopicClick(MouseEvent event) throws IOException {
        if (!courseList.getTabs().isEmpty()) {
            int selectedIndex = courseList.getSelectionModel().getSelectedIndex();
            Tab currentTab = courseList.getTabs().get(selectedIndex);

            FXMLLoader loader = new FXMLLoader(Main.class.getResource("screens/home/topicPane.fxml"));
            Parent root = loader.load();

            VBox topicList = (VBox) ((AnchorPane) currentTab.getContent()).getChildren().get(0);
            topicList.getChildren().add(root);
        }
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
    void onAccountSettingsBtnClick(MouseEvent event) {
        Main.loadScreen(event,"screens/accountSettings/accountSettingsScreen.fxml", "AccountSettings");
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
