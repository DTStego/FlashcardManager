package application.screens.home;

import application.Main;
import application.managers.Course;
import application.managers.IndexCard;
import application.managers.Notebook;
import application.managers.Topic;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
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
    @FXML
    private Label errorMsg;

    private final Notebook notebook = Main.currentUser.getNotebook();
    private Course currentCourse;
    private Topic currentTopic;
    private IndexCard currentIndexCard;
    private Tab currentTab;
    private TabPane currentTabPane = courseTabPane;

   @FXML
   public void initialize()
   {
       if (!notebook.getCourseList().isEmpty())
       {
           for (Course course : notebook.getCourseList())
           {
               Tab courseTab = createCourseTab(course);

               TabPane tabPane = buildTabPane();
               courseTab.setContent(tabPane);

               for (Topic topic : course.getTopicList())
               {
                    createTopicTab(topic, tabPane);
               }
           }
       }
   }

   /** Used when creating a new tab to create vertical tabs */
    void rotateTab(Tab tab)
    {
        Label tempLabel = new Label(tab.getText());
        tempLabel.setRotate(90);

        StackPane tempPane = new StackPane(new Group(tempLabel));
        tempPane.setRotate(90);

        tab.setGraphic(tempPane);
        tab.setText("");
    }

    /** Method overloading for renaming tabs */
    void rotateTab(Tab tab, String newName)
    {
        Label tempLabel = new Label(newName);
        tempLabel.setRotate(90);

        StackPane tempPane = new StackPane(new Group(tempLabel));

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

    private Tab createCourseTab(Course course)
    {
        Tab newTab = new Tab();
        newTab.setText(course.getName());

        rotateTab(newTab);
        courseTabPane.getTabs().add(newTab);

        currentCourse = course;
        currentTopic = null;
        currentIndexCard = null;
        currentTab = newTab;
        currentTabPane = courseTabPane;

        newTab.setOnSelectionChanged(event ->
        {
            if (newTab.isSelected())
            {
                currentCourse = course;
                currentTopic = null;
                currentIndexCard = null;
                currentTab = newTab;
                currentTabPane = courseTabPane;
            }
        });

        updateUser();
        return newTab;
    }

    /** Creates a topic tab in the selected course. */
    @FXML
    void createNewTopicBtn()
    {
        errorMsg.setText("");

        // Error if there is no selected course.
        if (currentCourse == null || currentTopic != null || currentIndexCard != null)
        {
            errorMsg.setText("Please click on a course!");
            return;
        }

        TabPane tabPane;

        // Checks to see if there is already a TabPane, if not, build one.
        if (currentTab.getContent() == null)
        {
            tabPane = buildTabPane();

            currentTab.setContent(tabPane);
        }

        // Retrieve the TabPane from the one built above or an existing one already loaded in the program.
        tabPane = (TabPane) currentTab.getContent();

        // Create a new topic object and store it in the course's topic list.
        Topic topic = new Topic(new ArrayList<>(), "Untitled Topic");
        currentCourse.getTopicList().add(topic);

        // Create an actual topic tab in the course's TabPane
        createTopicTab(topic, tabPane);
    }

    private void createTopicTab(Topic topic, TabPane tabpane)
    {
        Tab newTab = new Tab();
        newTab.setText(topic.getName());

        rotateTab(newTab);
        tabpane.getTabs().add(newTab);

        currentTopic = topic;
        currentIndexCard = null;

        newTab.setOnSelectionChanged(event ->
        {
            currentTopic = topic;
            currentIndexCard = null;
            currentTab = newTab;
            currentTabPane = tabpane;
        });
    }

    /**
     * Used when inserting topics into a course tab since
     * only the courses TabPane can be built in SceneBuilder.
     * @return TabPane with necessary settings that allows for tab vertical rotation,
     * tabs set to align on the left, and minimum tab size. Also disables ability to
     * delete tabs with the (X) button in the GUI which would bypass save ability.
     */
    private TabPane buildTabPane()
    {
        TabPane tabPane = new TabPane();
        tabPane.setRotateGraphic(true);
        tabPane.setSide(Side.LEFT);
        tabPane.setTabMinHeight(75);
        tabPane.setTabMaxHeight(150);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        return tabPane;
    }

    /** Deletes a course or topic tab and its corresponding object from the user's notebook */
    @FXML
    void deleteBtn()
    {
        if (currentIndexCard != null)
        {
            currentTopic.getCardList().remove(currentIndexCard);
            // Remove card information (Perhaps to another card if there is one or make it blank).
            updateUser();
            return;
        }

        if (currentTopic != null)
        {
            currentCourse.getTopicList().remove(currentTopic);
            currentTabPane.getTabs().remove(currentTab);
            updateUser();
            return;
        }

        if (currentCourse != null)
        {
            notebook.getCourseList().remove(currentCourse);
            currentTabPane.getTabs().remove(currentTab);
            updateUser();
        }
    }

    /**
     * Renames a course or topic tab and updates its object in the user's notebook.
     * There will be dedicated rename options for flashcards.
     */
    @FXML
    void renameBtn()
    {
        errorMsg.setText("");

        if (renameTxtField.getText().isEmpty())
        {
            errorMsg.setText("Please input a name!");
            return;
        }

        if (renameTxtField.getText().length() > 20)
        {
            errorMsg.setText("20 Character Limit");
            return;
        }

        if (currentIndexCard != null)
        {
            errorMsg.setText("Please use the dedicated rename buttons for flashcards.");
        }

        errorMsg.setText("");

        if (currentTopic != null)
        {
            currentTopic.setName(renameTxtField.getText());
            rotateTab(currentTab, renameTxtField.getText());
            updateUser();

            return;
        }

        if (currentCourse != null)
        {
            currentCourse.setName(renameTxtField.getText());
            rotateTab(currentTab, renameTxtField.getText());
            updateUser();
        }
    }

    Course getCurrentCourse() {
        Course currentCourse = null;

        int selectedIndex = courseTabPane.getSelectionModel().getSelectedIndex();
        Tab currentTab = courseTabPane.getTabs().get(selectedIndex);

        Label currentTabText = (Label) currentTab.getGraphic();
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
    void onAccountSettingsBtnClick(ActionEvent event)
    {
        Main.loadScreen(event,"screens/accountSettings/accountSettingsScreen.fxml", "AccountSettings");
    }
    @FXML
    void signOut(ActionEvent event)
    {
        Main.loadScreen(event, "screens/login/LoginScreen.fxml", "LoginScreen");
    }

    public void updateUser()
    {
        Main.userDatabase.updateUser(Main.currentUser);
    }
}
