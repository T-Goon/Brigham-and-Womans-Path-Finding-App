package edu.wpi.cs3733.D21.teamB.views.map.misc;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.D21.teamB.App;
import edu.wpi.cs3733.D21.teamB.entities.map.TxtDirPopup;
import edu.wpi.cs3733.D21.teamB.pathfinding.Directions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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
        List<Directions.Direction> directions = popup.getDirections();
        HBox first = new HBox();
        Label firstText = new Label(directions.get(0).getInstruction());
        firstText.setFont(Font.font("System", FontWeight.BOLD, 16));
        first.getChildren().add(firstText);
        textHolder.getChildren().add(first);
        for (Directions.Direction dir : directions.subList(1, directions.size())) {
            HBox instruction = new HBox();
            instruction.setSpacing(20);
            Label text = new Label("\t" + dir.getInstruction());
            text.setFont(new Font("System", 14));
            instruction.getChildren().add(text);
            instruction.getChildren().add(getDirectionImage(dir.getDirection()));
            instruction.setOnMouseClicked(e -> {
                popup.setIndex(textHolder.getChildren().indexOf(instruction));
                popup.highlight();
            });
            textHolder.getChildren().add(instruction);
        }
        ((Label) ((HBox) textHolder.getChildren().get(1)).getChildren().get(0)).setTextFill(Color.RED);
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

    private ImageView getDirectionImage(Directions.DirectionType d) {
        String path;
        switch (d) {
            case STOP:
                path = "/edu/wpi/cs3733/D21/teamB/images/maps/directions/stop.png";
                break;
            case STRAIGHT:
                path = "/edu/wpi/cs3733/D21/teamB/images/maps/directions/straightArrow.png";
                break;
            case TURN_AROUND:
                path = "/edu/wpi/cs3733/D21/teamB/images/maps/directions/turnAround.png";
                break;
            case LEFT:
                path = "/edu/wpi/cs3733/D21/teamB/images/maps/directions/leftArrow.png";
                break;
            case SLIGHT_LEFT:
                path = "/edu/wpi/cs3733/D21/teamB/images/maps/directions/slightLeftArrow.png";
                break;
            case SHARP_LEFT:
                path = "/edu/wpi/cs3733/D21/teamB/images/maps/directions/sharpLeftArrow.png";
                break;
            case RIGHT:
                path = "/edu/wpi/cs3733/D21/teamB/images/maps/directions/rightArrow.png";
                break;
            case SLIGHT_RIGHT:
                path = "/edu/wpi/cs3733/D21/teamB/images/maps/directions/slightRightArrow.png";
                break;
            case SHARP_RIGHT:
                path = "/edu/wpi/cs3733/D21/teamB/images/maps/directions/sharpRightArrow.png";
                break;
            case UP_STAIRS:
                path = "/edu/wpi/cs3733/D21/teamB/images/maps/directions/goUpStairs.png";
                break;
            case DOWN_STAIRS:
                path = "/edu/wpi/cs3733/D21/teamB/images/maps/directions/goDownStairs.png";
                break;
            case UP_ELEVATOR:
                path = "/edu/wpi/cs3733/D21/teamB/images/maps/directions/goUpElevator.png";
                break;
            case DOWN_ELEVATOR:
                path = "/edu/wpi/cs3733/D21/teamB/images/maps/directions/goDownElevator.png";
                break;
            default:
                throw new IllegalStateException("How did we get here?");
        }

        ImageView imageView = new ImageView(path);
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);
        return imageView;
    }
}
