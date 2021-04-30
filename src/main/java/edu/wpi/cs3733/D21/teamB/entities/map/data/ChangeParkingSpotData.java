package edu.wpi.cs3733.D21.teamB.entities.map.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.kurobako.gesturefx.GesturePane;

@AllArgsConstructor
@Getter
public class ChangeParkingSpotData {
    private final GesturePane gesturePane;
    private final String parkingName;
}
