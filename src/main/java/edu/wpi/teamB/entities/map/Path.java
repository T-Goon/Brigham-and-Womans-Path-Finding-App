package edu.wpi.teamB.entities.map;

import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode
public class Path {
    List<String> path;
    double totalPathCost;

    public List<String> getPath() {
        return path;
    }

    public double getTotalPathCost() {
        return totalPathCost;
    }

    public Path() {
    }

    public Path(List<String> path, double cost) {
        this.path = path;
        this.totalPathCost = cost;
    }
}
