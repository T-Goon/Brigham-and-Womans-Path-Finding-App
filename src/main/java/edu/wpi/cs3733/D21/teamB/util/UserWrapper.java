package edu.wpi.cs3733.D21.teamB.util;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.D21.teamB.App;
import edu.wpi.cs3733.D21.teamB.database.DatabaseHandler;
import edu.wpi.cs3733.D21.teamB.entities.User;
import edu.wpi.cs3733.D21.teamB.entities.requests.Request;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.Getter;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

@Getter
@SuppressWarnings("unchecked")
public class UserWrapper {

    private final User u;
    private final Label username;
    private final Label firstName;
    private final Label lastName;
    private Label employeeName;
    private final TableView parentTable;
    private final JFXButton btnEdit;
    private final JFXButton btnDel;

    public UserWrapper(User u, TableView parentTable) throws IOException {
        this.u = u;
        this.username = new Label(u.getUsername());
        this.firstName = new Label(u.getFirstName());
        this.lastName = new Label(u.getLastName());
        StringBuilder jobsBuilder = new StringBuilder();
        for (Request.RequestType job : u.getJobs()) {
            jobsBuilder.append(Request.RequestType.prettify(job));
            jobsBuilder.append(", ");
        }
        if (jobsBuilder.length() >= 2) {
            jobsBuilder.deleteCharAt(jobsBuilder.length() - 1);
            jobsBuilder.deleteCharAt(jobsBuilder.length() - 1);
        }
        this.employeeName = new Label(jobsBuilder.toString());
        if (employeeName.getText().equals("")) employeeName.setText("No responsibilities");
        this.parentTable = parentTable;

        // Set up edit button
        JFXButton btnEdit = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/edu/wpi/cs3733/D21/teamB/views/misc/tableEditBtn.fxml")));
        btnEdit.setId(u.getUsername() + "EditBtn");

        btnEdit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = App.getPrimaryStage();
                stage.setUserData(u);
                SceneSwitcher.addingUser = false;
                SceneSwitcher.switchScene(getClass(), "/edu/wpi/cs3733/D21/teamB/views/menus/userInformationDatabase.fxml", "/edu/wpi/cs3733/D21/teamB/views/menus/editUserMenu.fxml");
            }
        });


        this.btnEdit = btnEdit;

        // Set up delete button
        JFXButton btnDel = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/edu/wpi/cs3733/D21/teamB/views/misc/tableDelBtn.fxml")));
        btnDel.setId(u.getUsername() + "DelBtn");

        btnDel.setOnAction(event -> {
            try {
                DatabaseHandler.getDatabaseHandler("main.db").deleteUser(u.getUsername());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            parentTable.getItems().removeIf((Object o) -> ((UserWrapper) o).u.getUsername().equals(u.getUsername()));
        });

        this.btnDel = btnDel;
        if (u.getUsername().equals(DatabaseHandler.getDatabaseHandler("main.db").getAuthenticationUser().getUsername())) {
            this.btnDel.setDisable(true);
        }
    }
}
