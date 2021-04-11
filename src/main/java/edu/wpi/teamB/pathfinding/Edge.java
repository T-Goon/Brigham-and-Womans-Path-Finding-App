package edu.wpi.teamB.pathfinding;

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

  public String getEdgeID() {
    return edgeID;
  }

  public String getStartNodeName() {
    return startNodeName;
  }

  public String getEndNodeName() {
    return endNodeName;
  }
}
