package application.screens.home;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;

public class HomeScreenController {

    @FXML
    private TabPane courseList;

    @FXML
    private Button deleteCourseBtn;

    @FXML
    private Button newCourseBtn;

    @FXML
    void onDeleteTabClick(MouseEvent event) {
        if (courseList.getTabs().size() > 0) {
            int selectedIndex = courseList.getSelectionModel().getSelectedIndex();
            courseList.getTabs().remove(selectedIndex);
        }
    }

    @FXML
    void onNewTabClick(MouseEvent event) {
        Tab testTab = new Tab();
        testTab.setText("test tab " + courseList.getTabs().size());
        courseList.getTabs().add(testTab);
    }

}
