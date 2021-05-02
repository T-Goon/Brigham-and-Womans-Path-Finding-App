package edu.wpi.cs3733.D21.teamB.pathfinding;


import edu.wpi.cs3733.D21.teamB.entities.map.data.Node;
import edu.wpi.cs3733.D21.teamB.entities.map.data.Path;

import java.util.*;

public class AStar extends AlgoTemplate implements Pathfinder {


    public double calculateFVal(double heur, double edgeCost){
        return heur+edgeCost;
    }



    /**
     * Calculates the estimated time it would take to walk a certain path
     *
     * @param path the given Path
     * @return the estimated time string to put in the box
     */
    public static String getEstimatedTime(Path path) {
        //3-4mph average human walking speed
        //1 mph = 88 fpm

        //bWALK00601,1738,1545,1,Parking,WALK,Francis Vining Intersection Top Left,FrancisViningIntTopLeft
        //bWALK01201,3373,1554,1,Parking,WALK,Francis Top Sidewalk 3,FrancisSidewalk3
        //According to google maps, path from one corner of Francis street to other is ~500 ft:
        //using this to get pixels / minute
        //double pixDist = 1635.025;
        double timeConst = (2 / 1635.025);
        double timeDec = path.getTotalPathCost() * timeConst;

        double secondsTime = timeDec * 60;

        int min = (int) Math.floor(timeDec);
        int sec = (int) secondsTime - min * 60;

        if (min == 0) {
            return String.format("%02d", sec) + " sec";
        } else {

            return min + ":" + String.format("%02d", sec) + " min";
        }
    }
}
