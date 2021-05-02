package edu.wpi.cs3733.D21.teamB.pathfinding;

public class BestFS extends AlgoTemplate implements Pathfinder{

    public double calculateFVal(double newCost, double heur){
        return heur;
    }
}
