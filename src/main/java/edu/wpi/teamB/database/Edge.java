package edu.wpi.teamB.database;

import java.util.Objects;

public class Edge {

    private final String edgeID;
    private final String startNodeName;
    private final String endNodeName;

    public Edge(String edgeID, String startNodeName, String endNodeName) {
        this.edgeID = edgeID;
        this.startNodeName = startNodeName;
        this.endNodeName = endNodeName;
    }

    public String getEdgeID() {
        return edgeID;
    }

    public String getStartNodeName() {
        return startNodeName;
    }

    public String getEndNodeName() {
        return endNodeName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return Objects.equals(edgeID, edge.edgeID) && Objects.equals(startNodeName, edge.startNodeName) && Objects.equals(endNodeName, edge.endNodeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(edgeID, startNodeName, endNodeName);
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
