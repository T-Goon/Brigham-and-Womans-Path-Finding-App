package edu.wpi.teamB.util;

import com.jfoenix.controls.JFXButton;
import edu.wpi.teamB.App;
import edu.wpi.teamB.database.DatabaseHandler;
import edu.wpi.teamB.entities.Edge;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import lombok.Getter;

import java.io.IOException;
import java.util.Objects;

@Getter
public class EdgeWrapper {

    private final Label id;
    private final Label startNode;
    private final Label endNode;
    private final JFXButton btnEdit;
    private final JFXButton btnDel;

    public EdgeWrapper(Edge e) throws IOException {
        this.id = new Label(e.getEdgeID());
        this.startNode = new Label(e.getStartNodeName());
        this.endNode = new Label(e.getEndNodeName());

        // Set up edit button
        JFXButton btnEdit = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/edu/wpi/teamB/views/misc/nodeEdgeEditBtn.fxml")));
        btnEdit.setId(e.getEdgeID() + "EditBtn");

        // TODO connect button to action
        btnEdit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = App.getPrimaryStage();
                stage.setUserData(e);
                try {
                    SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/mapeditor/edges/editEdgeMenu.fxml");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        this.btnEdit = btnEdit;

        // Set up delete button
        JFXButton btnDel = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/edu/wpi/teamB/views/misc/nodeEdgeDelBtn.fxml")));
        btnDel.setId(e.getEdgeID() + "DelBtn");

        // TODO connect button to action
        btnDel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DatabaseHandler.getDatabaseHandler("main.db").removeEdge(e.getEdgeID());
                try {
                    SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/mapeditor/edges/edgeEditorMenu.fxml");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        this.btnDel = btnDel;
    }
}
