package edu.wpi.cs3733.D21.teamB.entities.chatbot;

public class CovidState implements IState {

    @Override
    public String respond(String input) {
        return input + " covid!";
    }
}
