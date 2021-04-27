package edu.wpi.cs3733.D21.teamB.pathfinding;

import edu.wpi.cs3733.D21.teamB.entities.map.data.Node;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
public class Pair {
    private String current;
    private ArrayList<Node> path;
}