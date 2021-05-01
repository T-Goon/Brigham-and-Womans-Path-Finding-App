package edu.wpi.cs3733.D21.teamB.entities.map.node;

import edu.wpi.cs3733.D21.teamB.App;
import edu.wpi.cs3733.D21.teamB.database.DatabaseHandler;
import edu.wpi.cs3733.D21.teamB.entities.map.data.GraphicalInputData;
import edu.wpi.cs3733.D21.teamB.util.Popup.Poppable;
import edu.wpi.cs3733.D21.teamB.util.Popup.Popup;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import net.kurobako.gesturefx.GesturePane;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
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
    public void setStart() {
        data.getStartTxt().setText(
                data.getNodeName()
        );
        data.getMd().removeAllPopups();
        data.getPfmc().validateFindPathButton();
    }

    /**
     * Set the end location text field
     */
    public void setEnd() {
        data.getEndTxt().setText(
                data.getNodeName()
        );
        data.getMd().removeAllPopups();
        data.getPfmc().validateFindPathButton();
    }

    /**
     * Add a stop to the path finding
     */
    public void addStop() {
        List<String> stopsList = data.getMc().getStopsList();

        // Only add stop if it has not been added just recently
        if (stopsList.size() == 0 || !stopsList.get(stopsList.size() - 1).equals(data.getNodeName())) {
            stopsList.add(data.getNodeName());
            data.getPfmc().displayStops(stopsList);
        }

        // Validate button
        data.getBtnRmStop().setDisable(stopsList.isEmpty());

        data.getMd().removeAllPopups();
    }

    /**
     * Add a favorite location to the tree view
     */
    public boolean addFavorite() {
        // Get tree item and item to add
        TreeItem<String> favorites = data.getPfmc().getFavorites();
        TreeItem<String> itemToAdd = new TreeItem<>(data.getNodeName());

        // Check if the location is already in favorites
        boolean contains = false;
        for (TreeItem<String> item : favorites.getChildren()) {
            if (item.getValue().equals(data.getNodeName())) {
                contains = true;
                break;
            }
        }

        // Check if the location is a parking spot
        boolean hasParking = false;
        if (data.getNodeName().contains("Park")) {
            try {
                List<String> locations = DatabaseHandler.getHandler().getFavorites();
                for (String location : locations) {
                    if (location.contains("Park")) {
                        hasParking = true;
                        break;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // Add the location to favorites
        if (!contains && !hasParking) {
            favorites.getChildren().add(itemToAdd);
            try {
                DatabaseHandler.getHandler().addFavoriteLocation(itemToAdd.getValue());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return hasParking;
    }

    /**
     * Remove a favorite location from the tree view
     */
    public void removeFavorite() {
        // Get tree item
        TreeItem<String> favorites = data.getPfmc().getFavorites();
        List<TreeItem<String>> toRemove = new ArrayList<>();

        // Find the location to remove
        for (TreeItem<String> item : favorites.getChildren()) {
            if (item.getValue().equals(data.getNodeName())) {
                toRemove.add(item);
                try {
                    DatabaseHandler.getHandler().removeFavoriteLocation(item.getValue());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        favorites.getChildren().removeAll(toRemove);
    }

    /**
     * Check if the location is a favorite
     *
     * @param longName the location's long name
     * @return true if the location is a favorite
     */
    public boolean isFavorite(String longName) {
        return longName.equals(data.getNodeName());
    }

    /**
     * Check if the location is a parking spot
     *
     * @param longName the location's long name
     * @return true if the location is a parking spot
     */
    public boolean isParking(String longName) {
        return longName.contains("Park");
    }

    /**
     * Show this popup on the map
     */
    public void show() {

        VBox locInput = null;

        // Pass this object to the controller
        App.getPrimaryStage().setUserData(this);

        try {
            // Load fxml
            locInput = FXMLLoader.load(
                    Objects.requireNonNull(getClass().getResource("/edu/wpi/cs3733/D21/teamB/views/map/misc/graphicalInput.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }

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
