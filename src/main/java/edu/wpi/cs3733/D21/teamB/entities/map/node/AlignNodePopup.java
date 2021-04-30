package edu.wpi.cs3733.D21.teamB.entities.map.node;

import edu.wpi.cs3733.D21.teamB.database.DatabaseHandler;
import edu.wpi.cs3733.D21.teamB.entities.map.data.AlignNodePopupData;
import edu.wpi.cs3733.D21.teamB.entities.map.data.Node;
import edu.wpi.cs3733.D21.teamB.util.Popup.Poppable;
import edu.wpi.cs3733.D21.teamB.util.Popup.Popup;
import edu.wpi.cs3733.D21.teamB.views.map.PathfindingMenuController;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class AlignNodePopup extends Popup<VBox, AlignNodePopupData> implements Poppable {

    public AlignNodePopup(Pane parent, AlignNodePopupData data) {
        super(parent, data);
    }

    /**
     * Show this popup on the map
     */
    @Override
    public void show() {
        VBox popup = null;

        try {
            popup = FXMLLoader.load(Objects.requireNonNull(
                    getClass().getClassLoader().getResource("edu/wpi/cs3733/D21/teamB/views/map/nodePopup/alignNodePopup.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        data.getGesturePane().setGestureEnabled(false);
        super.show(popup);
    }

    /**
     * Aligns the given nodes
     */
    public void alignNodes() {

        // Calculate mean for x and y
        List<Circle> nodes = data.getNodes();
        double meanX = (nodes.stream().mapToDouble(Circle::getCenterX).sum() / nodes.size()) * PathfindingMenuController.COORDINATE_SCALE;
        double meanY = (nodes.stream().mapToDouble(Circle::getCenterY).sum() / nodes.size()) * PathfindingMenuController.COORDINATE_SCALE;

        // Calculate the equation
        // Calculate slope, accounting for if it's straight up
        // Then calculate the y intercept
        double top = 0;
        double bottom = 0;
        for (Circle c : nodes) {
            double xDiff = (c.getCenterX() * PathfindingMenuController.COORDINATE_SCALE) - meanX;
            double yDiff = (c.getCenterY() * PathfindingMenuController.COORDINATE_SCALE) - meanY;
            top += xDiff * yDiff;
            bottom += Math.pow(xDiff, 2);
        }
        double slope = top / bottom;
        if (Double.isNaN(slope)) slope = Double.MAX_VALUE;
        double yIntercept = meanY - (slope * meanX);

        // Calculate perpendicular slope, accounting for if the slope is 0
        double perpendicularSlope = 0;
        if (slope != 0) perpendicularSlope = -1 / slope;


        // Now, for each node, calculate the spot on the line the shortest distance from the point, then update the node to that spot
        for (Circle c : nodes) {
            double perpYIntercept = (c.getCenterY() * PathfindingMenuController.COORDINATE_SCALE) - ((c.getCenterX() * PathfindingMenuController.COORDINATE_SCALE) * perpendicularSlope);
            double endX = c.getCenterX() * PathfindingMenuController.COORDINATE_SCALE;
            if (slope != 0) endX = (perpYIntercept - yIntercept) / (slope - perpendicularSlope);
            double endY = slope * endX + yIntercept;

            Node node = DatabaseHandler.getHandler().getNodeById(c.getId().substring(0, 10));
            node.setXCoord((int) endX);
            node.setYCoord((int) endY);
            try {
                DatabaseHandler.getHandler().updateNode(node);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // Clean up
        hide();
        data.getMapDrawer().removeAllPopups();
        data.getMapDrawer().refreshEditor();
    }

    /**
     * Hide this popup on the map
     */
    @Override
    public void hide() {
        for (Circle c : data.getNodes()) {
            c.setStroke(Color.BLACK);
        }
        data.getMapDrawer().getAligned().clear();
        data.getGesturePane().setGestureEnabled(true);
        super.hide();
    }
}
