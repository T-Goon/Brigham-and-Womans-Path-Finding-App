package edu.wpi.cs3733.D21.teamB.entities.map.node;

import edu.wpi.cs3733.D21.teamB.App;
import edu.wpi.cs3733.D21.teamB.entities.map.data.GraphicalInputData;
import edu.wpi.cs3733.D21.teamB.util.Popup.Poppable;
import edu.wpi.cs3733.D21.teamB.util.Popup.Popup;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import net.kurobako.gesturefx.GesturePane;

import javax.swing.text.html.ImageView;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class GraphicalInputPopup extends Popup<VBox, GraphicalInputData> implements Poppable {

    private final GesturePane gpane;

    public GraphicalInputPopup(Pane parent, GraphicalInputData data, GesturePane gpane) {
        super(parent, data);

        this.gpane = gpane;
    }

    /**
     * Set the start location text field
     */
    public void setStart(){
        data.getStartTxt().setText(
                data.getNodeName()
        );
        data.getMd().removeAllPopups();
        data.getPfmc().validateFindPathButton();
    }

    /**
     * Set the end location text field
     */
    public void setEnd(){
        data.getEndTxt().setText(
                data.getNodeName()
        );
        data.getMd().removeAllPopups();
        data.getPfmc().validateFindPathButton();
    }

    /**
     * Add a stop to the path finding
     */
    public void addStop(){
        List<String> stopsList = data.getMc().getStopsList();

        // Only add stop if it has not been added just recently
        if(stopsList.size() == 0 || !stopsList.get(stopsList.size()-1).equals(data.getNodeName())){
            stopsList.add(data.getNodeName());
            data.getPfmc().displayStops(stopsList);
        }

        // Validate button
        data.getBtnRmStop().setDisable(stopsList.isEmpty());

        data.getMd().removeAllPopups();
    }

    /**
     * Show this popup on the map
     */
    public void show() {

        VBox locInput = null;

        // Pass this object to the controller
        App.getPrimaryStage().setUserData(this);

        try{
            // Load fxml
            locInput = FXMLLoader.load(
                    Objects.requireNonNull(getClass().getResource("/edu/wpi/cs3733/D21/teamB/views/map/misc/graphicalInput.fxml")));
        } catch (IOException e) { e.printStackTrace(); }

        super.show(locInput);
        this.gpane.setGestureEnabled(false);
    }

    /**
     * Remove this popup from the map
     */
    @Override
    public void hide() {
        super.hide();
    }
}
