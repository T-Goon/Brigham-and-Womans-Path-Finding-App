package edu.wpi.cs3733.D21.teamB.entities.map.data;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
public class Path {
    List<String> path;
    double totalPathCost;


    public Path() {
    }

    public Path(List<String> path, double cost) {
        this.path = path;
        this.totalPathCost = cost;
    }

    public List<String> getFloorPathSegment(String floor){
        List<String> ret = new ArrayList<>();

        for(String nodeID: path){

            String floorFromID = nodeID.substring(nodeID.length()-2);

            //parses 01, 02, and 03 down to 1, 2, or 3
            if(floorFromID.charAt(0)=='0'){
                floorFromID = floorFromID.substring(1);
            }

            //Use the last two digits of the nodeID to test for floor
            if(floorFromID.equals(floor)){
                ret.add(nodeID);
            }
        }

        return ret;

    }
}
