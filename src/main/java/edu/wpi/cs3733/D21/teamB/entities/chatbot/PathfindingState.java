package edu.wpi.cs3733.D21.teamB.entities.chatbot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PathfindingState implements IState {
    @Override
    public List<String> respond(String input) {
        return new ArrayList<>(Collections.singleton(input + " pathfinding!"));
    }
}
