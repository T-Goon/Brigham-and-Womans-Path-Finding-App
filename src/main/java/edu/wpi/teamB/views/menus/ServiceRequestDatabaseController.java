package edu.wpi.teamB.views.menus;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import edu.wpi.teamB.database.DatabaseHandler;
import edu.wpi.teamB.entities.User;
import edu.wpi.teamB.entities.requests.Request;
import edu.wpi.teamB.util.RequestWrapper;
import edu.wpi.teamB.views.BasePageController;
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
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

@SuppressWarnings("unchecked") // Added so Java doesn't get mad at the raw use of TableView that is necessary
public class ServiceRequestDatabaseController extends BasePageController implements Initializable {

    @FXML
    private TableView tblRequests;

    @FXML
    private StackPane stackContainer;

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
        Map<String, Request> allRequests = null;
        try {
            allRequests = DatabaseHandler.getDatabaseHandler("main.db").getRequests();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        Map<String, Request> requests = new HashMap<String, Request>();

        User user = DatabaseHandler.getDatabaseHandler("main.db").getAuthenticationUser();
        User.AuthenticationLevel level = user.getAuthenticationLevel();
        String username = user.getUsername();
        String employeeName = user.getFirstName() + " " + user.getLastName();
        if (level == User.AuthenticationLevel.ADMIN) {
            requests = allRequests;
        } else if (level == User.AuthenticationLevel.STAFF) {
            if (allRequests != null) {
                for (Request request : allRequests.values()) {
                    if (request.getEmployeeName().equals(employeeName)) {
                        requests.put(request.getRequestID(), request);
                    } else if (request.getSubmitter().equals(username)) {
                        requests.put(request.getRequestID(), request);
                    }
                }
            }
        }

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
    public void handleButtonAction(ActionEvent e) {
        final String currentPath = "/edu/wpi/teamB/views/menus/serviceRequestDatabase.fxml";
        super.handleButtonAction(e);
        JFXButton btn = (JFXButton) e.getSource();
        switch (btn.getId()) {
            case "btnHelp":
                loadHelpDialog();
                break;
            case "btnEmergency":
                SceneSwitcher.switchScene(getClass(), currentPath, "/edu/wpi/teamB/views/requestForms/emergencyForm.fxml");
                break;
        }
    }

    private void loadHelpDialog() {
        JFXDialogLayout helpLayout = new JFXDialogLayout();

        Text helpText = new Text("To assign an employee to a request right click on the text in the 'assigned to' column and select a staff member from the context menu");
        helpText.setFont(new Font("MS Reference Sans Serif", 14));

        Label headerLabel = new Label("Help");
        headerLabel.setFont(new Font("MS Reference Sans Serif", 18));

        helpLayout.setHeading(headerLabel);
        helpLayout.setBody(helpText);
        JFXDialog helpWindow = new JFXDialog(stackContainer, helpLayout, JFXDialog.DialogTransition.CENTER);

        JFXButton button = new JFXButton("Close");
        button.setOnAction(event -> helpWindow.close());
        helpLayout.setActions(button);

        helpWindow.show();

    }
}
