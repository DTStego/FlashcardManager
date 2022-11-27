package application.screens.home;

import application.Main;
import application.managers.Course;
import application.managers.Topic;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.HashMap;
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

    private HashMap<Tab, CourseTabController> tabMap = new HashMap<>();

    private HashMap<Tab, Course> courseMap = new HashMap<>();

   @FXML
   public void initialize() throws IOException {
        List<Course> currentCourseList = Main.currentUser.getNotebook().getCourseList();

        //Goes through courses and displays tabs for them
        for (Course course : currentCourseList) {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("screens/home/courseTab.fxml"));
            Tab tempTab = new Tab();
            String tempTabName = course.getName();
            tempTab.setGraphic(new Label(tempTabName));

            Parent root = loader.load();
            tempTab.setContent(root);
            courseList.getTabs().add(tempTab);
            tabMap.put(tempTab, loader.getController());
            courseMap.put(tempTab, course);
        } 
   }

    @FXML
    void onDeleteTabClick(MouseEvent event) {
        if (courseList.getTabs().size() > 0) {
            int selectedIndex = courseList.getSelectionModel().getSelectedIndex();

            //Finds current Tab
            Tab currentTab = courseList.getTabs().get(selectedIndex);
            Course currentCourse = courseMap.get(currentTab);

            if (currentCourse == null) {
                //Show error
                displayError("There are no courses to delete");
            } else {
                //Remove in notebook, then remove in UI
                rename.setVisible(false);
                removeError();
                Main.currentUser.getNotebook().getCourseList().remove(currentCourse);          
                Tab removedTab = courseList.getTabs().remove(selectedIndex);
                tabMap.remove(removedTab);
                courseMap.remove(removedTab);
                updateUser();
            }
        }
    }

    @FXML
    void onNewTabClick(MouseEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("screens/home/courseTab.fxml"));
        Tab blankTab = new Tab();
        String blankTabName = "Blank Course " + courseList.getTabs().size();
        blankTab.setGraphic(new Label(blankTabName));
        courseList.getTabs().add(blankTab);
        courseList.getSelectionModel().select(blankTab);

        Course newCourse = new Course(null, blankTabName);
        Main.currentUser.getNotebook().getCourseList().add(newCourse);

        Parent root = loader.load();
        blankTab.setContent(root);

        tabMap.put(blankTab, loader.getController());
        courseMap.put(blankTab, newCourse);

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
            TopicPaneController topicController = loader.getController();
            CourseTabController tabController = tabMap.get(currentTab);

            String defaultTopicName = "Unnamed Topic " + (tabController.getTopicListSize() + 1);
            topicController.setTopicName(defaultTopicName);
            tabController.addTopic(root);

            Course currentCourse = courseMap.get(currentTab);
            currentCourse.add(new Topic(null, defaultTopicName));
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

            int selectedIndex = courseList.getSelectionModel().getSelectedIndex();
            Tab currentTab = courseList.getTabs().get(selectedIndex);

            Label currentTabText = (Label)currentTab.getGraphic();
            String newName = newCourseNameInput.getText();

            Course currentCourse = courseMap.get(currentTab);

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
