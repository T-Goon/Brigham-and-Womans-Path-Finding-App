package edu.wpi.teamB.entities.map.node;

import edu.wpi.teamB.database.DatabaseHandler;
import edu.wpi.teamB.entities.map.data.Node;
import edu.wpi.teamB.entities.map.data.NodeMenuPopupData;
import edu.wpi.teamB.util.Popup.Popup;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import lombok.Getter;

import java.io.IOException;
import java.util.Objects;

public class EditNodeWindow extends Popup<VBox, NodeMenuPopupData> {

    @Getter
    private final NodeMenuPopup container;

    public EditNodeWindow(Pane parent, NodeMenuPopupData data, NodeMenuPopup nmp) {
        super(parent, data);
        this.container = nmp;
    }

    public void updateNode(int x, int y, String floor, String building, String type, String longName, String shortName){

        Node node = new Node(
                data.getNodeID(),
                x,
                y,
                floor,
                building,
                type,
                longName,
                shortName);

        // Update database and graph
        DatabaseHandler.getDatabaseHandler("main.db").updateNode(node);

        // Remove popup from map and refresh map nodes
        data.getMd().refreshEditor();

        container.hide();
    }

    public void show(){

        VBox nodeEditMenu = null;

        // Load window
        try {
            nodeEditMenu = FXMLLoader.load(Objects.requireNonNull(
                    getClass().getClassLoader().getResource("edu/wpi/teamB/views/map/nodePopup/editNodePopup.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        super.show(nodeEditMenu);
    }

    @Override
    public void hide() {
        super.hide();
    }
}
