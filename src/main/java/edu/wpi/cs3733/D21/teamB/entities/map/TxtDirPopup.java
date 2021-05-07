package edu.wpi.cs3733.D21.teamB.entities.map;

import edu.wpi.cs3733.D21.teamB.database.DatabaseHandler;
import edu.wpi.cs3733.D21.teamB.entities.User;
import edu.wpi.cs3733.D21.teamB.entities.map.data.Node;
import edu.wpi.cs3733.D21.teamB.entities.map.data.TxtDirPopupData;
import edu.wpi.cs3733.D21.teamB.pathfinding.Directions;
import edu.wpi.cs3733.D21.teamB.util.ExternalCommunication;
import edu.wpi.cs3733.D21.teamB.util.Popup.Poppable;
import edu.wpi.cs3733.D21.teamB.util.Popup.Popup;
import edu.wpi.cs3733.D21.teamB.util.tts.TextToSpeech;
import edu.wpi.cs3733.D21.teamB.views.map.PathfindingMenuController;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import lombok.Getter;
import lombok.Setter;
import net.kurobako.gesturefx.GesturePane;

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

    private final GesturePane gPane;

    private Label previousText;
    private List<Line> previousLines;

    private final double avgX, avgY, scaleAmount;

    private final StackPane stackPane;

    private final TextToSpeech tts = new TextToSpeech();

    public TxtDirPopup(Pane parent, TxtDirPopupData data) {
        super(parent, data);

        mapDrawer = data.getMapDrawer();
        mapCache = data.getMapCache();
        gPane = data.getGesturePane();
        floorSwitcher = data.getFloorSwitcher();
        stackPane = data.getStackPane();
        directions = new ArrayList<>();
        directions.addAll(data.getInstructions());
        maxIndex = directions.size() - 1;

        avgX = mapCache.getAvgX();
        avgY = mapCache.getAvgY();
        scaleAmount = mapCache.getScaleAmount();

        // You can use up/down arrows for directions, as well as tab/shift-tab
        stackPane.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.DOWN) {
                next();
                e.consume();
            } else if (e.getCode() == KeyCode.UP) {
                previous();
                e.consume();
            } else if (e.getCode() == KeyCode.TAB && e.isShiftDown()) {
                previous();
                e.consume();
            } else if (e.getCode() == KeyCode.TAB) {
                next();
                e.consume();
            }
        });

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
     * Email the text directions to the current user
     */
    public void email() {
        User currentUser = DatabaseHandler.getHandler().getAuthenticationUser();
        ExternalCommunication.sendTextDirections(currentUser.getEmail(), currentUser.getFirstName(), directions);
    }

    /**
     * Restart the text directions
     */
    public void restart() {
        index = 1;
        if (!mapCache.getCurrentFloor().equals(directions.get(index).getFloor()))
            updateFloor();
        highlight(true);
    }

    /**
     * Go to the previous text direction
     */
    public void previous() {
        if (index > 1) {
            index--;
            if (!directions.get(index + 1).getFloor().equals(directions.get(index).getFloor()) || !mapCache.getCurrentFloor().equals(directions.get(index).getFloor()))
                updateFloor();
            highlight(true);
        }
    }

    /**
     * Go to the next text direction
     */
    public void next() {
        if (index < maxIndex) {
            index++;
            if (!directions.get(index - 1).getFloor().equals(directions.get(index).getFloor()) || !mapCache.getCurrentFloor().equals(directions.get(index).getFloor()))
                updateFloor();
            highlight(true);
        }
    }

    /**
     * Closes the popup
     */
    public void close() {
        index = 0;
        highlight(true);
        gPane.zoomTo(scaleAmount, new Point2D(gPane.getWidth() / 2, gPane.getHeight() / 2));
        gPane.centreOn(new Point2D(avgX, avgY));
        stackPane.setOnKeyPressed(null);
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
    public void highlight(boolean updateScrollPane) {
        // Reset the previous colors to the normal color
        // Background transparentBG = new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY));
        if (previousText != null && previousLines != null) {
            previousText.getParent().setStyle("-fx-background-color: transparent; -fx-padding: 0;");
            for (Line l : previousLines) l.setStroke(Color.rgb(0, 103, 177));
        }

        // Change the color to red for the new lines and scroll the scrollpane down to the element
        if (index != 0) {
            HBox box = (HBox) instructionBox.getChildren().get(index);
            Label label = (Label) box.getChildren().get(1);
            box.setStyle("-fx-background-color: #0067B1; -fx-padding: 0;");
            List<Line> lines = mapCache.getInstructionsToEdges().get(label.getText());
            for (Line l : lines) {
                l.setStroke(Color.RED);
                l.setOpacity(1);
            }
            previousText = label;
            previousLines = lines;

            // If the user has TTS enabled, speak the direction
            if (DatabaseHandler.getHandler().getAuthenticationUser().getTtsEnabled().equals("T")) {
                String toSpeak = label.getText();
                tts.speak(toSpeak, 1.0f, false, false);
            }

            // Update the scrollpane
            if (updateScrollPane) {
                double height = scrollPane.getContent().getBoundsInLocal().getHeight();
                double y = box.getBoundsInParent().getMaxY();
                scrollPane.setVvalue(y / height);
            }

            // Adjust coordinate scales
            if (lines.isEmpty()) return;
            Line l = lines.get(0);
            double minX = Math.min(l.getStartX(), l.getEndX());
            double minY = Math.min(l.getStartY(), l.getEndY());
            double maxX = Math.max(l.getStartX(), l.getEndX());
            double maxY = Math.max(l.getStartY(), l.getEndY());

            for (Line current : lines) {
                if (current.getStartX() < minX) minX = current.getStartX();
                if (current.getEndX() < minX) minX = current.getEndX();
                if (current.getStartY() < minY) minY = current.getStartY();
                if (current.getEndY() < minY) minY = current.getEndY();
                if (current.getStartX() > maxX) maxX = current.getStartX();
                if (current.getEndX() > maxX) maxX = current.getEndX();
                if (current.getStartY() > maxY) maxY = current.getStartY();
                if (current.getEndY() > maxY) maxY = current.getEndY();
            }

            int padding = 75;
            minX -= padding;
            minY -= padding;
            maxX += padding;
            maxY += padding;

            double avgX = (minX + maxX) / 2;
            double avgY = (minY + maxY) / 2;

            // Figure out how much to zoom
            double scaleX = gPane.getWidth() / (maxX - minX);
            double scaleY = gPane.getHeight() / (maxY - minY);
            double scaleAmount = Math.min(scaleX, scaleY);

            gPane.zoomTo(scaleAmount, new Point2D(gPane.getWidth() / 2, gPane.getHeight() / 2));
            gPane.centreOn(new Point2D(avgX, avgY));
        }
    }
}
