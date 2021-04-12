package edu.wpi.teamB.pathfinding;

import lombok.Getter;

@Getter
public class Edge implements Comparable<Edge>{

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
  public int compareTo(Edge o) {
    return Integer.compare(weight, o.getWeight());
  }
}
