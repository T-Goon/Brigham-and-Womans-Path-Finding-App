package edu.wpi.teamB.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Edge {

    private final String edgeID;
    private final String startNodeName;
    private final String endNodeName;
    private int weight;

    public Edge(String edgeID, String startNodeName, String endNodeName) {
        this.edgeID = edgeID;
        this.startNodeName = startNodeName;
        this.endNodeName = endNodeName;
    }

    @Override
    public String toString() {
        return "Edge{" +
                "edgeID='" + edgeID + '\'' +
                ", startNodeName='" + startNodeName + '\'' +
                ", endNodeName='" + endNodeName + '\'' +
                '}';
    }
}
