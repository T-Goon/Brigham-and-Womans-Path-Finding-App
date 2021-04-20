package edu.wpi.teamB.views.menus;

import edu.wpi.teamB.database.DatabaseHandler;
import edu.wpi.teamB.entities.requests.Request;
import edu.wpi.teamB.util.RequestWrapper;
import javafx.application.Platform;

import com.jfoenix.controls.JFXButton;
import edu.wpi.teamB.util.SceneSwitcher;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

@SuppressWarnings("unchecked") // Added so Java doesn't get mad at the raw use of TableView that is necessary
public class ServiceRequestDatabaseController implements Initializable {
    @FXML
    private JFXButton btnBack;

    @FXML
    private JFXButton btnExit;

    @FXML
    private JFXButton btnEmergency;

    @FXML
    private TableView tblRequests;

    @FXML
    private TableColumn<String, JFXButton> editCol;

    @FXML
    private TableColumn<String, JFXButton> typeCol;

    @FXML
    private TableColumn<String, JFXButton> timeCol;

    @FXML
    private TableColumn<String, JFXButton> dateCol;

    @FXML
    private TableColumn<String, JFXButton> completeCol;

    @FXML
    private TableColumn<String, JFXButton> employeeCol;

    @FXML
    private TableColumn<String, JFXButton> delCol;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Map<String, Request> requests = DatabaseHandler.getDatabaseHandler("main.db").getRequests();
        ObservableList<TableColumn<String, Label>> cols = tblRequests.getColumns();
        for (TableColumn<String, Label> c : cols) {
            switch (c.getId()) {
                case "editCol":
                    c.setCellValueFactory(new PropertyValueFactory<>("btnEdit"));
                    break;
                case "typeCol":
                    c.setCellValueFactory(new PropertyValueFactory<>("type"));
                    break;
                case "timeCol":
                    c.setCellValueFactory(new PropertyValueFactory<>("time"));
                    break;
                case "dateCol":
                    c.setCellValueFactory(new PropertyValueFactory<>("date"));
                    break;
                case "completeCol":
                    c.setCellValueFactory(new PropertyValueFactory<>("complete"));
                    break;
                case "employeeCol":
                    c.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
                    break;
                case "delCol":
                    c.setCellValueFactory(new PropertyValueFactory<>("btnDel"));
                    break;
            }
        }

        if (requests != null) {
            for (Request r : requests.values()) {
                try {
                    tblRequests.getItems().add(new RequestWrapper(r, tblRequests));
                } catch (IOException err) {
                    err.printStackTrace();
                }
            }
        }
    }

    @FXML
    private void handleButtonAction(ActionEvent e) {
        JFXButton btn = (JFXButton) e.getSource();

        switch (btn.getId()) {
            case "btnBack":
                SceneSwitcher.goBack(getClass(), 1);
                break;
            case "btnExit":
                Platform.exit();
                break;
            case "btnEmergency":
                SceneSwitcher.switchToTemp(getClass(), "/edu/wpi/teamB/views/requestForms/emergencyForm.fxml");
                break;
        }
    }
}
