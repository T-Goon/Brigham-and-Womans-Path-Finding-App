package edu.wpi.cs3733.D21.teamB.pathfinding;

public class BestFS extends AlgoTemplate implements Pathfinder{

    /**
     * Returns heur as it determines how we move through the path
     *
     * @param newCost not used for Best First Search
     * @param heur Euclidean dist from curr to end node
     * @return heurisitc value
     */
    public double calculateFVal(double newCost, double heur){
        return heur;
    }
}
