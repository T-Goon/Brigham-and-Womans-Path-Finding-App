package edu.wpi.cs3733.D21.teamB.pathfinding;
import edu.wpi.cs3733.D21.teamB.entities.map.data.Path;

public abstract class DijkstraTemplate {
    abstract Path findPath(String start, String end, boolean mobility);

}
