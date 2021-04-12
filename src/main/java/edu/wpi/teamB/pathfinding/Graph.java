package edu.wpi.teamB.pathfinding;
import java.util.*;
public class Graph {

  private static HashMap<String, List<Node>> initHashMap() {
    HashMap<String, List<Node>> adjNodes = new HashMap<String, List<Node>>();
    HashMap<String, Node> nodeInfo = Read.parseCSVNodes("src/main/resources/edu/wpi/teamB/csvfiles/MapBedges.csv");

    for (Node node : nodeInfo.values()) {
      List<Node> adjNodesList = findAdjNodes(node.getNodeID());
      adjNodes.put(node.getNodeID(), adjNodesList);
    }
    return adjNodes;
  }

  //testing
  public static List<Node> findAdjNodes(String ID) {
    List<Node> adjNodeList = new ArrayList<>();
    List<Edge> edgeInfo = Read.parseCSVEdges();
    HashMap<String, Node> nodeInfo = Read.parseCSVNodes("src/main/resources/edu/wpi/teamB/csvfiles/MapBedges.csv");

    for (Edge edges : edgeInfo) {
      if (edges.getStartNodeName().equals(ID)) {
        for (Node node : nodeInfo.values()) {
          if (edges.getEndNodeName().equals(node.getNodeID())) {
            adjNodeList.add(node);
            break;
          }
        }
      } else if (edges.getEndNodeName().equals(ID)) {
        for (Node node : nodeInfo.values()) {
          if (edges.getStartNodeName().equals(node.getNodeID())) {
            adjNodeList.add(node);
            break;
          }
        }
      }
    }
    return adjNodeList;
  }

  /**
   * Implementation of depth-first search given the starting nodeID.
   *
   * @param start the starting NodeID
   * @param end   the ending NodeID
   * @return a stack of the path of nodeIDs from start to end
   */

  public static Stack<String> dfs(String start, String end) {
    List<String> visited = new ArrayList<>();
    return dfsHelper(start, end, visited);
  }

  /**
   * DFS helper function.
   *
   * @param start   the current starting nodeID
   * @param end     the final nodeID
   * @param visited list of nodeIDs already visited
   * @return a stack of the path of nodeIDs from start to end
   */
  public static Stack<String> dfsHelper(String start, String end, List<String> visited) {
    Stack<String> stack = new Stack<>();
    visited.add(start);

    // If the start equals the end, this is the recursive base case
    if (start.equals(end)) {
      stack.push(start);
      return stack;
    }

    // Otherwise, run dfs on all the neighbors
    List<String> result;
    for (Node neighbor : initHashMap().get(start)) {
      result = new ArrayList<>();

      // If the neighbor is not already visited
      if (!visited.contains(neighbor.getNodeID())) {
        // Recursively see if it is part of path
        result.addAll(dfsHelper(neighbor.getNodeID(), end, visited));
        if (!result.isEmpty()) {
          stack.push(start);
          for (String s : result) {
            stack.push(s);
          }
          break;
        }
      }
    }

    return stack;
  }

  //test dist
  public static double dist(Node start, Node end) {
    double dist = Math.pow((start.getXCoord() - end.getXCoord()), 2) + Math.pow((start.getYCoord() + end.getYCoord()), 2);
    return Math.sqrt(dist);
  }


  //putting values in hashmap
  public static HashMap<String, Node> populateHashmap(Collection<Node> nodes) {
    HashMap<String, Node> spInfo = new HashMap<>();

    for (Node node : nodes) {
      node.setAccumWeight(Double.MAX_VALUE);
      node.setfVal(Double.MAX_VALUE);
      node.setPrevNode(null);
      spInfo.put(node.getNodeID(), node);
    }
    return spInfo;
  }

  private static String getMin(HashMap<String, Node> hashTbl){
    //priority queue
    double min = Double.MAX_VALUE+1;
    String result = null;

    for(Node data: hashTbl.values()){
      boolean isClosed = data.isClosed();
      if((data.getfVal() <= min) && (!data.isClosed())){
        min = data.getfVal();
        result = data.getNodeID();
      }
    }
    return result;
  }

  public static HashMap<String, Node> AStrHashTable(String startStr, String endStr) {
    HashMap<String, List<Node>> adjMat = initHashMap();
    HashMap<String, Node> nodes = Read.parseCSVNodes("src/main/resources/edu/wpi/teamB/csvfiles/MapBedges.csv");
    HashMap<String, Node> shortPathInfo = populateHashmap(nodes.values());


    Node start = nodes.get(startStr);
    Node end = nodes.get(endStr);

    List<Node> adjList;

    List<Node> close = new ArrayList<>();

    double heur = dist(start, end);
    double weight = 0; //"gVal"
    double fVal = heur + weight;

    Node curr = start;

    curr.setAccumWeight(weight);
    curr.setPrevNode(curr);
    curr.setfVal(fVal);

    shortPathInfo.replace(curr.getNodeID(), curr);

    while ((!curr.equals(end))) {

      //getting adj nodes of current node
      adjList = adjMat.get(curr.getNodeID());
      List<Node> adjListRead = new ArrayList();

      if(adjList.contains(end)){
        end.setPrevNode(curr);
        shortPathInfo.replace(end.getNodeID(), end);
        return shortPathInfo;
      }
      //for each adj node
      for (Node node : adjList) {
        if(!adjListRead.contains(node)){
          adjListRead.add(node);

        //check if curr node is close list
        if (!(close.contains(node))) {

          //weight = prev node weight + prev node to node weight
          //w of current node plus edge weight
          weight = shortPathInfo.get(curr.getNodeID()).getAccumWeight() + dist(curr, node);
          heur = dist(node, end);
          fVal = weight + heur;

          Node nodeMetrics = shortPathInfo.get(node.getNodeID());

          //updating fVal if a shorter path has been found
          //set the prev value of the node to the parent node (curr)
          if (fVal < nodeMetrics.getfVal()) {
            nodeMetrics.setfVal(fVal);
            nodeMetrics.setAccumWeight(weight);
            nodeMetrics.setPrevNode(curr);
            curr.setClosed();
            //shortPathInfo.replace(node.getNodeID(), nodeMetrics);
            shortPathInfo.replace(curr.getNodeID(), curr);
          }

        }
        }
      }
      close.add(curr);
      curr = nodes.get(getMin(shortPathInfo));
    }
    return shortPathInfo;
  }

  //backtracking to find A* path
  public static List<String> AStr(String start, String end) {
    // go to the end node check its previous node, keep doing this until you reach the start node
    HashMap<String, Node> AstrMap =  AStrHashTable(start, end);
    Stack<String> pathBwd = new Stack<>();
    List<String> path = new ArrayList<>();

    pathBwd.push(AstrMap.get(end).getNodeID());

    while((!end.equals(start) && (AstrMap.get(end).getPrevNode() != null))){
      String push = AstrMap.get(end).getPrevNode().getNodeID();
      pathBwd.push(push);
      end = push;
    }

    while(!pathBwd.isEmpty()){
      path.add(pathBwd.pop());
    }
    return path;
  }


}