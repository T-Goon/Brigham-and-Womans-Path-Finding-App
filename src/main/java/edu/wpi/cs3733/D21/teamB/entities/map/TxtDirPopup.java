package edu.wpi.cs3733.D21.teamB.entities.map;

import edu.wpi.cs3733.D21.teamB.entities.map.data.Node;
import edu.wpi.cs3733.D21.teamB.entities.map.data.TxtDirPopupData;
import edu.wpi.cs3733.D21.teamB.pathfinding.Directions;
import edu.wpi.cs3733.D21.teamB.util.Popup.Poppable;
import edu.wpi.cs3733.D21.teamB.util.Popup.Popup;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TxtDirPopup extends Popup<VBox, TxtDirPopupData> implements Poppable {

    @Getter
    private final List<Directions.Direction> directions;

    @Setter
    @Getter
    private int index = 1;
    private final int maxIndex;

    @Setter
    private VBox instructionBox;

    @Setter
    private ScrollPane scrollPane;

    @Getter
    private final MapDrawer mapDrawer;

    @Getter
    private final MapCache mapCache;
    private final FloorSwitcher floorSwitcher;

    private Label previousText;
    private List<Line> previousLines;

    public TxtDirPopup(Pane parent, TxtDirPopupData data) {
        super(parent, data);

        mapDrawer = data.getMapDrawer();
        mapCache = data.getMapCache();
        floorSwitcher = data.getFloorSwitcher();
        directions = new ArrayList<>();
        directions.addAll(data.getInstructions());
        maxIndex = directions.size() - 1;

    }

    public void show() {
        VBox txtDirBox;
        try {
            txtDirBox = FXMLLoader.load(
                    Objects.requireNonNull(getClass().getResource("/edu/wpi/cs3733/D21/teamB/views/map/misc/showTextDir.fxml")));

        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        restart();
        scrollPane.setVvalue(0.0);
        super.show(txtDirBox);
    }

    /**
     * Restart the text directions
     */
    public void restart() {
        index = 1;
        if (!mapCache.getCurrentFloor().equals(directions.get(index).getFloor()))
            updateFloor();
        highlight();
    }

    /**
     * Go to the previous text direction
     */
    public void previous() {
        if (index > 1) {
            index--;
            if (!directions.get(index + 1).getFloor().equals(directions.get(index).getFloor()))
                updateFloor();
            highlight();
        }
    }

    /**
     * Go to the next text direction
     */
    public void next() {
        if (index < maxIndex) {
            index++;
            if (!directions.get(index - 1).getFloor().equals(directions.get(index).getFloor()))
                updateFloor();
            highlight();
        }
    }

    /**
     * Closes the popup
     */
    public void close() {
        index = 0;
        highlight();
        hide();
    }

    /**
     * Updates the floor when clicked on as well as the list of instructions to edges
     */
    public void updateFloor() {
        if (!mapCache.getCurrentFloor().equals(directions.get(index).getFloor()))
            floorSwitcher.switchFloor(directions.get(index).getFloor());
        updateEdges();
    }

    /**
     * Updates the list of edges for the floors
     */
    public void updateEdges() {
        mapCache.getInstructionsToEdges().clear();
        for (int i = 1; i < directions.size(); i++) {
            List<Line> edges = new ArrayList<>();
            for (int j = 0; j < directions.get(i).getNodes().size() - 1; j++) {
                Node start = directions.get(i).getNodes().get(j);
                Node end = directions.get(i).getNodes().get(j + 1);
                for (Line l : mapCache.getEdgesPlaced()) {
                    if (!edges.contains(l) && l.getId().contains(start.getNodeID()) && l.getId().contains(end.getNodeID())) {
                        edges.add(l);
                    }
                }
            }
            mapCache.getInstructionsToEdges().put(directions.get(i).getInstruction(), edges);
        }
    }


    /**
     * Highlights the selected box in red, and the path in red as well
     */
    public void highlight() {
        // Reset the previous colors to the normal color
        if (previousText != null && previousLines != null) {
            previousText.setStyle("-fx-background-color: transparent;");
            for (Line l : previousLines) l.setStroke(Color.rgb(0, 103, 177));
        }

        // Change the color to red for the new lines and scroll the scrollpane down to the element
        if (index != 0) {
            HBox box = (HBox) instructionBox.getChildren().get(index);
            Label label = (Label) box.getChildren().get(1);
            label.setStyle("-fx-background-color: darkblue; -fx-padding: 10;");
            List<Line> lines = mapCache.getInstructionsToEdges().get(label.getText());
            for (Line l : lines) l.setStroke(Color.RED);
            previousText = label;
            previousLines = lines;

            // Update the scrollpane
            double height = scrollPane.getContent().getBoundsInLocal().getHeight();
            double y = box.getBoundsInParent().getMaxY();
            scrollPane.setVvalue(y / height);
        }
    }
}
