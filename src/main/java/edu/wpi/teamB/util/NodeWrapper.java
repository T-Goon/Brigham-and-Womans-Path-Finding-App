package edu.wpi.teamB.util;

import com.jfoenix.controls.JFXButton;
import com.sun.org.apache.xpath.internal.operations.String;
import edu.wpi.teamB.App;
import edu.wpi.teamB.database.DatabaseHandler;
import edu.wpi.teamB.entities.Edge;
import edu.wpi.teamB.entities.Node;
import edu.wpi.teamB.views.menus.NodesEditorMenuController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.derby.iapi.db.Database;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Getter
public class NodeWrapper {

    private final Label id;
    private final Label name;
    private final Label type;
    private final Label edges;
    private final JFXButton editBtn;
    private final JFXButton delBtn;

    public NodeWrapper(Node n) throws IOException {
        this.id = new Label(n.getNodeID());
        this.name = new Label(n.getLongName());
        this.type = new Label(n.getNodeType());
        List<Edge> edgesList = DatabaseHandler.getDatabaseHandler("main.db").getAdjacentEdgesOfNode(n.getNodeID());

        // Construct string of all edge names
        StringBuilder edgeString = new StringBuilder();
        Boolean firstEdge = true;
        for(Edge e : edgesList){
            if(firstEdge == true){
                edgeString.append(e.getEdgeID());
            } else{
                edgeString.append(" ").append(e.getEdgeID());
            }
        }

        edges = new Label(edgeString.toString());

        // Set up edit button
        JFXButton btnEdit = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/edu/wpi/teamB/views/misc/nodeEditBtn.fxml")));
        btnEdit.setId(n.getNodeID()+"EditBtn");

        // TODO connect button to action
        btnEdit.setOnAction(new EventHandler<ActionEvent>() {
            @SneakyThrows
            @Override
            public void handle(ActionEvent event) {
                Stage stage = App.getPrimaryStage();
                stage.setUserData(n);
                SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/editNodeMenu.fxml");
            }
        });

        this.editBtn = btnEdit;

        // Set up delete button
        JFXButton btnDel = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/edu/wpi/teamB/views/misc/nodeDelBtn.fxml")));
        btnDel.setId(n.getNodeID()+"DelBtn");

        // TODO connect button to action
        btnDel.setOnAction(new EventHandler<ActionEvent>() {
            @SneakyThrows
            @Override
            public void handle(ActionEvent event) {
                DatabaseHandler.getDatabaseHandler("main.db").removeNode(n.getNodeID());
                SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/menus/nodesEditorMenu.fxml");
            }
        });

        this.delBtn = btnDel;
    }
}
