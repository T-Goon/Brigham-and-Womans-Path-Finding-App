package edu.wpi.cs3733.D21.teamB.entities;

import edu.wpi.cs3733.D21.teamB.App;
import javafx.application.Application;
import javafx.scene.control.TextArea;
import edu.wpi.cs3733.D21.teamB.entities.LastFocused;
import java.awt.event.FocusEvent;

public class TextAreaFocusable extends TextArea {

    LastFocused lastFocused = LastFocused.getInstance();
    void focusGained(FocusEvent e){
        lastFocused.setAnode(this);
    }

}
