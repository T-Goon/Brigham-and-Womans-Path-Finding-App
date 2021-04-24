package edu.wpi.teamB.pathfinding;

import edu.wpi.teamB.entities.map.Path;
import edu.wpi.teamB.entities.map.Path;
import edu.wpi.teamB.entities.map.Node;
import edu.wpi.teamB.entities.map.Coord;

public class Directions {

    //takes a path and returns a string
    public static double dist(Coord a, Coord b) {
        return Math.sqrt(Math.pow((a.getX() - b.getY()), 2) + Math.pow((a.getY() - b.getY()), 2));
    }



    private double angleBetweenEdges(Node prev, Node curr, Node next){
        Coord prevC = new Coord(prev.getXCoord(), prev.getYCoord());
        Coord currC = new Coord(curr.getXCoord(), curr.getYCoord());
        Coord nextC = new Coord(next.getXCoord(), next.getYCoord());

        Coord firstTriangle = new Coord(curr.getXCoord(), prev.getYCoord());
        Coord secondTriangle = new Coord(next.getXCoord(), curr.getYCoord());

        double aLen = dist(prevC, firstTriangle);
        double bLen = dist(currC, firstTriangle);
        double firstAngle = java.lang.Math.atan(bLen/aLen);

        double aLen1 = dist(currC, secondTriangle);
        double bLen1 = dist(nextC, secondTriangle);
        double secondAngle = 180 - java.lang.Math.atan(bLen1/aLen1);

        return secondAngle + firstAngle;
    }


    /**
     *
     * @param path takes in path we want instructions for
     * @return No two consecutive are in a line
     */
    private Path simplifyPath(Path path){
        //det all nodes very near 180 degrees
        return null;
    }

    //turn simplified path into a list of Strings

    /**
     *
     * @param simplifiedPath pass in path we want to create instructions for
     * @return String instructions for that path
     */
    private String inst(Path simplifiedPath){
        //det all nodes very near 180 degrees
        return null;
    }


}
