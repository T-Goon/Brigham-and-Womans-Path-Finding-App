package edu.wpi.teamB.entities.map;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@EqualsAndHashCode
public class Node implements Comparable<Node> {
    private final String nodeID;
    private final int xCoord;
    private final int yCoord;
    private final String floor;
    private final String building;
    private final String nodeType;
    private final String longName;
    private final String shortName;

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
}
