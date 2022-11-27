package application.screens.topic;

import application.Main;
import application.managers.Course;
import application.managers.IndexCard;
import application.managers.Topic;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.util.HashMap;

public class TopicScreenController {

    @FXML
    private Button addCardBtn;

    @FXML
    private TabPane cardList;

    @FXML
    private Label cardSideLabel;

    @FXML
    private TextArea cardTextArea;

    @FXML
    private Button deleteCardBtn;

    @FXML
    private Button editCardBtn;

    @FXML
    private Button flipCardBtn;

    @FXML
    private Button saveBtn;

    @FXML
    private Button cancelBtn;

    @FXML
    private RadioButton learnedBox;

    @FXML
    private Label topicPageTitle;
    private Topic topic;

    private boolean isShowingFrontSide = true;

    private HashMap<Tab, IndexCard> cardMap = new HashMap<>();

    @FXML
    void initialize() {
        cardTextArea.setEditable(false);
        saveBtn.setDisable(true);
        cancelBtn.setDisable(true);
    }

    private void enableEdit(IndexCard card) {
        saveBtn.setDisable(false);
        cancelBtn.setDisable(false);
        cardList.setDisable(true);
        deleteCardBtn.setDisable(false);
        cardTextArea.setEditable(true);
        flipCardBtn.setDisable(true);
        if (isShowingFrontSide) {
            cardSideLabel.setText("Editing Question");
            cardTextArea.setText(card.getQuestion());
        } else {
            cardSideLabel.setText("Editing Answer");
            cardTextArea.setText(card.getAnswer());
        }
    }

    private void disableEdit(IndexCard card) {
        flipCardBtn.setDisable(false);
        deleteCardBtn.setDisable(false);
        saveBtn.setDisable(true);
        cancelBtn.setDisable(true);
        cardList.setDisable(false);
        cardTextArea.setEditable(false);
        if (isShowingFrontSide) {
            cardTextArea.setText(card.getQuestion());
            cardSideLabel.setText("Question");
        } else {
            cardSideLabel.setText("Answer");
            cardTextArea.setText(card.getAnswer());
        }
    }

    @FXML
    void onNewCardClick(MouseEvent event) {
        Tab newTab = new Tab();
        Label name = new Label("Unnamed Card " + (cardList.getTabs().size() + 1));
        name.setMaxSize(cardList.getTabMaxWidth(), cardList.getTabMaxHeight());
        newTab.setGraphic(name);
        cardList.getTabs().add(newTab);

        IndexCard card = new IndexCard("Sample Question", "Sample Answer");
        topic.add(card);

        cardMap.put(newTab, card);
    }

    @FXML
    void onCancelBtnClick(MouseEvent event) {
        int selectedIndex = cardList.getSelectionModel().getSelectedIndex();

        //Finds current Tab
        Tab currentTab = cardList.getTabs().get(selectedIndex);
        IndexCard currentCard = cardMap.get(currentTab);
        disableEdit(currentCard);
    }

    @FXML
    void onCardListClicked(MouseEvent event) {
        isShowingFrontSide = true;
        if (cardList.getTabs().size() > 0) {
            int selectedIndex = cardList.getSelectionModel().getSelectedIndex();

            //Finds current Tab
            Tab currentTab = cardList.getTabs().get(selectedIndex);
            IndexCard currentCard = cardMap.get(currentTab);
            cardSideLabel.setText("Question");
            cardTextArea.setText(currentCard.getQuestion());
            learnedBox.setSelected(currentCard.hasLearned());
        }
    }

    @FXML
    void onDeleteCardClick(MouseEvent event) {
        if (cardList.getTabs().size() > 0) {
            int selectedIndex = cardList.getSelectionModel().getSelectedIndex();

            //Finds current Tab
            Tab currentTab = cardList.getTabs().get(selectedIndex);
            IndexCard currentCard = cardMap.get(currentTab);

            if (currentCard != null) {
                topic.delete(currentCard);
                Tab removedTab = cardList.getTabs().remove(selectedIndex);
                cardMap.remove(removedTab);
            }
        }
    }

    @FXML
    void onEditCardClick(MouseEvent event) {
        if (cardList.getTabs().size() > 0) {
            int selectedIndex = cardList.getSelectionModel().getSelectedIndex();

            //Finds current Tab
            Tab currentTab = cardList.getTabs().get(selectedIndex);
            IndexCard currentCard = cardMap.get(currentTab);
            enableEdit(currentCard);
        }
    }

    @FXML
    void onFlipBtnClick(MouseEvent event) {
        isShowingFrontSide = !isShowingFrontSide;
        if (cardList.getTabs().size() > 0) {
            int selectedIndex = cardList.getSelectionModel().getSelectedIndex();

            //Finds current Tab
            Tab currentTab = cardList.getTabs().get(selectedIndex);
            IndexCard currentCard = cardMap.get(currentTab);

            if (isShowingFrontSide) {
                cardTextArea.setText(currentCard.getQuestion());
                cardSideLabel.setText("Question");
            } else {
                cardTextArea.setText(currentCard.getAnswer());
                cardSideLabel.setText("Answer");
            }

        }
    }

    @FXML
    void onSaveBtnClick(MouseEvent event) {
        if (cardList.getTabs().size() > 0) {
            int selectedIndex = cardList.getSelectionModel().getSelectedIndex();

            //Finds current Tab
            Tab currentTab = cardList.getTabs().get(selectedIndex);
            IndexCard currentCard = cardMap.get(currentTab);
            if (isShowingFrontSide) {
                currentCard.setQuestion(cardTextArea.getText());
            } else {
                currentCard.setAnswer(cardTextArea.getText());
            }
            disableEdit(currentCard);
        }
    }

    @FXML
    void onLearnedClick(MouseEvent event) {
        if (cardList.getTabs().size() > 0) {
            int selectedIndex = cardList.getSelectionModel().getSelectedIndex();

            //Finds current Tab
            Tab currentTab = cardList.getTabs().get(selectedIndex);
            IndexCard currentCard = cardMap.get(currentTab);
            if (learnedBox.isSelected())
                currentCard.setHasLearned(true);
            else
                currentCard.setHasLearned(false);
        }
    }

    @FXML
    void onBackBtnClicked(MouseEvent event) {
        Main.loadScreen(event, "screens/home/homeScreen.fxml", "Home");
    }
    public void setTitle(String title) {
        topicPageTitle.setText(title);
    }

    public void setTopic(Topic topic) {
        if (topic != null)
            this.topic = topic;
        else
            System.out.println("Can only set topic once");
    }

}
