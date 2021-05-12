package edu.wpi.cs3733.D21.teamB.entities.map;

import com.jfoenix.controls.JFXButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class FloorSwitcher {
    private final MapDrawer mapDrawer;
    private final MapCache mapCache;
    private final MapPathPopupManager mapPathPopupManager;
    private final ImageView map;

    private final JFXButton btnF3;
    private final JFXButton btnF2;
    private final JFXButton btnF1;
    private final JFXButton btnFL1;
    private final JFXButton btnFL2;

    private JFXButton currentlySelected;

    private final static String floor3Path = "/edu/wpi/cs3733/D21/teamB/images/maps/floors/03_thethirdfloor.png";
    private final static String floor2Path = "/edu/wpi/cs3733/D21/teamB/images/maps/floors/02_thesecondfloor.png";
    private final static String floor1Path = "/edu/wpi/cs3733/D21/teamB/images/maps/floors/01_thefirstfloor-Edited.png";
    private final static String floorL1Path = "/edu/wpi/cs3733/D21/teamB/images/maps/floors/00_thelowerlevel1.png";
    private final static String floorL2Path = "/edu/wpi/cs3733/D21/teamB/images/maps/floors/00_thelowerlevel2.png";

    private final static Image floor3Image = new Image(floor3Path);
    private final static Image floor2Image = new Image(floor2Path);
    private final static Image floor1Image = new Image(floor1Path);
    private final static Image floorL1Image = new Image(floorL1Path);
    private final static Image floorL2Image = new Image(floorL2Path);

    public final static String floor3ID = "3";
    public final static String floor2ID = "2";
    public final static String floor1ID = "1";
    public final static String floorL1ID = "L1";
    public final static String floorL2ID = "L2";

    public FloorSwitcher(MapDrawer mapDrawer, MapCache mapCache, MapPathPopupManager mapPathPopupManager, ImageView map, JFXButton btnF3, JFXButton btnF2, JFXButton btnF1, JFXButton btnFL1, JFXButton btnFL2) {
        this.mapDrawer = mapDrawer;
        this.mapCache = mapCache;
        this.mapPathPopupManager = mapPathPopupManager;
        this.map = map;
        this.btnF3 = btnF3;
        this.btnF2 = btnF2;
        this.btnF1 = btnF1;
        this.btnFL1 = btnFL1;
        this.btnFL2 = btnFL2;
        this.currentlySelected = btnF1;
    }

    /**
     * Switches the map image and has the map drawer place new nodes for that floor
     *
     * @param floor String of the floor to switch to
     */
    public void switchFloor(String floor) {
        switch (floor) {
            case floor3ID:
                mapCache.setCurrentFloor(floor3ID);
                map.setImage(floor3Image);
                mapDrawer.drawAllElements();
                highlightFloorButton(floor3ID);
                if (mapCache.getFinalPath() != null) {
                    mapDrawer.drawPath();
                }
                break;
            case floor2ID:

                mapCache.setCurrentFloor(floor2ID);
                map.setImage(floor2Image);
                mapDrawer.drawAllElements();
                highlightFloorButton(floor2ID);
                if (mapCache.getFinalPath() != null) {
                    mapDrawer.drawPath();
                }
                break;
            case floor1ID:

                mapCache.setCurrentFloor(floor1ID);
                map.setImage(floor1Image);
                mapDrawer.drawAllElements();
                highlightFloorButton(floor1ID);
                if (mapCache.getFinalPath() != null) {
                    mapDrawer.drawPath();
                }
                break;
            case floorL1ID:


                mapCache.setCurrentFloor(floorL1ID);
                map.setImage(floorL1Image);
                mapDrawer.drawAllElements();
                highlightFloorButton(floorL1ID);
                if (mapCache.getFinalPath() != null) {
                    mapDrawer.drawPath();
                }
                break;
            case floorL2ID:

                mapCache.setCurrentFloor(floorL2ID);
                map.setImage(floorL2Image);
                mapDrawer.drawAllElements();
                highlightFloorButton(floorL2ID);
                if (mapCache.getFinalPath() != null) {
                    mapDrawer.drawPath();
                }
                break;
            default:
                throw new IllegalStateException("NO FLOOR! AAAAAAAAAAAAHHHHHHHHHHH!!!!!!!!!!!!!");
        }

        // Update the edges for the text direction popup
        if (mapPathPopupManager.hasTxtDirPopup()) {
            TxtDirPopup popup = mapPathPopupManager.getTxtDirPopup();
            popup.updateEdges();
        }
    }

    /**
     * Changes the border color of the currently selected floor button.
     *
     * @param floor the floor id of the floor
     */
    private void highlightFloorButton(String floor) {
        currentlySelected.setStyle("-fx-border-color: BLACK;-fx-border-width: 1;-fx-background-color:  F6BD39;");

        switch (floor) {
            case floor3ID:
                currentlySelected = btnF3;
                break;
            case floor2ID:
                currentlySelected = btnF2;
                break;
            case floor1ID:
                currentlySelected = btnF1;
                break;
            case floorL1ID:
                currentlySelected = btnFL1;
                break;
            case floorL2ID:
                currentlySelected = btnFL2;
                break;
            default:
                throw new IllegalStateException("NO FLOOR! AAAAAAAAAAAAHHHHHHHHHHH!!!!!!!!!!!!!");
        }

        currentlySelected.setStyle("-fx-border-color: RED; -fx-border-width: 3; -fx-background-color:  F6BD39;");
    }

    /**
     * Converts a floor id to a int.
     *
     * @param floorID A floor id.
     * @return An int that represents the placement of the floor
     */
    public static int floorIDtoInt(String floorID) {
        switch (floorID) {
            case floor3ID:
                return 3;
            case floor2ID:
                return 2;
            case floor1ID:
                return 1;
            case floorL1ID:
                return -1;
            case floorL2ID:
                return -2;
            default:
                throw new IllegalArgumentException("NO FLOOR! AAAAAAAAAAAAHHHHHHHHHHH!!!!!!!!!!!!!" + " (the attempted floor was '" + floorID + "')");
        }
    }
}
