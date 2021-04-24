package edu.wpi.teamB.util.Popup;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

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
