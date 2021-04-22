package edu.wpi.teamB.util.Popup;

import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class PopupManager{

    /**
     * Show the popup in its parent.
     * @param key Key to get each popup object
     */
    public abstract void show(String key);

    /**
     * Hide the popup in its parent
     * @param key Key to get each popup
     */
    public abstract void hide(String key);

}
