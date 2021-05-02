package edu.wpi.cs3733.D21.teamB.pathfinding;


import edu.wpi.cs3733.D21.teamB.entities.map.data.Node;
import edu.wpi.cs3733.D21.teamB.entities.map.data.Path;

import java.util.*;

public class AStar extends AlgoTemplate implements Pathfinder {

    /**
     * Returns the fval for A*
     *
     * @param newCost the new cost calculated by accumulating the edge weights
     * @param heur Euclidean dist from curr to end node
     * @return
     */
    public double calculateFVal(double newCost, double heur) {
        return heur + newCost;
    }

    /**
     * Calculates the estimated time it would take to walk a certain path
     *
     * @param path the given Path
     * @return the estimated time string to put in the box
     */
    public static String getEstimatedTime(Path path) {
        //bWALK00601,1738,1545,
        //bWALK01201,3373,1554
        //According to google maps ~500 ft:
        //double pixDist = 1635.025;

        double timeConst = (2 / 1635.025);
        double timeDec = path.getTotalPathCost() * timeConst;
        double secondsTime = timeDec * 60;

        int min = (int) Math.floor(timeDec);
        int sec = (int) secondsTime - min * 60;

        if (min == 0) return String.format("%02d", sec) + " sec";

        return min + ":" + String.format("%02d", sec) + " min";

    }
}
