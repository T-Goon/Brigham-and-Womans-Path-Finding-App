package edu.wpi.teamB.util;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import edu.wpi.teamB.App;
import edu.wpi.teamB.database.DatabaseHandler;
import edu.wpi.teamB.entities.requests.*;
import edu.wpi.teamB.entities.User;
import edu.wpi.teamB.entities.requests.Request;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.Getter;

import java.io.IOException;
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
    private final JFXCheckBox complete;
    private  Label employeeName;
    private final TableView parentTable;
    private final JFXButton btnEdit;
    private final JFXButton btnDel;
    private ContextMenu contextMenu;

    public RequestWrapper(Request r, TableView parentTable) throws IOException {
        this.r = r;
        this.type = new Label(Request.RequestType.prettify(r.getRequestType()));
        this.time = new Label(r.getTime());
        this.date = new Label(r.getDate());
        this.complete = new JFXCheckBox();
        this.employeeName = new Label(r.getEmployeeName());
        if (employeeName.getText().equals("null")) employeeName.setText("Nobody");
        this.parentTable = parentTable;
        this.complete.setSelected(r.getComplete().equals("T"));

        //Setup context menu
        this.contextMenu = new ContextMenu();
        Menu assignMenu = new Menu("Assign");
        MenuItem unassignItem = new MenuItem("Unassign");

        List<MenuItem> staff = new ArrayList<>();
        for(User employee: DatabaseHandler.getDatabaseHandler("main.db").getUsersByAuthenticationLevel(User.AuthenticationLevel.STAFF)){
            MenuItem tempItem = new MenuItem(employee.getFirstName() + " " + employee.getLastName());
            tempItem.setOnAction(event -> {
                parentTable.getItems().removeIf((Object o) -> ((RequestWrapper) o).r.getRequestID().equals(r.getRequestID()));
                r.setEmployeeName(employee.getFirstName() + " " + employee.getLastName());

                DatabaseHandler.getDatabaseHandler("main.db").updateRequest(r);

                try {
                    parentTable.getItems().add(0, new RequestWrapper(r, parentTable));
                } catch (IOException e){
                    e.printStackTrace();
                }
            });
            staff.add(tempItem);
        }
        assignMenu.getItems().addAll(staff);

        unassignItem.setOnAction(event -> {
            parentTable.getItems().removeIf((Object o) -> ((RequestWrapper) o).r.getRequestID().equals(r.getRequestID()));
            r.setEmployeeName("null");
            DatabaseHandler.getDatabaseHandler("main.db").updateRequest(r);

            try {
                parentTable.getItems().add(0, new RequestWrapper(r, parentTable));
            } catch (IOException e){
                e.printStackTrace();
            }
        });

        this.contextMenu.getItems().add(assignMenu);
        this.contextMenu.getItems().add(unassignItem);

        //set context menu to the employee name label
        employeeName.setContextMenu(this.contextMenu);

        complete.setOnAction(event -> {
            r.setComplete(complete.isSelected() ? "T" : "F");
            DatabaseHandler.getDatabaseHandler("main.db").updateRequest(r);
        });

        // Set up edit button
        JFXButton btnEdit = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/edu/wpi/teamB/views/misc/tableEditBtn.fxml")));
        btnEdit.setId(r.getRequestID() + "EditBtn");

        btnEdit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = App.getPrimaryStage();
                stage.setUserData(r.getRequestID());

                switch(r.getRequestType()) {
                    case SANITATION:
                        SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/menus/serviceRequestDatabase.fxml", "/edu/wpi/teamB/views/requestForms/sanitationRequestForm.fxml");
                        break;
                    case MEDICINE:
                        SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/menus/serviceRequestDatabase.fxml", "/edu/wpi/teamB/views/requestForms/medDeliveryRequestForm.fxml");
                        break;
                    case INTERNAL_TRANSPORT:
                        SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/menus/serviceRequestDatabase.fxml", "/edu/wpi/teamB/views/requestForms/internalTransportationRequestForm.fxml");
                        break;
                    case RELIGIOUS:
                        SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/menus/serviceRequestDatabase.fxml", "/edu/wpi/teamB/views/requestForms/religiousRequestForm.fxml");
                        break;
                    case FOOD:
                        SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/menus/serviceRequestDatabase.fxml", "/edu/wpi/teamB/views/requestForms/foodDeliveryRequestForm.fxml");
                        break;
                    case FLORAL:
                        SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/menus/serviceRequestDatabase.fxml", "/edu/wpi/teamB/views/requestForms/floralDeliveryRequestForm.fxml");
                        break;
                    case SECURITY:
                        SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/menus/serviceRequestDatabase.fxml", "/edu/wpi/teamB/views/requestForms/securityRequestForm.fxml");
                        break;
                    case EXTERNAL_TRANSPORT:
                        SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/menus/serviceRequestDatabase.fxml", "/edu/wpi/teamB/views/requestForms/externalTransportationRequestForm.fxml");
                        break;
                    case LAUNDRY:
                        SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/menus/serviceRequestDatabase.fxml", "/edu/wpi/teamB/views/requestForms/laundryRequestForm.fxml");
                        break;
                    case CASE_MANAGER:
                        SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/menus/serviceRequestDatabase.fxml", "/edu/wpi/teamB/views/requestForms/caseManagerRequestForm.fxml");
                        break;
                    case SOCIAL_WORKER:
                        SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/menus/serviceRequestDatabase.fxml", "/edu/wpi/teamB/views/requestForms/socialWorkerRequestForm.fxml");
                        break;
                }
            }
        });


        this.btnEdit = btnEdit;

        // Set up delete button
        JFXButton btnDel = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/edu/wpi/teamB/views/misc/tableDelBtn.fxml")));
        btnDel.setId(r.getRequestID() + "DelBtn");

        btnDel.setOnAction(event -> {
            DatabaseHandler.getDatabaseHandler("main.db").removeRequest(r);
            parentTable.getItems().removeIf((Object o) -> ((RequestWrapper) o).r.getRequestID().equals(r.getRequestID()));
        });

        this.btnDel = btnDel;
    }
}
