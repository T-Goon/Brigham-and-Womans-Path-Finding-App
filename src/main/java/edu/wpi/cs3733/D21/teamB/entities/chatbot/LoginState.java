package edu.wpi.cs3733.D21.teamB.entities.chatbot;

public class LoginState implements IState {

//    Stack<String> messagesSent;

    @Override
    public String respond(String input) {
        return input + " login!";
    }
}
