package edu.wpi.cs3733.D21.teamB.entities.map;

import edu.wpi.cs3733.D21.teamB.entities.map.data.TxtDirPopupData;
import edu.wpi.cs3733.D21.teamB.pathfinding.Directions;
import edu.wpi.cs3733.D21.teamB.util.Popup.Poppable;
import edu.wpi.cs3733.D21.teamB.util.Popup.Popup;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
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

    @Getter
    private final MapDrawer mapDrawer;

    @Getter
    private final MapCache mapCache;

    private Label previousText;
    private List<Line> previousLines;

    public TxtDirPopup(Pane parent, TxtDirPopupData data) {
        super(parent, data);

        mapDrawer = data.getMapDrawer();
        mapCache = data.getMapCache();
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
        super.show(txtDirBox);
    }

    /**
     * Restart the text directions
     */
    public void restart() {
        index = 1;
        highlight();
    }

    /**
     * Go to the previous text direction
     */
    public void previous() {
        if (index > 1) {
            index--;
            highlight();
        }
    }

    /**
     * Go to the next text direction
     */
    public void next() {
        if (index < maxIndex) {
            index++;
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
     * Highlights the selected box in red, and the path in red as well
     */
    public void highlight() {
        // Reset the previous colors to the normal color
        if (previousText != null && previousLines != null) {
            previousText.setStyle("-fx-background-color: transparent;");
            for (Line l : previousLines) l.setStroke(Color.rgb(0, 103, 177));
        }

        // Change the color to red for the new lines
        if (index != 0) {
            HBox box = (HBox) instructionBox.getChildren().get(index);
            Label label = (Label) box.getChildren().get(1);
            label.setStyle("-fx-background-color: darkblue;");
            List<Line> lines = mapCache.getInstructionsToEdges().get(label.getText());
            for (Line l : lines) l.setStroke(Color.RED);
            previousText = label;
            previousLines = lines;
        }
    }
}
