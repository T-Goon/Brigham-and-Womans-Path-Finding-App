package edu.wpi.teamB.util;

import com.jfoenix.controls.JFXButton;
import edu.wpi.teamB.database.DatabaseHandler;
import edu.wpi.teamB.entities.Edge;
import edu.wpi.teamB.entities.Node;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
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

    public NodeWrapper(Node n) throws IOException {
        this.id = new Label(n.getNodeID());
        this.name = new Label(n.getLongName());
        this.type = new Label(n.getNodeType());
        List<Edge> edgesList = DatabaseHandler.getDatabaseHandler("main.db").getAdjacentEdgesOfNode(n.getNodeID());

        // Construct string of all edge names
        StringBuilder edgeString = null;
        for (Edge e : edgesList) {
            if (edgeString == null) edgeString = new StringBuilder(e.getEdgeID());
            else edgeString.append(" ").append(e.getEdgeID());
        }

        edges = new Label(edgeString == null ? "None" : edgeString.toString());

        // Set up edit button
        JFXButton btnEdit = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/edu/wpi/teamB/views/misc/nodeEditBtn.fxml")));
        btnEdit.setId(n.getNodeID() + "EditBtn");

        // TODO connect button to action
        btnEdit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello world");
            }
        });

        this.btnEdit = btnEdit;

        // Set up delete button
        JFXButton btnDel = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/edu/wpi/teamB/views/misc/nodeDelBtn.fxml")));
        btnDel.setId(n.getNodeID() + "DelBtn");

        // TODO connect button to action
        btnDel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DatabaseHandler.getDatabaseHandler("main.db").removeNode(n.getNodeID());
                try {
                    SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/menus/nodesEditorMenu.fxml");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        this.btnDel = btnDel;
    }
}
