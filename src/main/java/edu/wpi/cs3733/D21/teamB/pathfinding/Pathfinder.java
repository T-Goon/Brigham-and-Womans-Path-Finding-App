package edu.wpi.cs3733.D21.teamB.pathfinding;

import edu.wpi.cs3733.D21.teamB.entities.map.data.Path;

import java.util.Stack;

public interface Pathfinder {
    Path findPath(Stack<String> nodes);

    Path findPath(String startID, String endID);
}
