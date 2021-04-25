package edu.wpi.cs3733.D21.teamB.pathfinding;

import edu.wpi.cs3733.D21.teamB.entities.map.data.Node;
import edu.wpi.cs3733.D21.teamB.entities.map.data.Path;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class DFS implements Pathfinder {

    /**
     * Finds the path between multiple nodes by using DFS
     *
     * @param nodes a Stack of the destination nodes in order. (Start, dest1, dest2, ...)
     * @return A Path with the full and complete path through the destinations
     */
    public Path findPath(Stack<String> nodes) {
        // todo implement later
        return null;
    }

    /**
     * Implementation of depth-first search given the starting nodeID.
     *
     * @param start the starting NodeID
     * @param end   the ending NodeID
     * @return a stack of the path of nodeIDs from start to end
     */
    public Path findPath(String start, String end) {
        Graph graph = Graph.getGraph();
        List<String> path = dfsHelper(start, end, new ArrayList<>(), graph);
        double cost = graph.calculateCost(path);
        return new Path(path, cost);
    }

    /**
     * DFS helper function.
     *
     * @param start   the current starting nodeID
     * @param end     the final nodeID
     * @param visited list of nodeIDs already visited
     * @return a stack of the path of nodeIDs from start to end
     */
    public static List<String> dfsHelper(String start, String end, List<String> visited, Graph graph) {
        visited.add(start);

        // If the start equals the end, this is the recursive base case
        if (start.equals(end)) {
            return Collections.singletonList(start);
        }

        // Otherwise, run dfs on all the neighbors
        List<String> result;
        Stack<String> stack = new Stack<>();
        for (Node neighbor : graph.getAdjNodesById(start)) {
            result = new ArrayList<>();

            // If the neighbor is not already visited
            if (!visited.contains(neighbor.getNodeID())) {
                // Recursively see if it is part of path
                result.addAll(dfsHelper(neighbor.getNodeID(), end, visited, graph));
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
}
