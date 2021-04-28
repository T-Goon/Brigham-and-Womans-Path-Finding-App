package edu.wpi.cs3733.D21.teamB.pathfinding;

import edu.wpi.cs3733.D21.teamB.entities.map.data.Node;
import edu.wpi.cs3733.D21.teamB.entities.map.data.Path;
import edu.wpi.cs3733.D21.teamB.entities.map.Coord;

import java.util.ArrayList;
import java.util.List;

public class Directions {

    /**
     * Takes in two coordinates and returns the distance euclidean distance between them
     *
     * @param a the first coordinate
     * @param b the second coordinate
     * @return the distance between them
     */
    public static double dist(Coord a, Coord b) {
        return Math.sqrt(Math.pow((a.getX() - b.getX()), 2) + Math.pow((a.getY() - b.getY()), 2));
    }

    /**
     * Takes in 3 coordinates and calculates the angle of deviance between the line created by the prev and curr
     * and the line created by the curr and next
     *
     * @param prev the users previous node
     * @param curr the node the user is standing on now
     * @param next the node the user is going to go to
     * @return the angle change from their straight path
     */
    public static double angleBetweenEdges(Node prev, Node curr, Node next) {

        Coord prevC = new Coord(prev.getXCoord(), -prev.getYCoord());
        Coord currC = new Coord(curr.getXCoord(), -curr.getYCoord());
        Coord nextC = new Coord(next.getXCoord(), -next.getYCoord());

        double aX = currC.getX() - prevC.getX();
        double aY = currC.getY() - prevC.getY();
        double firstAngle;

        if (aX != 0) {
            firstAngle = java.lang.Math.atan(aY / aX) * (180 / Math.PI);
            if (aX < 0) {
                firstAngle += 180;
            }
        } else {
            if (aY > 0) {
                firstAngle = 90;
            } else {
                firstAngle = -90;
            }
        }

        double bX = nextC.getX() - currC.getX();
        double bY = nextC.getY() - currC.getY();
        double secondAngle;
        if (bX != 0) {
            secondAngle = java.lang.Math.atan(bY / bX) * (180 / Math.PI);
            if (bX < 0) {
                secondAngle += 180;
            }
        } else {
            if (bY > 0) {
                secondAngle = 90;
            } else {
                secondAngle = -90;
            }
        }

        //deviation of the straight line
        double remainder = (firstAngle - secondAngle) % 360.0;

        if (remainder < -180) {
            remainder += 360;
        }

        if (remainder == 180) {
            remainder = 0;
        }


        return remainder;
    }

    /**
     * Takes in a path and generates ids at where the user has made a turn
     *
     * @param path the path that we want to simplify
     * @return the List of strings at where the user changes direction
     */
    public static List<String> simplifyPath(Path path, List<String> stopIDs) {

        List<String> pathID = path.getPath();
        List<String> pathIDCopy = new ArrayList<>(pathID);
        Graph graph = Graph.getGraph();

        Node prevNode = null;
        Node currNode = null;

        for (String nextNodeID : pathID) {

            Node nextNode = graph.getNodes().get(nextNodeID);

            if (prevNode != null) {
                if (!stopIDs.contains(prevNode.getNodeID()) && !stopIDs.contains(currNode.getNodeID()) && !stopIDs.contains(nextNodeID) && prevNode.getFloor().equals(currNode.getFloor()) && currNode.getFloor().equals(nextNode.getFloor()) &&
                        (angleBetweenEdges(prevNode, currNode, nextNode) >= -30) && (angleBetweenEdges(prevNode, currNode, nextNode) <= 30)) {
                    pathIDCopy.remove(currNode.getNodeID());
                }
            }
            prevNode = currNode;
            currNode = nextNode;
        }

        return pathIDCopy;
    }

    public static int round(double rndNum) {
        if (rndNum < 25) {
            return ((int) rndNum + 4) / 5 * 5;
        }
        return Math.round((int) rndNum / 25) * 25;

    }

    /**
     * ElEV, STAI
     * The list of instructions the user has to get through to
     * go to their destination
     *
     * @param path the path we want instructions for
     * @return a list of strings where each element in the list is one instruction
     */
    public static List<String> instructions(Path path, List<String> stopIDs) {
        List<String> simplePath = simplifyPath(path, stopIDs);

        String idEnd = simplePath.get(simplePath.size() - 1);
        Graph graph = Graph.getGraph();

        String endLoc = graph.getNodes().get(idEnd).getLongName();
        String startLoc = graph.getNodes().get(simplePath.get(0)).getLongName();

        List<String> directions = new ArrayList<>();
        directions.add("Starting route from " + startLoc + " to " + endLoc + ":");

        double FT_CONST = 500.0 / 1635;


        double distance;
        for (int i = 0; i < simplePath.size() - 1; i++) {
            Node curr = graph.getNodes().get(simplePath.get(i));
            Node next = graph.getNodes().get(simplePath.get(i + 1));

            Coord currCoord = new Coord(curr.getXCoord(), curr.getYCoord());
            Coord nextCoord = new Coord(next.getXCoord(), next.getYCoord());
            distance = dist(currCoord, nextCoord) * FT_CONST;

            // Starting now
            if (i == 0) {
                // Elevator or stairs
                if (curr.getNodeID().contains("ELEV") || curr.getNodeID().contains("STAI") || curr.getLongName().contains("elevator")) {
                    if (next.getNodeID().contains("STAI")) directions.add("Walk to floor " + next.getFloor() + ".");
                    else directions.add("Take the elevator to floor " + next.getFloor() + ".");
                } else directions.add("Walk about " + round(distance) + " feet towards " + next.getLongName() + ".");
            } else {
                if (stopIDs.contains(curr.getNodeID())) {
                    directions.add("You have arrived at your stop, " + curr.getLongName() + ".");
                }
                //get turn then get the dist between c and next turn blah and walk dist
                double turn = angleBetweenEdges(graph.getNodes().get(simplePath.get(i - 1)), curr, next);
                if (curr.getNodeID().contains("ELEV") || curr.getNodeID().contains("STAI") || curr.getLongName().contains("elevator")) {
                    if (next.getNodeID().contains("STAI")) {
                        directions.add("Walk to floor " + next.getFloor() + ".");
                        continue;
                    } else if (next.getNodeID().contains("ELEV") || next.getLongName().contains("elevator")) {
                        directions.add("Take the elevator to floor " + next.getFloor() + ".");
                        continue;
                    }
                }

                if (turn > 70 && turn <= 110) {
                    directions.add("Take a right and walk about " + round(distance) + " feet towards " + next.getLongName() + ".");
                } else if (turn > 110 && turn <= 160) {
                    directions.add("Take a sharp right and walk about " + round(distance) + " feet towards " + next.getLongName() + ".");
                } else if (turn <= 70 && turn > 30) {
                    directions.add("Take a slight right and walk about " + round(distance) + " feet towards " + next.getLongName() + ".");
                } else if (turn < -70 && turn >= -110) {
                    directions.add("Take a left and walk about " + round(distance) + " feet towards " + next.getLongName() + ".");
                } else if (turn < -110 && turn >= -160) {
                    directions.add("Take a sharp left and walk about " + round(distance) + " feet towards " + next.getLongName() + ".");
                } else if (turn >= -70 && turn <= -30) {
                    directions.add("Take a slight left and walk about " + round(distance) + " feet towards " + next.getLongName() + ".");
                } else {
                    directions.add("Turn around and walk about " + round(distance) + " feet towards " + next.getLongName() + ".");
                }
            }
        }
        //add that you have reached your destination
        directions.add("You have reached your destination.");
        return directions;
    }
}
