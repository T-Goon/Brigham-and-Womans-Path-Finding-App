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
        double bLen = currC.getY()-firstTriangle.getY();
        double firstAngle = java.lang.Math.atan(bLen / aLen)* (180/Math.PI);

        Coord secondTriangle = new Coord(nextC.getX(),currC.getY());
        double aLen1 = currC.getX() - secondTriangle.getX();
        double bLen1 = nextC.getY()-secondTriangle.getY();
        double secondAngle = 180 - java.lang.Math.atan(bLen1 / aLen1) * (180/Math.PI);

        return secondAngle + firstAngle;
    }

    /**
     * @param path takes in path we want instructions for
     * @return No two consecutive are in a line
     */
    public static List<String> simplifyPath(Path path) {

        List<String> pathID = path.getPath();
        List<String> pathIDCopy = new ArrayList<>(pathID);
        Path retPath = new Path();

        Graph graph = Graph.getGraph();

        Node prevNode = null;
        Node currNode = null;

        for(String nextNodeID : pathID){

            Node nextNode = graph.getNodes().get(nextNodeID);

            if(prevNode != null){
                if((angleBetweenEdges(prevNode, currNode, nextNode) >= 150) && (angleBetweenEdges(prevNode, currNode, nextNode) <= 210)){
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
    private List<String> inst(Path path) {
        List<String> simplePath = simplifyPath(path);
        Graph graph = Graph.getGraph();
        List<String> directions = new ArrayList<>();
        double FT_CONST = 500/1635.02;

        Node curr = null;
        Node prev = null;

        double turn;


        for(String id : simplePath){
            Node next = graph.getNodes().get(id);
            if(curr != null){
                Coord prevCoord = new Coord(curr.getXCoord(), curr.getYCoord());
                Coord currCoord = new Coord(next.getXCoord(), curr.getYCoord());
                double feet = dist(prevCoord, currCoord)*FT_CONST;

                if(prev != null){
                    turn = angleBetweenEdges(prev, curr, next);

                    if(turn<150 && turn>=90){
                        //take a right
                    }
                    else if(turn>0 && turn<90){
                        //sharp right
                    }
                    else if(turn>=210 && turn<=270){
                        //take a left
                    }
                    else if(turn==0 || (turn>270 && turn<=360)){
                        //sharp left
                    }
                    else{
                        //straight (I don't even think I would get here lol)
                    }
                    //turn blah and walk feet feet to long name
                }
                else{
                    //head straight feet feet to long name
                }

            }
        }
        //you have reached your destination

        return null;
    }


}
