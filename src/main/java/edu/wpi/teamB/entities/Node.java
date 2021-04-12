package edu.wpi.teamB.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@EqualsAndHashCode
public class Node {
    private final String nodeID;
    private final int xCoord;
    private final int yCoord;
    private final int floor;
    private final String building;
    private final String nodeType;
    private final String longName;
    private final String shortName;

    @Setter
    private double accumWeight;
    @Setter
    private double fVal;
    @Setter
    private Node prevNode;
    @Setter
    private boolean closed;

    public Node(
            String nodeID,
            int xCoord,
            int yCoord,
            int floor,
            String building,
            String nodeType,
            String longName,
            String shortName) {
        this.nodeID = nodeID;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.floor = floor;
        this.building = building;
        this.nodeType = nodeType;
        this.longName = longName;
        this.shortName = shortName;
        this.closed = false;
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
}
