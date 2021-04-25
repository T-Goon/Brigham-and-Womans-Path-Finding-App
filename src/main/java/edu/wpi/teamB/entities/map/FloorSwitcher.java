package edu.wpi.teamB.entities.map;

import com.jfoenix.controls.JFXButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class FloorSwitcher {
    private final MapDrawer md;
    private final MapCache mc;
    private final ImageView map;

    private final JFXButton btnF3;
    private final JFXButton btnF2;
    private final JFXButton btnF1;
    private final JFXButton btnFL1;
    private final JFXButton btnFL2;

    private JFXButton currentlySelected;

    private final static String floor3Path = "/edu/wpi/teamB/images/maps/03_thethirdfloor.png";
    private final static String floor2Path = "/edu/wpi/teamB/images/maps/02_thesecondfloor.png";
    private final static String floor1Path = "/edu/wpi/teamB/images/maps/01_thefirstfloor-Edited.png";
    private final static String floorL1Path = "/edu/wpi/teamB/images/maps/00_thelowerlevel1.png";
    private final static String floorL2Path = "/edu/wpi/teamB/images/maps/00_thelowerlevel2.png";

    public final static String floor3ID = "3";
    public final static String floor2ID = "2";
    public final static String floor1ID = "1";
    public final static String floorL1ID = "L1";
    public final static String floorL2ID = "L2";

    public FloorSwitcher(MapDrawer md, MapCache mc, ImageView map, JFXButton btnF3, JFXButton btnF2, JFXButton btnF1, JFXButton btnFL1, JFXButton btnFL2) {
        this.md = md;
        this.mc = mc;
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
     * @param floor String of the floor to switch to
     */
    public void switchFloor(String floor) {
        switch (floor) {
            case floor3ID:
                map.setImage(new Image(floor3Path));
                mc.setCurrentFloor(floor3ID);
                md.drawAllElements();
                highlightFloorButton(floor3ID);
                break;
            case floor2ID:
                map.setImage(new Image(floor2Path));
                mc.setCurrentFloor(floor2ID);
                md.drawAllElements();
                highlightFloorButton(floor2ID);
                break;
            case floor1ID:
                map.setImage(new Image(floor1Path));
                mc.setCurrentFloor(floor1ID);
                md.drawAllElements();
                highlightFloorButton(floor1ID);
                break;
            case floorL1ID:
                map.setImage(new Image(floorL1Path));
                mc.setCurrentFloor(floorL1ID);
                md.drawAllElements();
                highlightFloorButton(floorL1ID);
                break;
            case floorL2ID:
                map.setImage(new Image(floorL2Path));
                mc.setCurrentFloor(floorL2ID);
                md.drawAllElements();
                highlightFloorButton(floorL2ID);
                break;
            default:
                System.err.println("NO FLOOR! AAAAAAAAAAAAHHHHHHHHHHH!!!!!!!!!!!!!");
                break;
        }
    }

    /**
     * Changes the background color of the currently selected floor button.
     * @param floor the floor id of the floor
     */
    private void highlightFloorButton(String floor){
        currentlySelected.setBorder(new Border(
                new BorderStroke(
                        Color.BLACK,
                        BorderStrokeStyle.SOLID,
                        new CornerRadii(0),
                        new BorderWidths(1.0))));

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
                System.err.println("NO FLOOR! AAAAAAAAAAAAHHHHHHHHHHH!!!!!!!!!!!!!");
                break;
        }

        currentlySelected.setBorder(new Border(
                new BorderStroke(
                        Color.RED,
                        BorderStrokeStyle.SOLID,
                        new CornerRadii(0),
                        new BorderWidths(3.0))));
    }
}
