package edu.wpi.teamB.entities.map.node;

import edu.wpi.teamB.entities.map.data.AddEdgeData;
import edu.wpi.teamB.util.Popup.Popup;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Objects;

public class AddEdgeWindow extends Popup<VBox, AddEdgeData> {

    private final NodeMenuPopup container;

    public AddEdgeWindow(Pane parent, AddEdgeData data, NodeMenuPopup nmp) {
        super(parent, data);
        this.container = nmp;
    }

    public void show(){

        VBox addEdgeMenu = null;

        // Load window
        try {
            addEdgeMenu = FXMLLoader.load(Objects.requireNonNull(
                    getClass().getClassLoader().getResource("edu/wpi/teamB/views/map/nodePopup/addEdgePopup.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        super.show(addEdgeMenu);
    }

    @Override
    public void hide() {
        super.hide();
    }
}
