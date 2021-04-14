package edu.wpi.teamB.entities;

import edu.wpi.teamB.database.DatabaseHandler;
import edu.wpi.teamB.pathfinding.Graph;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class Edge {

    private final String edgeID;
    private final String startNodeName;
    private final String endNodeName;


    public boolean floorChange(){
        Graph graph = Graph.getGraph(DatabaseHandler.getDatabaseHandler("main.db"));
        return graph.getNodes().get(startNodeName).getFloor().equals(graph.getNodes().get(endNodeName).getFloor());
    }


    @Override
    public String toString() {
        return "Edge{" +
                "edgeID='" + edgeID + '\'' +
                ", startNodeName='" + startNodeName + '\'' +
                ", endNodeName='" + endNodeName + '\'' +
                '}';
    }
}
