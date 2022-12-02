package application.screens.home;

import application.Main;
import application.managers.Course;
import application.managers.IndexCard;
import application.managers.Notebook;
import application.managers.Topic;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.Translate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeScreenController {
    @FXML
    private TabPane courseTabPane;
    @FXML
    private Button renameBtn;
    @FXML
    private TextField renameTxtField;
    @FXML
    private Button newTopicBtn;
    @FXML
    private Label errorMsg;

    @FXML
    private Label side;
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
    private Button cardTextSetter;
    @FXML
    private TextField cardTextField;
    @FXML
    private Button randomizeBtn;
    // Is initialized when a topic topicPane is created (Which is inserted in a course tab).
    private List<IndexCard> displayedCardList;

    private boolean onFrontSide = true;

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

            makeCardElementsVisible(false);
       });

       Topic tempTopic = null;

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

               //Set the sizes of topic tabs created
               tabPane.setTabMinWidth(40);
               tabPane.setTabMinHeight(200);

               topicTabPaneSettings(courseTab, tabPane);

               // Iterate though each topic in each course and populate that course's TabPane.
               for (int j = 0; j < notebook.getCourseList().get(i).getTopicList().size(); j++)
               {
                   Topic topic = notebook.getCourseList().get(i).getTopicList().get(j);

                   createTopicTab(topic, tabPane);

                   if (j == 0)
                   {
                       tempTopic = topic;
                       firstTabTemp = tabPane.getTabs().get(j);
                   }
               }
           }

           // Set the sight to the first course tab.
           currentCourse = notebook.getCourseList().get(0);
           currentTopic = tempTopic;
           currentTab = firstTabTemp;
           makeCardElementsVisible(false);

           if (currentTopic != null && !currentTopic.getCardList().isEmpty())
           {
               makeCardElementsVisible(true);
               currentIndexCard = currentTopic.getCardList().get(0);
               displayCard();
           }
           else if (currentTopic != null)
           {
               side.setDisable(false);
               createCardBtn.setDisable(false);
           }
       }

       //Set the size of course tabs created
       courseTabPane.setTabMinWidth(40);
       courseTabPane.setTabMinHeight(200);
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
        String newCourseName = "Untitled Course " + (notebook.getCourseList().size() + 1);
        for (Course course : notebook.getCourseList())
        {
            if (newCourseName.equals(course.getName()))
            {
                newCourseName = "Untitled Course " + (notebook.getCourseList().size() + 2);
                break;
            }
        }

        Course course = new Course(new ArrayList<>(), newCourseName);
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
            errorMsg.setText("* Please click on a course!");
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

        //Set size of topic tabs created
        tabPane.setTabMinWidth(40);
        tabPane.setTabMinHeight(200);

        // Create a new topic object and store it in the course's topic list.
        String newTopicName = "Untitled Topic " + (currentCourse.getTopicList().size() + 1);
        for (Topic t : currentCourse.getTopicList())
        {
            if (newTopicName.equals(t.getName()))
            {
                newTopicName = "Untitled Topic " + (currentCourse.getTopicList().size() + 2);
                break;
            }
        }

        Topic topic = new Topic(new ArrayList<>(), newTopicName);
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

    /**
     * Run when a user clicks on a topic tab. Initializes the Flashcard UI with the first flashcard of a Topic
     * object if there is a flashcard.
     */
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
                        displayedCardList = currentTopic.getCardList();
                        currentIndexCard = null;
                        currentTabPane = newTab.getTabPane();
                    }
                }

                // Set Visibility, Load flashcards
                if (currentTopic.getCardList().size() > 0)
                {
                    currentIndexCard = currentTopic.getCardList().get(0);
                    displayCard();
                    makeCardElementsVisible(true);
                    checkArrowVisibility();
                    randomizeBtn.setDisable(true);
                }
                else if (currentTopic.getCardList().size() > 1)
                {
                    randomizeBtn.setDisable(false);
                }
                else
                {
                    cardText.setText("");
                    makeDefaultCardElementsVisible();
                    randomizeBtn.setDisable(true);
                }
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

    /** Deletes a course tab and its corresponding object from the user's notebook */
    @FXML
    void deleteCourse()
    {
        errorMsg.setText("");
        errorMsg.setVisible(false);

        if (currentCourse != null)
        {
            notebook.getCourseList().remove(currentCourse);
            currentTabPane.getTabs().remove(currentTab);
            updateUser();
        }
        else
        {
            errorMsg.setText("No Course Selected!");
            errorMsg.setVisible(true);
        }
    }

    @FXML
    void deleteTopic()
    {
        errorMsg.setText("");
        errorMsg.setVisible(false);

        if (currentTopic != null)
        {
            currentCourse.getTopicList().remove(currentTopic);
            currentTabPane.getTabs().remove(currentTab);
            updateUser();
        }
        else
        {
            errorMsg.setText("No Topic Selected!");
            errorMsg.setVisible(true);
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
            errorMsg.setText("* Please input a name!");
            return;
        }

        if (renameTxtField.getText().length() > 20)
        {
            errorMsg.setText("* Name must be equal to or less than 20 characters");
            return;
        }

        if (currentIndexCard != null)
        {
            errorMsg.setText("* Please use the dedicated rename buttons for flashcards.");
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
    void makeDefaultCardElementsVisible()
    {
        side.setDisable(false);
        createCardBtn.setDisable(false);
        cardText.setDisable(false);
        cardTextField.setDisable(true);
        cardTextSetter.setDisable(true);
        checkmark.setDisable(true);
        flipBtn.setDisable(true);
        sortBtn.setDisable(true);
        deleteCardBtn.setDisable(true);
        randomizeBtn.setDisable(true);
    }

    void makeCardElementsVisible(boolean input)
    {
        side.setDisable(!input);
        createCardBtn.setDisable(!input);
        cardText.setDisable(!input);
        cardTextField.setDisable(!input);
        cardTextSetter.setDisable(!input);
        checkmark.setDisable(!input);
        flipBtn.setDisable(!input);
        sortBtn.setDisable(!input);
        deleteCardBtn.setDisable(!input);
    }

    @FXML
    void createNewCardBtn()
    {
        IndexCard newIndexCard = new IndexCard("Enter Text Using Textbox", "Enter Text Using Textbox");
        currentTopic.getCardList().add(newIndexCard);
        displayedCardList = currentTopic.getCardList();
        currentIndexCard = newIndexCard;
        updateUser();
        displayCard();
        side.setText("Front");
        onFrontSide = true;
        makeCardElementsVisible(true);
        checkArrowVisibility();

        if (currentTopic.getCardList().size() > 1)
        {
            randomizeBtn.setDisable(false);
        }
    }

    @FXML
    void deleteCardBtn()
    {
        if (displayedCardList.size() == 1)
        {
            currentTopic.getCardList().remove(currentIndexCard);
            displayedCardList.remove(currentIndexCard);
            makeDefaultCardElementsVisible();
            cardText.setText("");
            updateUser();
            checkArrowVisibility();
        }

        if (currentTopic.getCardList().size() > 1)
        {
            currentTopic.getCardList().remove(currentIndexCard);
            displayedCardList.remove(currentIndexCard);
            currentIndexCard = displayedCardList.get(0);
            displayCard();
            updateUser();
            checkArrowVisibility();
            randomizeBtn.setDisable(false);
        }

        if (currentTopic.getCardList().size() < 2)
        {
            randomizeBtn.setDisable(true);
        }
    }

    @FXML
    void changeCardText()
    {
        cardText.setText(cardTextField.getText());
        cardTextField.setText("");

        if (onFrontSide)
            currentIndexCard.setQuestion(cardText.getText());
        else
            currentIndexCard.setAnswer(cardText.getText());

        updateUser();
    }

    @FXML
    void checkmarkAction()
    {
        currentIndexCard.setHasLearned(checkmark.isSelected());
        updateUser();
    }

    void displayCard()
    {
        if (onFrontSide)
            cardText.setText(currentIndexCard.getQuestion());
        else
            cardText.setText(currentIndexCard.getAnswer());

        checkmark.setSelected(currentIndexCard.hasLearned());
    }

    @FXML
    void flipBtnAction()
    {
        if (onFrontSide)
        {
            side.setText("Back");
            onFrontSide = false;
        } else
        {
            side.setText("Front");
            onFrontSide = true;
        }
        displayCard();
    }

    @FXML
    void leftArrowClick()
    {
        for (int i = 0; i < displayedCardList.size(); i++)
        {
            if (currentIndexCard == displayedCardList.get(i))
            {
                currentIndexCard = displayedCardList.get(i - 1);
                checkArrowVisibility();
                side.setText("Front");
                onFrontSide = true;
                displayCard();
                return;
            }
        }
    }

    @FXML
    void rightArrowClick()
    {
        for (int i = 0; i < displayedCardList.size(); i++)
        {
            if (currentIndexCard == displayedCardList.get(i))
            {
                currentIndexCard = displayedCardList.get(i + 1);
                checkArrowVisibility();
                side.setText("Front");
                onFrontSide = true;
                displayCard();
                return;
            }
        }
    }

    @FXML
    void randomizeCardList()
    {
        Collections.shuffle(displayedCardList);
        currentIndexCard = displayedCardList.get(0);
        displayCard();
        checkArrowVisibility();
    }

    @FXML
    void allMenuItem()
    {
        errorMsg.setText("");
        errorMsg.setVisible(false);

        displayedCardList = new ArrayList<>(currentTopic.getCardList());
        currentIndexCard = displayedCardList.get(0);
        displayCard();
        checkArrowVisibility();
    }

    @FXML
    void learnedMenuItem()
    {
        errorMsg.setText("");
        errorMsg.setVisible(false);

        ArrayList<IndexCard> learnedCardList = new ArrayList<>();
        for (IndexCard indexCard : currentTopic.getCardList())
        {
            if (indexCard.hasLearned())
                learnedCardList.add(indexCard);
        }

        if (learnedCardList.isEmpty())
        {
            errorMsg.setText("* There are no cards for that sort.");
            errorMsg.setVisible(true);
            return;
        }

        displayedCardList = learnedCardList;

        currentIndexCard = displayedCardList.get(0);
        displayCard();
        checkArrowVisibility();
    }

    @FXML
    void notLearnedMenuItem()
    {
        errorMsg.setText("");
        errorMsg.setVisible(false);

        ArrayList<IndexCard> notLearnedCardList = new ArrayList<>();
        for (IndexCard indexCard : currentTopic.getCardList())
        {
            if (!indexCard.hasLearned())
                notLearnedCardList.add(indexCard);
        }

        if (notLearnedCardList.isEmpty())
        {
            errorMsg.setText("* There are no cards for that sort.");
            errorMsg.setVisible(true);
            return;
        }

        displayedCardList = notLearnedCardList;
        currentIndexCard = displayedCardList.get(0);
        displayCard();
        checkArrowVisibility();
    }

    void checkArrowVisibility()
    {
        if (displayedCardList.size() > 1)
        {
            for (int i = 0; i < displayedCardList.size(); i++)
            {
                if (currentIndexCard == displayedCardList.get(i))
                {
                    if (i == 0)
                    {
                        leftArrow.setOpacity(0.35);
                        leftArrow.setDisable(true);
                    }
                    else
                    {
                        leftArrow.setOpacity(1.0);
                        leftArrow.setDisable(false);
                    }

                    if (i == displayedCardList.size() - 1)
                    {
                        rightArrow.setOpacity(0.35);
                        rightArrow.setDisable(true);
                    }
                    else
                    {
                        rightArrow.setOpacity(1.0);
                        rightArrow.setDisable(false);
                    }
                }
            }
        }
        else
        {
            leftArrow.setDisable(true);
            leftArrow.setOpacity(0.35);
            rightArrow.setDisable(true);
            rightArrow.setOpacity(0.35);
        }
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
        Main.loadBigScreen(event,"screens/accountSettings/accountSettingsScreen.fxml", "AccountSettings");
    }
    @FXML
    void signOut(ActionEvent event)
    {
        Main.loadSmallScreen(event, "screens/login/LoginScreen.fxml", "LoginScreen");
    }

    public void updateUser()
    {
        Main.userDatabase.updateUser(Main.currentUser);
    }
}
