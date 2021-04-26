package edu.wpi.cs3733.D21.teamB.util.Popup;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

/**
 * @param <T> the data type of the main container of the popup (ex. in PFMC, it would be MapStack)
 * @param <E> the entity object for any of the popups
 * @param <V> the data type of the main/previous menu (usually VBox)
 */
public abstract class Window<T extends Pane, E, V extends Node> extends Popup<T, E>{

    private final V previous;

    public Window(Pane parent, E data, V previous) {
        super(parent, data);

        this.previous = previous;
    }

    /**
     * Goes back to the previous window in a popup.
     */
    @Override
    public void hide() {
        parent.getChildren().add(previous);
        super.hide();
    }
}
