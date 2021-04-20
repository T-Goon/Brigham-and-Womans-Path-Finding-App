package edu.wpi.teamB.util;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import edu.wpi.teamB.App;
import edu.wpi.teamB.database.DatabaseHandler;
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
public class UserWrapper {

    private final User u;
    private final Label username;
    private final Label firstName;
    private final Label lastName;
    private  Label employeeName;
    private final TableView parentTable;
    private final JFXButton btnEdit;
    private final JFXButton btnDel;

    public UserWrapper(User u, TableView parentTable) throws IOException {
        this.u = u;
        this.username = new Label(u.getUsername());
        this.firstName = new Label(u.getFirstName());
        this.lastName = new Label(u.getLastName());
        StringBuilder jobsBuilder = new StringBuilder();
        for(Request.RequestType job : u.getJobs()){
            jobsBuilder.append(Request.RequestType.prettify(job));
            jobsBuilder.append(", ");
        }
        if(jobsBuilder.length()>=2){
            jobsBuilder.deleteCharAt(jobsBuilder.length()-1);
            jobsBuilder.deleteCharAt(jobsBuilder.length()-1);
        }
        this.employeeName = new Label(jobsBuilder.toString());
        if (employeeName.getText().equals("null")) employeeName.setText("No responsibilities");
        this.parentTable = parentTable;

        // Set up edit button
        JFXButton btnEdit = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/edu/wpi/teamB/views/misc/tableEditBtn.fxml")));
        btnEdit.setId(u.getUsername() + "EditBtn");

        btnEdit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = App.getPrimaryStage();
                stage.setUserData(u);
                SceneSwitcher.switchScene(getClass(), "this is broken", "this is broken");
            }
        });


        this.btnEdit = btnEdit;

        // Set up delete button
        JFXButton btnDel = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/edu/wpi/teamB/views/misc/tableDelBtn.fxml")));
        btnDel.setId(u.getUsername() + "DelBtn");

        btnDel.setOnAction(event -> {
            DatabaseHandler.getDatabaseHandler("main.db").deleteUser(u.getUsername());
            parentTable.getItems().removeIf((Object o) -> ((UserWrapper) o).u.getUsername().equals(u.getUsername()));
        });

        this.btnDel = btnDel;
    }
}
