package edu.wpi.teamB.pathfinding;

import edu.wpi.teamB.entities.map.data.Path;
import edu.wpi.teamB.entities.map.data.Node;
import edu.wpi.teamB.entities.map.Coord;

import java.util.ArrayList;
import java.util.List;

public class Directions {

    //takes a path and returns a string
    public static double dist(Coord a, Coord b) {
        return Math.sqrt(Math.pow((a.getX() - b.getX()), 2) + Math.pow((a.getY() - b.getY()), 2));
    }

    public static double angleBetweenEdges(Node prev, Node curr, Node next) {
        Coord prevC = new Coord(prev.getXCoord(), prev.getYCoord());
        Coord currC = new Coord(curr.getXCoord(), curr.getYCoord());
        Coord nextC = new Coord(next.getXCoord(), next.getYCoord());

        Coord firstTriangle = new Coord(currC.getX(), prevC.getY());

        double aLen = prevC.getX() - firstTriangle.getX();
        double bLen = currC.getY() - firstTriangle.getY();
        double firstAngle = java.lang.Math.atan(bLen / aLen) * (180 / Math.PI);

        Coord secondTriangle = new Coord(nextC.getX(), currC.getY());
        double aLen1 = currC.getX() - secondTriangle.getX();
        double bLen1 = nextC.getY() - secondTriangle.getY();
        double secondAngle = 180 - java.lang.Math.atan(bLen1 / aLen1) * (180 / Math.PI);

        return secondAngle + firstAngle;
    }

    /**
     * @param path takes in path we want instructions for
     * @return No two consecutive are in a line
     */
    public static List<String> simplifyPath(Path path) {

        List<String> pathID = path.getPath();
        List<String> pathIDCopy = new ArrayList<>(pathID);
        Graph graph = Graph.getGraph();

        Node prevNode = null;
        Node currNode = null;

        for (String nextNodeID : pathID) {

            Node nextNode = graph.getNodes().get(nextNodeID);

            if (prevNode != null) {
                if ((angleBetweenEdges(prevNode, currNode, nextNode) >= 150) && (angleBetweenEdges(prevNode, currNode, nextNode) <= 210)) {
                    pathIDCopy.remove(currNode.getNodeID());
                }
            }
            prevNode = currNode;
            currNode = nextNode;
        }

        return pathIDCopy;
    }

    /**
     * @param path pass in path we want to create instructions for
     * @return String instructions for that path
     */
    public static List<String> inst(Path path) {
        List<String> simplePath = simplifyPath(path);

        String idEnd = simplePath.get(simplePath.size()-1);
        Graph graph = Graph.getGraph();

        String endloc = graph.getNodes().get(idEnd).getLongName();

        List<String> directions = new ArrayList<>();
        directions.add("Starting route to " + endloc);

        double FT_CONST = 5000/1635;

        Node prev = null;
        Node curr = null;

        double distance;
        String dir = "";
        for (String id : simplePath) {
            Node next = graph.getNodes().get(id);

            if (curr != null && prev == null) {
                Coord currCoord = new Coord(curr.getXCoord(), curr.getYCoord());
                Coord nextCoord = new Coord(next.getXCoord(), next.getYCoord());
                distance = dist(currCoord, nextCoord)*FT_CONST;
                //starting directions get the distance between the current and next and convert to feet
                dir += "Walk " + (int)distance + " feet towards " + next.getLongName();
                directions.add(dir);
            }

            if(prev != null) {
                dir = "";
                Coord currCoord = new Coord(curr.getXCoord(), curr.getYCoord());
                Coord nextCoord = new Coord(next.getXCoord(), next.getYCoord());
                distance = dist(currCoord, nextCoord)*FT_CONST;
                //get turn then get the dist between c and next turn blah and walk dist

                double turn = angleBetweenEdges(prev, curr, next);

                if (turn < 150 && turn >= 80) {
                    //take a right
                    dir += "Take a right and walk " + (int)distance + " feet towards " + next.getLongName();
                } else if (turn > 0 && turn < 80) {
                    //sharp right
                    dir += "Take a sharp right and walk " + (int)distance + " feet towards " + next.getLongName();
                } else if (turn >= 210 && turn <= 280) {
                    dir += "Take a left and walk " + (int)distance + " feet towards " + next.getLongName();
                } else{// if (turn == 0 || (turn > 280 && turn <= 360)) {
                    dir += "Take a sharp left and walk " + (int)distance + " feet towards " + next.getLongName();
                }
                directions.add(dir);
            }

            prev = curr;
            curr = next;
        }
        //add that you have reached your destination
        directions.add("You have reached your destination");
        return directions;
    }
}
