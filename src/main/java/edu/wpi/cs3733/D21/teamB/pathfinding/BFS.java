package edu.wpi.cs3733.D21.teamB.pathfinding;

public class BFS extends AlgoTemplate implements Pathfinder {

    /**
     * Since BFS does not take into account heuristic or edge cost
     * we simpily return 1 as every edge is viewed to be the same cost
     *
     * @param heur not used
     * @param newCost not used
     * @return 1
     */
    public double calculateFVal(double newCost, double heur){
        return 1;
    }
}
