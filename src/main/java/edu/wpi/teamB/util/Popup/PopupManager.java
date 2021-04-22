package edu.wpi.teamB.util.Popup;

import javafx.scene.layout.Pane;

import java.util.HashMap;
import java.util.Map;

public abstract class PopupManager{

    private final Map<String, Popup<Pane, Object>> popups = new HashMap<>();

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

    /**
     * Adds the popup to the popups map.
     * @param key Key String for the new popup
     * @param popup The new popup object to store
     */
    public void manageNew(String key, Popup<Pane, Object> popup){

        this.popups.put(key, popup);
    }

    /**
     * Removed the popup from the popups map.
     * @param key Key String for the popup
     */
    public void removeOld(String key){
        this.popups.remove(key);
    }
}
