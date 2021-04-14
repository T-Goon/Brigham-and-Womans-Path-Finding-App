package edu.wpi.teamB.entities;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class Edge {

    private final String edgeID;
    private final String startNodeName;
    private final String endNodeName;

    @Override
    public String toString() {
        return "Edge{" +
                "edgeID='" + edgeID + '\'' +
                ", startNodeName='" + startNodeName + '\'' +
                ", endNodeName='" + endNodeName + '\'' +
                '}';
    }
}
