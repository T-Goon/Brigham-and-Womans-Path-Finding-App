package edu.wpi.teamB.pathfinding;

import java.util.Objects;

public class Node {
  private final String nodeID;
  private final int xcoord;
  private final int ycoord;
  private final int floor;
  private final String building;
  private final String nodeType;
  private final String longName;
  private final String shortName;

  private double accumWeight;
  private double fVal;
  private String prevNode;
  private boolean isClosed;

  public Node(
      String nodeID,
      int xcoord,
      int ycoord,
      int floor,
      String building,
      String nodeType,
      String longName,
      String shortName) {
    this.nodeID = nodeID;
    this.xcoord = xcoord;
    this.ycoord = ycoord;
    this.floor = floor;
    this.building = building;
    this.nodeType = nodeType;
    this.longName = longName;
    this.shortName = shortName;
    this.isClosed = false;
  }

  public String getNodeID() {
    return nodeID;
  }

  public int getXCoord() {
    return xcoord;
  }

  public int getYCoord() {
    return ycoord;
  }

  public int getFloor() {
    return floor;
  }

  public String getBuilding() {
    return building;
  }

  public String getNodeType() {
    return nodeType;
  }

  public String getLongName() {
    return longName;
  }

  public String getShortName() {
    return shortName;
  }

  public double getAccumWeight() {
    return accumWeight;
  }

  public double getfVal() {
    return fVal;
  }

  public String getPrevNode() {
    return prevNode;
  }

  public boolean isClosed() {
    return isClosed;
  }

  public void setAccumWeight(double accumWeight) {
    this.accumWeight = accumWeight;
  }

  public void setfVal(double fVal) {
    this.fVal = fVal;
  }

  public void setPrevNode(String prevNode) {
    this.prevNode = prevNode;
  }

  public void setClosed() {
    isClosed = true;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Node node = (Node) o;
    return xcoord == node.xcoord
        && ycoord == node.ycoord
        && floor == node.floor
        && Objects.equals(nodeID, node.nodeID)
        && Objects.equals(building, node.building)
        && Objects.equals(nodeType, node.nodeType)
        && Objects.equals(longName, node.longName)
        && Objects.equals(shortName, node.shortName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nodeID, xcoord, ycoord, floor, building, nodeType, longName, shortName);
  }
}
