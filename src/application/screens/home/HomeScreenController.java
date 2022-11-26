package application.screens.home;

import application.Main;
import application.managers.Course;
import application.managers.IndexCard;
import application.managers.Notebook;
import application.managers.Topic;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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

    @FXML
    private Label title;
    @FXML
    private CheckBox checkmark;
    @FXML
    private ImageView leftArrow;
    @FXML
    private ImageView rightArrow;
    @FXML
    private Button flipBtn;
    @FXML
    private MenuButton sortBtn;
    @FXML
    private Button createCardBtn;
    @FXML
    private Button deleteCardBtn;
    @FXML
    private Label cardText;
    @FXML
    private TextField cardTextField;

    private final Notebook notebook = Main.currentUser.getNotebook();

    // Will identify below variables as "Sight" or "Program Sight".
    private Course currentCourse;
    private Topic currentTopic;
    private IndexCard currentIndexCard;
    private Tab currentTab;
    private TabPane currentTabPane = courseTabPane;

    // TODO: Hide "Create Topic/Course" buttons based on program sight.
    // TODO: Fix currentCourse not equaling the first course on setup.
   @FXML
   public void initialize()
   {
       // Global rule for course TabPane when a course is selected.
       courseTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) ->
       {
            if (newTab != null)
            {
                currentTab = newTab;

                /*
                 * Course names are hidden inside the tab's getGraphic() method which has
                 * a StackPane that bundles a Label (That's where the course name is) inside a group.
                 * Attempts to unravel that to get the label.
                 */
                Label courseName = (Label) ((Group) ((StackPane) newTab.getGraphic()).getChildren().get(0)).getChildren().get(0);

                // Search for the course in user's notebook using the courseName Label.
                for (Course course : notebook.getCourseList())
                {
                    if (courseName.getText().equals(course.getName()))
                        currentCourse = course;
                }

                // If a course tab is selected, make sure program is set so neither a topic nor card is selected.
                currentTopic = null;
                currentIndexCard = null;
                currentTabPane = courseTabPane;
            }

            // Change visibility of flashcard elements, unload flashcards
            makeCardElementsVisible(false);
       });

       // Populate GUI with course and topic tabs.
       if (!notebook.getCourseList().isEmpty())
       {
           // Used to set the sight to the first course tab.
           Tab firstTabTemp = new Tab();

           // Iterate through the list of courses and create a tab for each course.
           for (int i = 0 ; i < notebook.getCourseList().size(); i++)
           {
               Tab courseTab = createCourseTab(notebook.getCourseList().get(i));

               // Save the first course to an outside variable to set the program sight later.
               if (i == 0)
                   firstTabTemp = courseTab;

               TabPane tabPane = buildTabPane();
               topicTabPaneSettings(courseTab, tabPane);

               // Iterate though each topic in each course and populate that course's TabPane.
               for (Topic topic : notebook.getCourseList().get(i).getTopicList())
               {
                   createTopicTab(topic, tabPane);
               }
           }

           // Set the sight to the first course tab.
           currentCourse = notebook.getCourseList().get(0);
           currentTab = firstTabTemp;
           makeCardElementsVisible(false);
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

    /** Method overloading for changing a tab's name */
    void rotateTab(Tab tab, String newName)
    {
        Label tempLabel = new Label(newName);
        tempLabel.setRotate(90);

        StackPane tempPane = new StackPane(new Group(tempLabel));

        tab.setGraphic(tempPane);
        tab.setText("");
    }

    /** On "Create New Course" click, create a new Course and corresponding Tab (Insert into courseTabPane). */
    @FXML
    void createNewCourseBtn()
    {
        Course course = new Course(new ArrayList<>(), "Untitled Course " + (notebook.getCourseList().size() + 1));
        notebook.getCourseList().add(course);
        updateUser();

        createCourseTab(course);
    }

    /**
     * Create a Tab based off of Course object, add to list, and set program sight to course.
     * @param course Course object to be linked with a course tab based on its name.
     * @return Tab that was created and inserted into the course TabPane
     */
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

        // Checks to see if there is already a TabPane, if not, build one to list topics.
        if (currentTab.getContent() == null)
        {
            tabPane = buildTabPane();

            // Event when a topic is clicked on by a user in the TabPane containing topics.
            topicTabPaneSettings(currentTab, tabPane);
        }

        // Retrieve the TabPane from the one built above or an existing one already loaded in the program.
        tabPane = (TabPane) currentTab.getContent();

        // Create a new topic object and store it in the course's topic list.
        Topic topic = new Topic(new ArrayList<>(), "Untitled Topic " + (currentCourse.getTopicList().size() + 1));
        currentCourse.getTopicList().add(topic);
        updateUser();

        // Create an actual topic tab in the course's TabPane
        createTopicTab(topic, tabPane);
    }

    private void createTopicTab(Topic topic, TabPane tabpane)
    {
        Tab newTab = new Tab();
        newTab.setText(topic.getName());

        rotateTab(newTab);
        tabpane.getTabs().add(newTab);

        currentIndexCard = null;
    }

    private void topicTabPaneSettings(Tab courseTab, TabPane tabPane)
    {
        tabPane.getSelectionModel().selectedItemProperty().addListener((observableValue, oldTab, newTab) ->
        {
            if (newTab != null)
            {
                currentTab = newTab;

                /*
                 * Topic names are hidden inside the tab's getGraphic() method which has
                 * a StackPane that bundles a Label (That's where the course name is) inside a group.
                 * Attempts to unravel that to get the label.
                 */
                Label topicName = (Label) ((Group) ((StackPane) newTab.getGraphic()).getChildren().get(0)).getChildren().get(0);

                for (Topic topic : currentCourse.getTopicList())
                {
                    if (topicName.getText().equals(topic.getName()))
                    {
                        currentTopic = topic;
                        currentIndexCard = null;
                        currentTabPane = newTab.getTabPane();
                    }
                }

                // Set Visibility, Load flashcards
                makeCardElementsVisible(true);
            }
        });
        courseTab.setContent(tabPane);
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
        tabPane.setTabMaxHeight(160);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        return tabPane;
    }

    /** Deletes a course or topic tab and its corresponding object from the user's notebook */
    // TODO Add prompt to check if user wants to delete the manager.
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

    /** Change default flashcard element visibility. Ignores delete button when there is no flashcard in the topicList */
    @FXML
    void makeCardElementsVisible(boolean input)
    {
        title.setDisable(!input);
        checkmark.setDisable(!input);
        sortBtn.setDisable(!input);
        flipBtn.setDisable(!input);
        createCardBtn.setDisable(!input);
        cardText.setDisable(!input);
    }

    @FXML
    void createNewCardBtn()
    {

    }

    @FXML
    void handCursor()
    {
        courseTabPane.getScene().setCursor(Cursor.HAND);
    }

    @FXML
    void pointerCursor()
    {
        courseTabPane.getScene().setCursor(Cursor.DEFAULT);
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
