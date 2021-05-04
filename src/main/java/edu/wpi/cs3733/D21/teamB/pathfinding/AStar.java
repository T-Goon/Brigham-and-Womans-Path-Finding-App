package edu.wpi.cs3733.D21.teamB.pathfinding;

public class AStar extends AlgoTemplate implements Pathfinder {

    /**
     * Returns the fval for A*
     *
     * @param newCost the new cost calculated by accumulating the edge weights
     * @param heur Euclidean dist from curr to end node
     * @return fValue
     */
    public double calculateFVal(double newCost, double heur) {
        return heur + newCost;
    }

}
