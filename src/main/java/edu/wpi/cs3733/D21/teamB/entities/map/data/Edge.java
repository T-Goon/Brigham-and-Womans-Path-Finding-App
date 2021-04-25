package edu.wpi.cs3733.D21.teamB.entities.map.data;

import edu.wpi.cs3733.D21.teamB.entities.IStoredEntity;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class Edge implements IStoredEntity {

    private final String edgeID;
    private final String startNodeID;
    private final String endNodeID;

    @Override
    public String toString() {
        return "Edge{" +
                "edgeID='" + edgeID + '\'' +
                ", startNodeID='" + startNodeID + '\'' +
                ", endNodeID='" + endNodeID + '\'' +
                '}';
    }
}
