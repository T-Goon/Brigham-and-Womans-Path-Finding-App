package edu.wpi.cs3733.D21.teamB.entities.map.data;

import edu.wpi.cs3733.D21.teamB.entities.IStoredEntity;
import javafx.scene.paint.Color;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@EqualsAndHashCode
public class Node implements Comparable<Node>, IStoredEntity {
    private final String nodeID;
    @Setter
    private int xCoord;
    @Setter
    private int yCoord;
    private final String floor;
    private final String building;
    private final String nodeType;
    private final String longName;
    private final String shortName;

    @Setter
    @Getter
    private Color color;

    @EqualsAndHashCode.Exclude
    @Setter
    private double fVal;

    public Node(String nodeID, int xCoord, int yCoord, String floor, String building, String nodeType, String longName, String shortName) {
        this.nodeID = nodeID;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.floor = floor;
        this.building = building;
        this.nodeType = nodeType;
        this.longName = longName;
        this.shortName = shortName;
        this.color = Color.web("012D5A");
    }

    public Node(String nodeID, int xCoord, int yCoord, String floor, String building, String nodeType, String longName, String shortName, Color color) {
        this.nodeID = nodeID;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.floor = floor;
        this.building = building;
        this.nodeType = nodeType;
        this.longName = longName;
        this.shortName = shortName;
        this.color = color;
    }

    public int getFloorAsInt(){
        switch(floor){
            case "L2":
                return 0;
            case "L1":
                return 1;
            case "1":
                return 2;
            case "2":
                return 3;
            case "3":
                return 4;
            default:
                return -1;
        }
    }

    @Override
    public String toString() {
        return "Node{" +
                "nodeID='" + nodeID + '\'' +
                ", xcoord=" + xCoord +
                ", ycoord=" + yCoord +
                ", floor=" + floor +
                ", building='" + building + '\'' +
                ", nodeType='" + nodeType + '\'' +
                ", longName='" + longName + '\'' +
                ", shortName='" + shortName + '\'' +
                '}';
    }

    @Override
    public int compareTo(Node o) {
        return Double.compare(this.fVal, o.getFVal());
    }

    public static int floorAsInt(String floorString){
        switch(floorString){
            case "L2":
                return 0;
            case "L1":
                return 1;
            case "1":
                return 2;
            case "2":
                return 3;
            case "3":
                return 4;
            case "01":
                return 2;
            case "02":
                return 3;
            case "03":
                return 4;
            default:
                return -1;
        }
    }

}
