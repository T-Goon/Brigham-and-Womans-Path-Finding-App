package edu.wpi.cs3733.D21.teamB.entities.chatbot;

import java.util.List;

public interface IState {
    List<String> respond(String input);
}
