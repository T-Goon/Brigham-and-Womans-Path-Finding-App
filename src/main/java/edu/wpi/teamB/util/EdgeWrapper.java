package edu.wpi.teamB.util;

import com.jfoenix.controls.JFXButton;
import edu.wpi.teamB.App;
import edu.wpi.teamB.database.DatabaseHandler;
import edu.wpi.teamB.entities.Edge;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import lombok.Getter;

import java.io.IOException;
import java.util.Objects;

@Getter
@SuppressWarnings("unchecked")
public class EdgeWrapper {

    private final Label id;
    private final Label startNode;
    private final Label endNode;
    private final JFXButton btnEdit;
    private final JFXButton btnDel;
    private final TableView parentTable;

    public EdgeWrapper(Edge e, TableView parentTable) throws IOException {
        this.id = new Label(e.getEdgeID());
        this.startNode = new Label(e.getStartNodeName());
        this.endNode = new Label(e.getEndNodeName());
        this.parentTable = parentTable;

        // Set up edit button
        JFXButton btnEdit = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/edu/wpi/teamB/views/misc/nodeEdgeEditBtn.fxml")));
        btnEdit.setId(e.getEdgeID() + "EditBtn");

        btnEdit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = App.getPrimaryStage();
                stage.setUserData(e);
                try {
                    SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/mapEditor/edges/editEdgeMenu.fxml");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        this.btnEdit = btnEdit;

        // Set up delete button
        JFXButton btnDel = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/edu/wpi/teamB/views/misc/nodeEdgeDelBtn.fxml")));
        btnDel.setId(e.getEdgeID() + "DelBtn");

        btnDel.setOnAction(event -> {
            DatabaseHandler.getDatabaseHandler("main.db").removeEdge(e.getEdgeID());
            parentTable.getItems().removeIf( (Object o) -> ((EdgeWrapper) o).id == id);
        });

        this.btnDel = btnDel;
    }
}
