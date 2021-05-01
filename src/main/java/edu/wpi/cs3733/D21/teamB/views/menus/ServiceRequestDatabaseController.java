package edu.wpi.cs3733.D21.teamB.views.menus;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import edu.wpi.cs3733.D21.teamB.database.DatabaseHandler;
import edu.wpi.cs3733.D21.teamB.entities.User;
import edu.wpi.cs3733.D21.teamB.entities.requests.Request;
import edu.wpi.cs3733.D21.teamB.util.RequestWrapper;
import edu.wpi.cs3733.D21.teamB.util.SceneSwitcher;
import edu.wpi.cs3733.D21.teamB.views.BasePageController;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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
public class   ServiceRequestDatabaseController extends BasePageController implements Initializable {

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
        Map<String, Request> allRequests;
        try {
            allRequests = DatabaseHandler.getHandler().getRequests();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        Map<String, Request> requests = new HashMap<>();

        User user = DatabaseHandler.getHandler().getAuthenticationUser();
        User.AuthenticationLevel level = user.getAuthenticationLevel();
        String username = user.getUsername();
        String employeeName = user.getFirstName() + " " + user.getLastName();
        if (level == User.AuthenticationLevel.ADMIN) {
            requests = allRequests;
        } else if (level == User.AuthenticationLevel.STAFF) {
            if (allRequests != null) {
                for (Request request : allRequests.values()) {
                    if (request.getEmployeeName().equals(employeeName) || request.getSubmitter().equals(username) || request.getRequestType().equals(Request.RequestType.EMERGENCY)) {
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
                    c.setCellValueFactory(new PropertyValueFactory<>("progress"));
                    break;
                case "employeeCol":
                    c.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
                    break;
                case "delCol":
                    c.setCellValueFactory(new PropertyValueFactory<>("btnDel"));
                    break;
            }
        }

        setRowColor();

        if (requests != null) {
            for (Request r : requests.values()) {
                try {
                    tblRequests.getItems().add(new RequestWrapper(r, tblRequests, this));
                } catch (IOException err) {
                    err.printStackTrace();
                }
            }
        }
    }

    @FXML
    public void handleButtonAction(ActionEvent e) {
        final String currentPath = "/edu/wpi/cs3733/D21/teamB/views/menus/serviceRequestDatabase.fxml";
        super.handleButtonAction(e);
        JFXButton btn = (JFXButton) e.getSource();
        switch (btn.getId()) {
            case "btnHelp":
                loadHelpDialog();
                break;
            case "btnEmergency":
                SceneSwitcher.isEmergencyBtn = true;
                SceneSwitcher.switchScene(getClass(), currentPath, "/edu/wpi/cs3733/D21/teamB/views/requestForms/emergencyForm.fxml");
                break;
        }
    }

    private void loadHelpDialog() {
        JFXDialogLayout helpLayout = new JFXDialogLayout();

        Text helpText = new Text("To assign an employee to a request, right click on the text in the 'Assigned To' column and select a staff member from the context menu.\n" +
                "To set the current status of the request, right click on the text in the 'Complete' column and select the status of the request.\n");
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

    public void setRowColor() {
        TableColumn<String, Label> tc = (TableColumn<String, Label>) tblRequests.getColumns().get(1);
        tc.setCellFactory(column -> new TableCell<String, Label>() {
            @Override
            protected void updateItem(Label item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(item);
                TableRow<String> currentRow = getTableRow();
                if (!isEmpty()) {
                    if (item.getText().equals("Emergency")) {
                        currentRow.setStyle("-fx-background-color: #CE2029");
                    } else if (getTableRow().getIndex() % 2 == 0) {
                        currentRow.setStyle("-fx-background-color: #0264AA");
                    } else if (getTableRow().getIndex() % 2 != 0) {
                        currentRow.setStyle("-fx-background-color: #0067B1");
                    }

                }
            }
        });
        tc = (TableColumn<String, Label>) tblRequests.getColumns().get(5);
        tc.setCellFactory(column -> new TableCell<String, Label>() {
            @Override
            protected void updateItem(Label item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(item);
                TableRow<String> currentRow = getTableRow();
                if (!isEmpty()) {
                    if (item.getText().equals("Nobody")) {
                        currentRow.setStyle("-fx-background-color: #CE2029");
                    }

                }
            }
        });
    }
}