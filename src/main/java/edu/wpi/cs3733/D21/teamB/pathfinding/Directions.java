package edu.wpi.cs3733.D21.teamB.pathfinding;

import edu.wpi.cs3733.D21.teamB.database.DatabaseHandler;
import edu.wpi.cs3733.D21.teamB.entities.map.FloorSwitcher;
import edu.wpi.cs3733.D21.teamB.entities.map.data.Node;
import edu.wpi.cs3733.D21.teamB.entities.map.data.Path;
import edu.wpi.cs3733.D21.teamB.entities.map.Coord;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
     * The list of instructions the user has to get through to
     * go to their destination
     *
     * @param path the path we want instructions for
     * @return a list of directions of the text directions
     */
    public static List<Direction> instructions(Path path, List<String> stopIDs) {
        List<String> simplePath = simplifyPath(path, stopIDs);
        if (simplePath.isEmpty()) return null;

        String idEnd = simplePath.get(simplePath.size() - 1);
        Graph graph = Graph.getGraph();

        String endLoc = graph.getNodes().get(idEnd).getLongName();
        String startLoc = graph.getNodes().get(simplePath.get(0)).getLongName();

        List<Direction> directions = new ArrayList<>();
        String initialFloor = graph.getNodes().get(simplePath.get(0)).getFloor();
        String currentFloor = initialFloor;
        directions.add(new Direction("Starting route from " + startLoc + " to " + endLoc + ":", null, null, initialFloor));

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
                    String newFloor = getNewFloor(currentFloor, next.getFloorAsInt() - curr.getFloorAsInt());
                    if (next.getNodeID().contains("STAI"))
                        directions.add(new Direction("Walk to floor " + next.getFloor() + ".", (curr.getFloorAsInt() < next.getFloorAsInt()) ? DirectionType.UP_STAIRS : DirectionType.DOWN_STAIRS, getAllNodesBetween(path, curr, next), newFloor));
                    else
                        directions.add(new Direction("Take the elevator to floor " + next.getFloor() + ".", (curr.getFloorAsInt() < next.getFloorAsInt()) ? DirectionType.UP_ELEVATOR : DirectionType.DOWN_ELEVATOR, getAllNodesBetween(path, curr, next), newFloor));
                    currentFloor = newFloor;
                } else
                    directions.add(new Direction("Walk about " + round(distance) + " feet towards " + next.getLongName() + ".", DirectionType.STRAIGHT, getAllNodesBetween(path, curr, next), currentFloor));
            } else {
                if (stopIDs.contains(curr.getNodeID())) {
                    directions.add(new Direction("You have arrived at your stop, " + curr.getLongName() + ".", DirectionType.STOP, getAllNodesBetween(path, curr, next), currentFloor));
                }
                //get turn then get the dist between c and next turn blah and walk dist
                double turn = angleBetweenEdges(graph.getNodes().get(simplePath.get(i - 1)), curr, next);
                if (curr.getNodeID().contains("ELEV") || curr.getNodeID().contains("STAI") || curr.getLongName().contains("elevator")) {
                    String newFloor = getNewFloor(currentFloor, next.getFloorAsInt() - curr.getFloorAsInt());
                    if (next.getNodeID().contains("STAI")) {
                        directions.add(new Direction("Walk to floor " + next.getFloor() + ".", (curr.getFloorAsInt() < next.getFloorAsInt()) ? DirectionType.UP_STAIRS : DirectionType.DOWN_STAIRS, getAllNodesBetween(path, curr, next), newFloor));
                        currentFloor = newFloor;
                        continue;
                    } else if (next.getNodeID().contains("ELEV") || next.getLongName().contains("elevator")) {
                        directions.add(new Direction("Take the elevator to floor " + next.getFloor() + ".", (curr.getFloorAsInt() < next.getFloorAsInt()) ? DirectionType.UP_ELEVATOR : DirectionType.DOWN_ELEVATOR, getAllNodesBetween(path, curr, next), newFloor));
                        currentFloor = newFloor;
                        continue;
                    }
                }

                if (turn > 70 && turn <= 110) {
                    directions.add(new Direction("Take a right and walk about " + round(distance) + " feet towards " + next.getLongName() + ".", DirectionType.RIGHT, getAllNodesBetween(path, curr, next), currentFloor));
                } else if (turn > 110 && turn <= 160) {
                    directions.add(new Direction("Take a sharp right and walk about " + round(distance) + " feet towards " + next.getLongName() + ".", DirectionType.SHARP_RIGHT, getAllNodesBetween(path, curr, next), currentFloor));
                } else if (turn <= 70 && turn > 30) {
                    directions.add(new Direction("Take a slight right and walk about " + round(distance) + " feet towards " + next.getLongName() + ".", DirectionType.SLIGHT_RIGHT, getAllNodesBetween(path, curr, next), currentFloor));
                } else if (turn < -70 && turn >= -110) {
                    directions.add(new Direction("Take a left and walk about " + round(distance) + " feet towards " + next.getLongName() + ".", DirectionType.LEFT, getAllNodesBetween(path, curr, next), currentFloor));
                } else if (turn < -110 && turn >= -160) {
                    directions.add(new Direction("Take a sharp left and walk about " + round(distance) + " feet towards " + next.getLongName() + ".", DirectionType.SHARP_LEFT, getAllNodesBetween(path, curr, next), currentFloor));
                } else if (turn >= -70 && turn <= -30) {
                    directions.add(new Direction("Take a slight left and walk about " + round(distance) + " feet towards " + next.getLongName() + ".", DirectionType.SLIGHT_LEFT, getAllNodesBetween(path, curr, next), currentFloor));
                } else {
                    directions.add(new Direction("Turn around and walk about " + round(distance) + " feet towards " + next.getLongName() + ".", DirectionType.TURN_AROUND, getAllNodesBetween(path, curr, next), currentFloor));
                }
            }
        }
        //add that you have reached your destination
        directions.add(new Direction("You have reached your destination.", DirectionType.STOP, Collections.emptyList(), currentFloor));
        return directions;
    }

    /**
     * Ignores the simple path and gets all the nodes from the base path between the two starting nodes (including them)
     *
     * @param path  the actual path
     * @param start the starting node
     * @param end   the end node
     * @return a list of nodes of the nodes between the starting and end nodes, inclusive on both ends
     */
    private static List<Node> getAllNodesBetween(Path path, Node start, Node end) {
        List<Node> toReturn = new ArrayList<>();
        DatabaseHandler db = DatabaseHandler.getHandler();
        boolean addNodes = false;
        for (String nodeID : path.getPath()) {
            if (nodeID.equals(start.getNodeID())) addNodes = true;
            if (addNodes) {
                Node n = db.getNodeById(nodeID);
                toReturn.add(n);
            }
            if (nodeID.equals(end.getNodeID())) addNodes = false;
        }
        return toReturn;
    }

    /**
     * Returns floor given starting floor and offset
     *
     * @param initialFloor the starting floor of the directions
     * @param delta        the floor change
     * @return a string representation of the floor
     */
    private static String getNewFloor(String initialFloor, int delta) {
        return String.valueOf(FloorSwitcher.floorIDtoInt(initialFloor) + delta).replace("-", "L");
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Direction {
        private final String instruction;
        private final DirectionType direction;
        private final List<Node> nodes;
        private final String floor;
    }

    public enum DirectionType {
        STRAIGHT,
        SHARP_LEFT,
        LEFT,
        SLIGHT_LEFT,
        SHARP_RIGHT,
        RIGHT,
        SLIGHT_RIGHT,
        TURN_AROUND,
        UP_STAIRS,
        DOWN_STAIRS,
        UP_ELEVATOR,
        DOWN_ELEVATOR,
        STOP
    }
}
