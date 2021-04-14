package edu.wpi.teamB.util;

import com.jfoenix.controls.JFXButton;
import edu.wpi.teamB.App;
import edu.wpi.teamB.database.DatabaseHandler;
import edu.wpi.teamB.entities.Edge;
import edu.wpi.teamB.entities.Node;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import lombok.Getter;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Getter
public class NodeWrapper {

    private final Label id;
    private final Label name;
    private final Label type;
    private final Label edges;
    private final JFXButton btnEdit;
    private final JFXButton btnDel;
    private final TableView parentTable;

    public NodeWrapper(Node n, TableView parentTable) throws IOException {
        this.id = new Label(n.getNodeID());
        this.name = new Label(n.getLongName());
        this.type = new Label(n.getNodeType());
        this.parentTable = parentTable;
        List<Edge> edgesList = DatabaseHandler.getDatabaseHandler("main.db").getAdjacentEdgesOfNode(n.getNodeID());

        // Construct string of all edge names
        StringBuilder edgeString = null;
        for (Edge e : edgesList) {
            if (edgeString == null) edgeString = new StringBuilder(e.getEdgeID());
            else edgeString.append(" ").append(e.getEdgeID());
        }

        edges = new Label(edgeString == null ? "None" : edgeString.toString());

        // Set up edit button
        JFXButton btnEdit = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/edu/wpi/teamB/views/misc/nodeEdgeEditBtn.fxml")));
        btnEdit.setId(n.getNodeID() + "EditBtn");


        // TODO connect button to action
        btnEdit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = App.getPrimaryStage();
                stage.setUserData(n);
                try {
                    SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/mapeditor/nodes/editNodeMenu.fxml");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        this.btnEdit = btnEdit;

        // Set up delete button
        JFXButton btnDel = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/edu/wpi/teamB/views/misc/nodeEdgeDelBtn.fxml")));
        btnDel.setId(n.getNodeID() + "DelBtn");

        // TODO connect button to action
        btnDel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DatabaseHandler.getDatabaseHandler("main.db").removeNode(n.getNodeID());
                parentTable.getItems().removeIf( (Object o) -> ((NodeWrapper) o).id == id);
//                    SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/mapeditor/nodes/nodeEditorMenu.fxml");
            }
        });

        this.btnDel = btnDel;
    }
}
