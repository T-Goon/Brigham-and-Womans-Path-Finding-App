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

    public TxtDirPopup(Pane parent, TxtDirPopupData data) {
        super(parent, data);

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

    public void highlight() {
        for (int i = 1; i < instructionBox.getChildren().size(); i++)
            ((Label) ((HBox) (instructionBox.getChildren().get(i))).getChildren().get(1)).setTextFill(index == i ? Color.RED : Color.WHITE);
    }
}
