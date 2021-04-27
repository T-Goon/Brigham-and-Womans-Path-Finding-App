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


}
