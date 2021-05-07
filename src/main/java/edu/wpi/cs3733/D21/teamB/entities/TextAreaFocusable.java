package edu.wpi.cs3733.D21.teamB.entities;

import javafx.scene.control.TextArea;
import edu.wpi.cs3733.D21.teamB.entities.LastFocused;
import java.awt.event.FocusEvent;

public class TextAreaFocusable extends TextArea {

    final LastFocused lastFocused = LastFocused.getInstance();
    void focusGained(FocusEvent e){
        lastFocused.setAnode(this);
    }

}
