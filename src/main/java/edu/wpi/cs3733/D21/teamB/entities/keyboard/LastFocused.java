package edu.wpi.cs3733.D21.teamB.entities.keyboard;

import com.jfoenix.controls.JFXButton;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

public class LastFocused {
    private static LastFocused single_instance = null;
    private Node anode;

    private LastFocused(){
        this.anode = new VBox();
    }

    public static LastFocused getInstance(){
        if (single_instance == null) {
            single_instance = new LastFocused();
        }

        return single_instance;
    }
    public void setAnode(Node anode) {
        if (!(anode.getClass() == JFXButton.class)) {
            this.anode = anode;
        }
    }
    public Node getAnode() {
        return anode;
    }

    public void requestFocus() {
        anode.requestFocus();
    }
}
