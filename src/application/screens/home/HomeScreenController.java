package application.screens.home;

import application.Main;
import application.managers.Course;
import application.managers.IndexCard;
import application.managers.Notebook;
import application.managers.Topic;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeScreenController {
    @FXML
    private TabPane courseTabPane;
    @FXML
    private TextField renameTxtField;
    @FXML
    private Label errorMsg;
    @FXML
    private Label tabSelectedLbl;

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
    /* Is initialized when a topic topicPane is created (Which is inserted in a course tab).
       Temp list used during sorts and randomizers so the user's notebook isn't touched.
     */
    private List<IndexCard> displayedCardList;

    private boolean onFrontSide = true;

    private final Notebook notebook = Main.currentUser.getNotebook();

    // Will identify below variables as "Sight" or "Program Sight".
    private Course currentCourse;
    private Topic currentTopic;
    private IndexCard currentIndexCard;
    private Tab currentTab;
    private TabPane currentTabPane = courseTabPane;

    /**
     * Ran when a user logs in and enters the home screen. Sets behavior for when a user clicks on a tab in the course
     * TabPane. Creates existing course and topic tabs and sets the program scope to the correct items. Also makes sure
     * that buttons and other elements are correctly enabled/disabled.
     */
   @FXML
   public void initialize()
   {
       // Global rule for the "course" TabPane when a course tab is selected.
       courseTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) ->
       {
            if (newTab != null)
            {
                // Update the selected tab label with the tab that was clicked on.
                currentTab = newTab;
                updateSelectedTabLbl();

                /*
                 * Course names are hidden inside the tab's getGraphic() method which has
                 * a StackPane that bundles a Label (That's where the course name is) inside a group.
                 * Attempts to unravel that to get the label/tab name.
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

            // If a course tab is selected, disable the index card UI. It should only be visible when a topic tab is clicked.
            makeCardElementsVisible(false);
       });

       // Used later to select the first topic that the first course has (if one exists) to determine program sight.
       Topic tempTopic = null;

       // Populate GUI with course and topic tabs (if they exist).
       if (!notebook.getCourseList().isEmpty())
       {
           // Used to set the sight to either the first course tab or the first topic tab (if it exists).
           Tab firstTabTemp = new Tab();

           // Iterate through the list of courses and create a tab for each course.
           for (int i = 0 ; i < notebook.getCourseList().size(); i++)
           {
               Tab courseTab = createCourseTab(notebook.getCourseList().get(i));

               // Save the first course to an outside variable to set the program sight later.
               if (i == 0)
                   firstTabTemp = courseTab;

               // Set settings for a topic's TabPane's size.
               TabPane tabPane = buildTabPane();

               // Set conditions when a user clicks on a topic tab.
               topicTabPaneSettings(courseTab, tabPane);

               // Iterate though each topic in each course and populate that course's TabPane with topic tabs.
               for (int j = 0; j < notebook.getCourseList().get(i).getTopicList().size(); j++)
               {
                   Topic topic = notebook.getCourseList().get(i).getTopicList().get(j);

                   createTopicTab(topic, tabPane);

                   // Store the topic and tab for the first topic to set the program sight later.
                   if (j == 0)
                   {
                       tempTopic = topic;
                       firstTabTemp = tabPane.getTabs().get(j);
                   }
               }
           }

           // Set the sight to the first course tab. Update "currently selected" label.
           currentCourse = notebook.getCourseList().get(0);
           currentTopic = tempTopic;
           currentTab = firstTabTemp;
           updateSelectedTabLbl();
           makeCardElementsVisible(false);

           // If there were topics in the first course object in the notebook, display its flashcards.
           if (currentTopic != null && !currentTopic.getCardList().isEmpty())
           {
               makeCardElementsVisible(true);
               currentIndexCard = currentTopic.getCardList().get(0);
               displayCard();
               if (currentTopic.getCardList().size() > 1)
                   randomizeBtn.setDisable(false);
           }
           else if (currentTopic != null)
           {
               // Otherwise allow the user to add new flashcards.
               side.setDisable(false);
               createCardBtn.setDisable(false);
           }
       }

       // Add two courses if there are no courses yet (By default) -- Obfuscates issues that occur when there is only one course.
       if (notebook.getCourseList().size() == 0)
       {
           createNewCourseBtn();
           createNewCourseBtn();
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
        // To mitigate issues when courses have the same name, try to make sure the course doesn't have the same "Untitled Course " + index name.
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

        // Need to rotate the text so it'll be horizontal (the default is vertical).
        rotateTab(newTab);
        courseTabPane.getTabs().add(newTab);

        currentCourse = course;
        currentTopic = null;
        currentIndexCard = null;
        currentTab = newTab;
        updateSelectedTabLbl();
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

        // Create a new topic object and store it in the course's topic list. Same with creating a new course, i.e., name mitigation.
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

        currentTabPane = tabPane;
    }

    /** Creates a topic Tab and inserts it into the specific course's Tab/TabPane */
    private void createTopicTab(Topic topic, TabPane tabpane)
    {
        Tab newTab = new Tab();
        newTab.setText(topic.getName());

        // Rotates the tab so it's horizontal, not vertical (which is the default).
        rotateTab(newTab);
        tabpane.getTabs().add(newTab);

        currentIndexCard = null;
    }

    /**
     * Run when a user clicks on a topic tab. Initializes the Flashcard UI
     * with the first flashcard of a Topic object if there is a flashcard.
     */
    private void topicTabPaneSettings(Tab courseTab, TabPane tabPane)
    {
        // Listener on the entire TabPane that runs when a topic tab is clicked on.
        tabPane.getSelectionModel().selectedItemProperty().addListener((observableValue, oldTab, newTab) ->
        {
            if (newTab != null)
            {
                currentTab = newTab;
                updateSelectedTabLbl();

                /*
                 * Topic names are hidden inside the tab's getGraphic() method which has
                 * a StackPane that bundles a Label (That's where the course name is) inside a group.
                 * Attempts to unravel that to get the label.
                 */
                Label topicName = (Label) ((Group) ((StackPane) newTab.getGraphic()).getChildren().get(0)).getChildren().get(0);

                // Attempts to locate the Topic object based on the selected tab's name.
                for (Topic topic : currentCourse.getTopicList())
                {
                    if (topicName.getText().equals(topic.getName()))
                    {
                        currentTopic = topic;
                        displayedCardList = currentTopic.getCardList();
                        currentIndexCard = null;

                        /* ISSUE: newTab.getTabPane() may return null when the tab is "selected" automatically
                           when a tab near it is deleted. Unsure about the actual cause of this bug...
                         */
                        currentTabPane = newTab.getTabPane();
                    }
                }

                // Set Visibility, Load flashcards starting at the first position.
                if (currentTopic.getCardList().size() > 0)
                {
                    currentIndexCard = currentTopic.getCardList().get(0);
                    displayCard();
                    makeCardElementsVisible(true);
                    checkArrowVisibility();
                    randomizeBtn.setDisable(true);
                    if (currentTopic.getCardList().size() > 1)
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
     * only the "courses" TabPane can be built in SceneBuilder.
     * @return TabPane with necessary settings that allows for tab vertical rotation,
     * tabs set to align on the left, and minimum tab size. Also disables ability to
     * delete tabs with the (X) button in the GUI which would bypass save ability.
     */
    private TabPane buildTabPane()
    {
        TabPane tabPane = new TabPane();
        tabPane.setRotateGraphic(true);
        tabPane.setSide(Side.LEFT);
        tabPane.setTabMinHeight(200);
        tabPane.setTabMaxHeight(160);
        tabPane.setTabMinWidth(40);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        return tabPane;
    }

    /** Deletes a course or topic tab and its corresponding object from the user's notebook */
    @FXML
    void deleteBtn()
    {
        errorMsg.setText("");
        errorMsg.setVisible(false);

        // Code for deleting a topic.
        if (currentTopic != null)
        {
            // Anomaly condition caused by the "on tab-click" listener in certain conditions. Just print an error.
            if (currentTabPane == null)
            {
                errorMsg.setText("Program anomaly. Please create another course/tab and then attempt deletion!");
                errorMsg.setVisible(true);
                return;
            }

            // Remove the Topic object and its tab and update the Notebook object;
            currentCourse.getTopicList().remove(currentTopic);
            currentTabPane.getTabs().remove(currentTab);
            updateUser();

            // If the topicList is empty after deletion, set the program scope to the parent course.
            if (currentCourse.getTopicList().isEmpty())
            {
                currentTopic = null;
                for (Tab courseTab : courseTabPane.getTabs())
                {
                    if (currentCourse.getName()
                            .equals(((Label) ((Group) ((StackPane) courseTab.getGraphic()).getChildren().get(0)).getChildren().get(0)).getText()))
                    {
                        currentTab = courseTab;
                        updateSelectedTabLbl();
                    }
                }
            }
            return;
        }

        // Code to delete a course which only works if no topic was selected.
        if (currentCourse != null)
        {
            // Remove the Course object and its Tab. Then, update the user.
            notebook.getCourseList().remove(currentCourse);
            currentTabPane.getTabs().remove(currentTab);
            updateUser();

            // Code to set the "Current Tab" label to none.
            if (notebook.getCourseList().isEmpty())
            {
                tabSelectedLbl.setText("Current Tab: None Selected");
            }
        }
    }

    /**
     * Renames a course or topic tab and updates its object in the user's notebook.
     * There is a dedicated rename option for flashcards.
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

        // Limit to amount of characters due to size of Tab objects.
        if (renameTxtField.getText().length() > 20)
        {
            errorMsg.setText("* Name must be equal to or less than 20 characters");
            return;
        }

        errorMsg.setText("");

        // Rename the selected topic and update the notebook and "Current Tab" label.
        if (currentTopic != null)
        {
            currentTopic.setName(renameTxtField.getText());
            rotateTab(currentTab, renameTxtField.getText());
            updateUser();
            updateSelectedTabLbl();

            renameTxtField.clear();
            return;
        }

        // Rename the selected course and update the notebook and "Current Tab" label.
        if (currentCourse != null)
        {
            currentCourse.setName(renameTxtField.getText());
            rotateTab(currentTab, renameTxtField.getText());
            updateUser();
            updateSelectedTabLbl();

            renameTxtField.clear();
        }
    }

    /** Create a new flashcard and set up the FlashCard UI area. Also adjusts the program scope. */
    @FXML
    void createNewCardBtn()
    {
        IndexCard newIndexCard = new IndexCard("Enter Text Using Text-box", "Enter Text Using Text-box");
        currentTopic.getCardList().add(newIndexCard);
        displayedCardList = currentTopic.getCardList();

        currentIndexCard = newIndexCard;
        updateUser();
        displayCard();
        side.setText("Front");
        onFrontSide = true;
        makeCardElementsVisible(true);
        checkArrowVisibility();

        // Allow the randomize button to be used if there are multiple flashcards.
        if (currentTopic.getCardList().size() > 1)
        {
            randomizeBtn.setDisable(false);
        }
    }

    /** Method to delete a flashcard. Includes some QOL adjustments. */
    @FXML
    void deleteCardBtn()
    {
        // Will result in no flashcards and therefore adjusts the UI for that situation.
        if (displayedCardList.size() == 1)
        {
            currentTopic.getCardList().remove(currentIndexCard);
            displayedCardList.remove(currentIndexCard);

            makeDefaultCardElementsVisible();
            cardText.setText("");
            updateUser();
            checkArrowVisibility();
        }

        // There will still be flashcards after the deletion so this plans for that scenario.
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

        // Disables the randomize button when the deletion reduces the flashcard amount to less than 2.
        if (currentTopic.getCardList().size() < 2)
        {
            randomizeBtn.setDisable(true);
        }
    }

    /** Changes the flashcard text based on the text field below the Flashcard UI. */
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

    /**
     * Change default flashcard element visibility. Ignores delete button when there is no flashcard in the topicList.
     * This method is for when there is no flashcard available and only makes elements for creating a card available.
     */
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

    /** This method makes all buttons and field available and is used when there is a flashcard to edit/change. */
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

    /** Runs when the checkmark is clicked/changed. Updates the IndexCard object in the user's notebook. */
    @FXML
    void checkmarkAction()
    {
        currentIndexCard.setHasLearned(checkmark.isSelected());
        updateUser();
    }

    /**
     * Indicator which helps point out what the program thinks is the selected tab in the TabPane(s). This helps
     * mitigate errors in the Tab listeners since they seem to fire/not fire at unintended/intended moments. While the
     * current tab may be accurate, some actions such as delete or create may still not work as intended.
     */
    void updateSelectedTabLbl()
    {
        tabSelectedLbl.setText("Current Tab: " +
                ((Label) ((Group) ((StackPane) currentTab.getGraphic()).getChildren().get(0)).getChildren().get(0)).getText());
    }

    /**
     * Method called to display the currentIndexCard's information.
     * Does not require anything else besides currentIndexCard != null.
     */
    void displayCard()
    {
        if (onFrontSide)
            cardText.setText(currentIndexCard.getQuestion());
        else
            cardText.setText(currentIndexCard.getAnswer());

        checkmark.setSelected(currentIndexCard.hasLearned());
    }

    /** Flip the IndexCard by using the "onFrontSide" variable and change the Flashcard UI to match. */
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

    /**
     * Only sets functionality of moving to a flashcard left of the current one in the list.
     * Ability to actually use the left arrow is determined through checkArrowVisibility().
     */
    @FXML
    void leftArrowClick()
    {
        // Assumes that there is a flashcard to the left of the current one and switches to it.
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

    /**
     * Only sets functionality of moving to a flashcard right of the current one in the list.
     * Ability to actually use the right arrow is determined through checkArrowVisibility().
     */
    @FXML
    void rightArrowClick()
    {
        // Assumes that there is a flashcard to the right of the current one and switches to it.
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

    /** Shuffles the current displayedCardList (All/Learned/Unlearned) and displays the first card. */
    @FXML
    void randomizeCardList()
    {
        errorMsg.setText("");
        errorMsg.setVisible(false);

        Collections.shuffle(displayedCardList);
        currentIndexCard = displayedCardList.get(0);
        displayCard();
        checkArrowVisibility();
    }

    /** Displays all the flashcards in the order that they were created. */
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

    /** Displays all the flashcards that have been checked "learned". */
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

    /** Displays all the flashcards that are not checked as "learned". */
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

    /**
     * Determines whether the left/right arrow are disabled or not based on whether
     * there is a flashcard to the left/right of the current flashcard. Arrows are actually
     * images in an ImageView and therefore don't lose opacity when disabled. It needs to be done manually.
     */
    void checkArrowVisibility()
    {
        // Arrows are only enabled if there is more than one flashcard.
        if (displayedCardList.size() > 1)
        {
            for (int i = 0; i < displayedCardList.size(); i++)
            {
                // Identify the current flashcard.
                if (currentIndexCard == displayedCardList.get(i))
                {
                    // If the current flashcard is the first one, there's no flashcard to its left.
                    if (i == 0)
                    {
                        leftArrow.setOpacity(0.35);
                        leftArrow.setDisable(true);
                    }
                    else
                    {
                        // Otherwise, there is a flashcard to its left. This enables the left arrow.
                        leftArrow.setOpacity(1.0);
                        leftArrow.setDisable(false);
                    }

                    // If the current flashcard is the last one in the list, disable the right arrow.
                    if (i == displayedCardList.size() - 1)
                    {
                        rightArrow.setOpacity(0.35);
                        rightArrow.setDisable(true);
                    }
                    else
                    {
                        // Otherwise, enable the right arrow since there is a flashcard to its right.
                        rightArrow.setOpacity(1.0);
                        rightArrow.setDisable(false);
                    }
                }
            }
        }
        else
        {
            // Disable arrows if there is less than two flashcards.
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
