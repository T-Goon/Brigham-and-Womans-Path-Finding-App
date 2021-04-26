package edu.wpi.cs3733.D21.teamB.pathfinding;

import edu.wpi.cs3733.D21.teamB.entities.map.data.Path;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public interface Pathfinder {
    /**
     * Finds the path between multiple nodes by using DFS
     *
     * @param nodes a Stack of the destination nodes in order. (Start, dest1, dest2, ...)
     * @return A Path with the full and complete path through the destinations
     */
    default Path findPath(Stack<String> nodes) {
        Path retPath = new Path();

        while (nodes.size() > 1) {
            Path tempPath = findPath(nodes.pop(), nodes.peek());
            if (tempPath == null) {
                retPath.setPath(new ArrayList<>());
                return retPath;
            }

            List<String> tempPathList = tempPath.getPath();
            List<String> totalPath = retPath.getPath();

            //If the return path is not initialized, initialize it
            if (totalPath == null) {
                totalPath = new LinkedList<>();
            }

            //remove the start node from any path other than the first one in order to avoid doubling nodes in the path
            if (totalPath.size() > 0) {
                tempPathList.remove(0);
            }

            //add this segment's path to the return path
            totalPath.addAll(tempPathList);
            retPath.setPath(totalPath);

            //Add this path segment to total path cost
            retPath.setTotalPathCost(retPath.getTotalPathCost() + tempPath.getTotalPathCost());
        }
        return retPath;
    }

    Path findPath(String startID, String endID);
}
