package edu.wpi.teamB.entities.map;

import com.jfoenix.controls.JFXButton;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class LabelButton extends HBox {
    private Label label;
    private JFXButton button;

    public LabelButton(Label label) {
        super();

        this.label = label;
        this.getChildren().add(label);
        this.setAlignment(Pos.CENTER_LEFT);
    }

    public LabelButton(Label label, JFXButton button) {
        super(5);

        this.label = label;
        this.button = button;
        this.getChildren().addAll(label, button);
        this.setAlignment(Pos.CENTER_LEFT);
    }
}
