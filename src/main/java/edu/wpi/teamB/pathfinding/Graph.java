package edu.wpi.teamB.pathfinding;

import edu.wpi.teamB.entities.Edge;
import edu.wpi.teamB.entities.Node;

import java.util.*;

public class Graph {

    private static HashMap<String, List<Node>> initHashMap() {
        HashMap<String, List<Node>> adjNodes = new HashMap<String, List<Node>>();
        HashMap<String, Node> nodeInfo = Read.parseCSVNodes();

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
        HashMap<String, Node> nodeInfo = Read.parseCSVNodes();

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
            node.setFVal(Double.MAX_VALUE);
            node.setPrevNode(null);
            spInfo.put(node.getNodeID(), node);
        }
        return spInfo;
    }

    private static String getMin(HashMap<String, Node> hashTbl) {
        //priority queue
        double min = Double.MAX_VALUE;
        String result = null;

        for (Node data : hashTbl.values()) {
            if ((data.getFVal() < min) && (!data.isClosed())) {
                min = data.getFVal();
                result = data.getNodeID();
            }
        }
        return result;
    }

    private static HashMap<String, Node> AStrHashTable(Node startStr, Node endStr) {
        HashMap<String, List<Node>> adjMat = initHashMap();
        HashMap<String, Node> nodes = Read.parseCSVNodes();

        Node start = nodes.get(startStr);
        Node end = nodes.get(endStr);

        HashMap<String, Node> shortPathInfo = populateHashmap(nodes.values());
        List<Node> adjList;
        List<Node> close = new ArrayList<>();

        double heur = dist(start, end);
        double weight = 0; //"gVal"
        double fVal = heur + weight;

        Node curr = start;

        curr.setAccumWeight(weight);
        curr.setPrevNode(curr);
        curr.setFVal(fVal);

        shortPathInfo.replace(start.getNodeID(), curr);

        while ((!curr.equals(end))) {
            //getting adj nodes of current node
            adjList = adjMat.get(curr.getNodeID());

            //for each adj node
            for (Node node : adjList) {
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
                    if (fVal < nodeMetrics.getFVal()) {
                        nodeMetrics.setFVal(fVal);
                        nodeMetrics.setAccumWeight(weight);
                        nodeMetrics.setPrevNode(curr);
                    }

                }

            }
            close.add(curr);
            curr = nodes.get(getMin(shortPathInfo));
        }
        return shortPathInfo;
    }

    //backtracking to find A* path
    public static List<String> AStr(Node start, Node end, HashMap<String, Node> aStrData) {
        // go to the end node check its previous node, keep doing this until you reach the start node
        Stack<String> path = new Stack<>();
        List<String> fPath = new ArrayList<>();

        while (!start.equals(end)) {
            Node node = aStrData.get(end.getNodeID()).getPrevNode();
            path.push(node.getNodeID());
            end = node;
        }
        for (String stack : path) {
            String add = path.pop();
            fPath.add(add);
        }
        return fPath;
    }
}