package edu.wpi.cs3733.D21.teamB.util.Popup;

import javafx.scene.layout.Pane;
import lombok.Getter;

/**
 * @param <T> the data type of the main container of the popup (ex. in PFMC, it would be MapStack)
 * @param <E> the entity object for any of the popups
 */
public abstract class Popup<T extends Pane, E> {

    protected final Pane parent;
    protected T window;

    @Getter
    protected final E data;

    public Popup(Pane parent, E data){
        this.parent = parent;
        this.data = data;
    }

    /**
     * Shows the popup in the parent node.
     */
    protected void show(T node){

        parent.getChildren().add(node);
        window = node;
    }

    /**
     * Removes the popup in the parent node.
     */
    public void hide(){

        parent.getChildren().remove(window);
        window = null;
    }
}
