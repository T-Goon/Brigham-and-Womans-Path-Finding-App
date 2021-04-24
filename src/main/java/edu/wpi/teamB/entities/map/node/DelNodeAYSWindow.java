package edu.wpi.teamB.entities.map.node;

import edu.wpi.teamB.App;
import edu.wpi.teamB.database.DatabaseHandler;
import edu.wpi.teamB.entities.map.data.NodeMenuPopupData;
import edu.wpi.teamB.util.Popup.Popup;
import edu.wpi.teamB.util.Popup.Window;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import net.kurobako.gesturefx.GesturePane;

import java.io.IOException;
import java.util.Objects;

public class DelNodeAYSWindow extends Window<VBox, NodeMenuPopupData, VBox> {

    private final NodeMenuPopup container;

    public DelNodeAYSWindow(Pane parent, NodeMenuPopupData data, NodeMenuPopup nmPopup, VBox previous) {
        super(parent, data, previous);
        this.container = nmPopup;
    }

    /**
     * Delete a node
     */
    public void delNode(){
        DatabaseHandler.getDatabaseHandler("main.db").removeNode(data.getNodeID());
        data.getMd().refreshEditor();

        container.hide();
    }

    /**
     * Show popup
     */
    public void show() {

        VBox areYouSureMenu = null;

        // Pass data to window
        App.getPrimaryStage().setUserData(this);

        // Load new window
        try {
            areYouSureMenu = FXMLLoader.load(Objects.requireNonNull(
                    getClass().getClassLoader().getResource("edu/wpi/teamB/views/map/nodePopup/delNodeAreYouSure.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        super.show(areYouSureMenu);
    }
}
