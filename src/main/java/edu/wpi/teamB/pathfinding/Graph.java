package edu.wpi.teamB.pathfinding;
import java.util.*;
public class Graph {

  /**
   * initHashMap reads in the nodes data and populates a hashMap
   * where the keys are the nodeIDs and the values are all ]
   * the nodes adjacent to the nodeID given.
   *
   * @return the HashMap that contains the nodeIDs and
   * the adjacent nodes
   */
  private static HashMap<String, List<Node>> initHashMap() {
    HashMap<String, List<Node>> adjNodes = new HashMap<String, List<Node>>();
    HashMap<String, Node> nodeInfo = Read.parseCSVNodes("src/main/resources/edu/wpi/teamB/csvfiles/MapBnodes.csv");

    for (Node node : nodeInfo.values()) {
      List<Node> adjNodesList = findAdjNodes(node.getNodeID());
      adjNodes.put(node.getNodeID(), adjNodesList);
    }
    return adjNodes;
  }

  /**
   * findAdjNodes: Finds adjacent nodes of the given node
   *
   * @param ID: The NodeID that we are getting the adjacent nodes of
   * @return The list of adjacent nodes of the given node id
   */
  public static List<Node> findAdjNodes(String ID) {
    List<Node> adjNodeList = new ArrayList<>();
    List<Edge> edgeInfo = Read.parseCSVEdges("src/main/resources/edu/wpi/teamB/csvfiles/MapBedges.csv");
    HashMap<String, Node> nodeInfo = Read.parseCSVNodes("src/main/resources/edu/wpi/teamB/csvfiles/MapBnodes.csv");

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
   * Finds the distance between the start and end node
   * using the distance formula
   *
   * @param start: the start node
   * @param end: the end node
   * @return the distance between the start and end node
   */
  public static double dist(Node start, Node end) {
    double dist = Math.pow((start.getXCoord() - end.getXCoord()), 2) + Math.pow((start.getYCoord() + end.getYCoord()), 2);
    return Math.sqrt(dist);
  }

  /**
   * creates a hashmap with A* metrics set to their initial values
   *
   * @param nodes:
   * @return
   */
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

  /**
   * Finding nodeID that is open with the lowest F value
   *
   * @param hashTbl The table with updated A Start metrics for each ID
   * @return The nodeID that has lowest F value that is closed
   */
  private static String getMin(HashMap<String, Node> hashTbl){
    double min = Double.MAX_VALUE;
    String result = null;

    for(Node data: hashTbl.values()){
      if((data.getfVal() <= min) && (!data.isClosed())){
        min = data.getfVal();
        result = data.getNodeID();
      }
    }
    return result;
  }

  /**
   * Fills in hashtable with A* information we need to find the path
   * @param startStr nodeID to start on
   * @param endStr nodeID to end on
   * @return Hashtable with NodeIDs as values and A* algorithm information to
   * back track the path
   */
  public static HashMap<String, Node> AStrHashTable(String startStr, String endStr) {
    HashMap<String, List<Node>> adjMat = initHashMap();
    HashMap<String, Node> nodes = Read.parseCSVNodes("src/main/resources/edu/wpi/teamB/csvfiles/MapBnodes.csv");
    HashMap<String, Node> shortPathInfo = populateHashmap(nodes.values());


    Node start = nodes.get(startStr);
    Node end = nodes.get(endStr);
    List<Node> adjList;

    double heur = dist(start, end);
    double weight = 0;
    double fVal = heur + weight;

    Node curr = start;

    curr.setAccumWeight(weight);
    curr.setPrevNode(curr.getNodeID());
    curr.setfVal(fVal);

    shortPathInfo.replace(curr.getNodeID(), curr);

    while ((!curr.equals(end))) {

      //getting adj nodes of current node
      adjList = adjMat.get(curr.getNodeID());
      List<Node> adjListRead = new ArrayList();

      if(adjList.contains(end)){
        end.setPrevNode(curr.getNodeID());
        shortPathInfo.replace(end.getNodeID(), end);
        return shortPathInfo;
      }
      //for each adj node
      for (Node node : adjList) {
        if(!adjListRead.contains(node)){
          adjListRead.add(node);

        //check if curr node is close list
          if (!node.isClosed()) {
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
            nodeMetrics.setPrevNode(curr.getNodeID());
            curr.setClosed();
            shortPathInfo.replace(curr.getNodeID(), curr);
          }

        }
        }
      }
      curr.setClosed();
      curr = nodes.get(getMin(shortPathInfo));
    }
    return shortPathInfo;
  }

  /**
   * Uses AStrHashTable to get A* info
   * then backtracks based on what the previous node is
   *
    * @param start node to start on
   * @param end node to end on
   * @return A list of nodes that is the path
   */
  public static List<String> AStr(String start, String end) {
    // go to the end node check its previous node, keep doing this until you reach the start node
    HashMap<String, Node> AstrMap =  AStrHashTable(start, end);
    Stack<String> pathBwd = new Stack<>();
    List<String> path = new ArrayList<>();

    pathBwd.push(AstrMap.get(end).getNodeID());

    while((!end.equals(start) && (AstrMap.get(end).getPrevNode() != null))){
      String push = AstrMap.get(end).getPrevNode();
      pathBwd.push(push);
      end = push;
    }

    while(!pathBwd.isEmpty()){
      path.add(pathBwd.pop());
    }
    return path;
  }


}