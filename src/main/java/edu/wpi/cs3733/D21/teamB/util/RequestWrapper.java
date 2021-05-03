package edu.wpi.cs3733.D21.teamB.util;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.D21.teamB.App;
import edu.wpi.cs3733.D21.teamB.database.DatabaseHandler;
import edu.wpi.cs3733.D21.teamB.entities.User;
import edu.wpi.cs3733.D21.teamB.entities.requests.Request;
import edu.wpi.cs3733.D21.teamB.views.menus.ServiceRequestDatabaseController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.Getter;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@SuppressWarnings("unchecked")
public class RequestWrapper {

    private final Request r;
    private final Label type;
    private final Label time;
    private final Label date;
    private final Label progress;
    private final Label employeeName;
    private final TableView parentTable;
    private final JFXButton btnEdit;
    private final JFXButton btnDel;
    private final ContextMenu completeMenu;
    private final ContextMenu contextMenu;
    private final ServiceRequestDatabaseController controller;

    public RequestWrapper(Request r, TableView parentTable, ServiceRequestDatabaseController controller) throws IOException {
        this.r = r;
        this.type = new Label(Request.RequestType.prettify(r.getRequestType()));
        this.time = new Label(r.getTime());
        this.date = new Label(r.getDate());
        switch (r.getProgress()) {
            case "F":
                this.progress = new Label("Not Started");
                break;
            case "P":
                this.progress = new Label("In Progress");
                break;
            case "T":
                this.progress = new Label("Complete");
                break;
            default:
                throw new IllegalStateException("How did we get here?");
        }
        this.employeeName = new Label(r.getEmployeeName());
        if (employeeName.getText().equals("null")) employeeName.setText("Nobody");
        this.parentTable = parentTable;

        DatabaseHandler db = DatabaseHandler.getHandler();
        this.completeMenu = new ContextMenu();
        Menu menu = new Menu("Progress:");
        MenuItem notComplete = new MenuItem("Not Started");
        MenuItem inProgress = new MenuItem("In Progress");
        MenuItem complete = new MenuItem("Complete");
        notComplete.setOnAction(e -> {
            progress.setText("Not Started");
            r.setProgress("F");
            try {
                db.updateRequest(r);
            } catch (SQLException err) {
                err.printStackTrace();
            }
        });
        inProgress.setOnAction(e -> {
            progress.setText("In Progress");
            r.setProgress("P");
            try {
                db.updateRequest(r);
            } catch (SQLException err) {
                err.printStackTrace();
            }
        });
        complete.setOnAction(e -> {
            progress.setText("Complete");
            r.setProgress("T");
            try {
                db.updateRequest(r);
            } catch (SQLException err) {
                err.printStackTrace();
            }
        });

        menu.getItems().add(notComplete);
        menu.getItems().add(inProgress);
        menu.getItems().add(complete);
        completeMenu.getItems().add(menu);
        progress.setContextMenu(completeMenu);
        this.controller = controller;

        //Setup context menu
        this.contextMenu = new ContextMenu();
        Menu assignMenu = new Menu("Assign");
        MenuItem unassignItem = new MenuItem("Unassign");

        List<MenuItem> staff = new ArrayList<>();
        List<User> users = db.getUsersByAuthenticationLevel(User.AuthenticationLevel.STAFF);
        if (users != null) {
            for (User employee : users) {
                MenuItem tempItem = new MenuItem(employee.getFirstName() + " " + employee.getLastName());
                tempItem.setOnAction(event -> {
                    parentTable.getItems().removeIf((Object o) -> ((RequestWrapper) o).r.getRequestID().equals(r.getRequestID()));
                    r.setEmployeeName(employee.getFirstName() + " " + employee.getLastName());

                    // Try to update the request
                    try {
                        db.updateRequest(r);
                    } catch (SQLException e) {
                        e.printStackTrace();
                        return;
                    }

                    try {
                        parentTable.getItems().add(0, new RequestWrapper(r, parentTable, controller));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                staff.add(tempItem);
            }
        }
        assignMenu.getItems().addAll(staff);

        unassignItem.setOnAction(event -> {
            parentTable.getItems().removeIf((Object o) -> ((RequestWrapper) o).r.getRequestID().equals(r.getRequestID()));
            r.setEmployeeName("null");

            try {
                db.updateRequest(r);
            } catch (SQLException e) {
                e.printStackTrace();
                return;
            }

            try {
                parentTable.getItems().add(0, new RequestWrapper(r, parentTable, controller));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        this.contextMenu.getItems().add(assignMenu);
        this.contextMenu.getItems().add(unassignItem);

        //set context menu to the employee name label
        employeeName.setContextMenu(this.contextMenu);

        // Set up edit button
        JFXButton btnEdit = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/edu/wpi/cs3733/D21/teamB/views/misc/tableEditBtn.fxml")));
        btnEdit.setId(r.getRequestID() + "EditBtn");

        btnEdit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = App.getPrimaryStage();
                stage.setUserData(r.getRequestID());

                switch (r.getRequestType()) {
                    case SANITATION:
                        SceneSwitcher.switchScene(getClass(), "/edu/wpi/cs3733/D21/teamB/views/menus/serviceRequestDatabase.fxml", "/edu/wpi/cs3733/D21/teamB/views/requestForms/sanitationRequestForm.fxml");
                        break;
                    case MEDICINE:
                        SceneSwitcher.switchScene(getClass(), "/edu/wpi/cs3733/D21/teamB/views/menus/serviceRequestDatabase.fxml", "/edu/wpi/cs3733/D21/teamB/views/requestForms/medDeliveryRequestForm.fxml");
                        break;
                    case INTERNAL_TRANSPORT:
                        SceneSwitcher.switchScene(getClass(), "/edu/wpi/cs3733/D21/teamB/views/menus/serviceRequestDatabase.fxml", "/edu/wpi/cs3733/D21/teamB/views/requestForms/internalTransportationRequestForm.fxml");
                        break;
                    case RELIGIOUS:
                        SceneSwitcher.switchScene(getClass(), "/edu/wpi/cs3733/D21/teamB/views/menus/serviceRequestDatabase.fxml", "/edu/wpi/cs3733/D21/teamB/views/requestForms/religiousRequestForm.fxml");
                        break;
                    case FOOD:
                        SceneSwitcher.switchScene(getClass(), "/edu/wpi/cs3733/D21/teamB/views/menus/serviceRequestDatabase.fxml", "/edu/wpi/cs3733/D21/teamB/views/requestForms/foodDeliveryRequestForm.fxml");
                        break;
                    case FLORAL:
                        SceneSwitcher.switchScene(getClass(), "/edu/wpi/cs3733/D21/teamB/views/menus/serviceRequestDatabase.fxml", "/edu/wpi/cs3733/D21/teamB/views/requestForms/floralDeliveryRequestForm.fxml");
                        break;
                    case SECURITY:
                        SceneSwitcher.switchScene(getClass(), "/edu/wpi/cs3733/D21/teamB/views/menus/serviceRequestDatabase.fxml", "/edu/wpi/cs3733/D21/teamB/views/requestForms/securityRequestForm.fxml");
                        break;
                    case EXTERNAL_TRANSPORT:
                        SceneSwitcher.switchScene(getClass(), "/edu/wpi/cs3733/D21/teamB/views/menus/serviceRequestDatabase.fxml", "/edu/wpi/cs3733/D21/teamB/views/requestForms/externalTransportationRequestForm.fxml");
                        break;
                    case LAUNDRY:
                        SceneSwitcher.switchScene(getClass(), "/edu/wpi/cs3733/D21/teamB/views/menus/serviceRequestDatabase.fxml", "/edu/wpi/cs3733/D21/teamB/views/requestForms/laundryRequestForm.fxml");
                        break;
                    case CASE_MANAGER:
                        SceneSwitcher.switchScene(getClass(), "/edu/wpi/cs3733/D21/teamB/views/menus/serviceRequestDatabase.fxml", "/edu/wpi/cs3733/D21/teamB/views/requestForms/caseManagerRequestForm.fxml");
                        break;
                    case SOCIAL_WORKER:
                        SceneSwitcher.switchScene(getClass(), "/edu/wpi/cs3733/D21/teamB/views/menus/serviceRequestDatabase.fxml", "/edu/wpi/cs3733/D21/teamB/views/requestForms/socialWorkerRequestForm.fxml");
                        break;
                    case GIFT:
                        SceneSwitcher.switchScene(getClass(), "/edu/wpi/cs3733/D21/teamB/views/menus/serviceRequestDatabase.fxml", "/edu/wpi/cs3733/D21/teamB/views/requestForms/giftRequestForm.fxml");
                        break;
                    case LANGUAGE:
                        SceneSwitcher.switchScene(getClass(), "/edu/wpi/cs3733/D21/teamB/views/menus/serviceRequestDatabase.fxml", "/edu/wpi/cs3733/D21/teamB/views/requestForms/languageRequestForm.fxml");
                        break;
                    case EMERGENCY:
                        SceneSwitcher.isEmergencyBtn = false;
                        SceneSwitcher.switchScene(getClass(), "/edu/wpi/cs3733/D21/teamB/views/menus/serviceRequestDatabase.fxml", "/edu/wpi/cs3733/D21/teamB/views/requestForms/emergencyForm.fxml");
                        break;
                    case COVID:
                        SceneSwitcher.switchScene(getClass(),"/edu/wpi/cs3733/D21/teamB/views/menus/serviceRequestDatabase.fxml", "/edu/wpi/cs3733/D21/teamB/views/requestForms/covidRequestForm.fxml");
                        break;
                    default:
                        throw new IllegalStateException("How did we get here?");
                }
            }
        });


        this.btnEdit = btnEdit;

        // Set up delete button
        JFXButton btnDel = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/edu/wpi/cs3733/D21/teamB/views/misc/tableDelBtn.fxml")));
        btnDel.setId(r.getRequestID() + "DelBtn");

        btnDel.setOnAction(event -> {
            try {
                db.removeRequest(r);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            parentTable.getItems().removeIf((Object o) -> ((RequestWrapper) o).r.getRequestID().equals(r.getRequestID()));
            controller.setRowColor();
        });

        this.btnDel = btnDel;
    }
}
