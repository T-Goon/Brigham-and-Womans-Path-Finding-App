package edu.wpi.cs3733.D21.teamB.views.map.misc;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.D21.teamB.App;
import edu.wpi.cs3733.D21.teamB.entities.map.TxtDirPopup;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TxtDirPopupController implements Initializable {

    @FXML
    private JFXButton btnRestart;

    @FXML
    private JFXButton btnPrevious;

    @FXML
    private JFXButton btnNext;

    @FXML
    private JFXButton btnClose;

    @FXML
    private VBox textHolder;

    private TxtDirPopup popup;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        popup = (TxtDirPopup) App.getPrimaryStage().getUserData();
        popup.setInstructionBox(textHolder);

        // Add all the text directions to the holder
        List<String> directions = popup.getDirections();
        HBox first = new HBox();
        Label firstText = new Label(directions.get(0));
        firstText.setFont(Font.font("System", FontWeight.BOLD, 16));
        first.getChildren().add(firstText);
        textHolder.getChildren().add(first);
        for (String s : directions.subList(1, directions.size())) {
            HBox instruction = new HBox();
            Label text = new Label("\t" + s);
            text.setFont(new Font("System", 14));
            instruction.getChildren().add(text);
            instruction.setOnMouseClicked(e -> {
                popup.setIndex(textHolder.getChildren().indexOf(instruction));
                popup.highlight();
            });
            textHolder.getChildren().add(instruction);
        }

    }

    @FXML
    private void handleButtonAction(ActionEvent e) {
        JFXButton btn = (JFXButton) e.getSource();
        switch (btn.getId()) {
            case "btnRestart":
                popup.restart();
                break;
            case "btnPrevious":
                popup.previous();
                break;
            case "btnNext":
                popup.next();
                break;
            case "btnClose":
                popup.hide();
                break;
        }
    }

}
