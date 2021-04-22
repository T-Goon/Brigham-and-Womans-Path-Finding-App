package edu.wpi.teamB.util.Popup;

import javafx.scene.layout.Pane;
import lombok.Getter;


public abstract class Popup<T extends Pane, E> {

    private final Pane parent;
    private final E data;
    @Getter
    private final String fxmlFile;

    private T window;

    public Popup(Pane parent, String filePath, E data){
        this.parent = parent;
        this.fxmlFile = filePath;
        this.data = data;
    }

    /**
     * Shows the popup in the parent node.
     */
    void show(T node){

        parent.getChildren().add(node);
        window = node;
    }

    /**
     * Removes the popup in the parent node.
     */
    void hide(){

        parent.getChildren().remove(window);
        window = null;
    }
}
