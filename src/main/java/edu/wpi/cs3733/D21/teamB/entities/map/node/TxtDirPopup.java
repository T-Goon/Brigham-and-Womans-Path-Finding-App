package edu.wpi.cs3733.D21.teamB.entities.map.node;

import edu.wpi.cs3733.D21.teamB.entities.map.data.TxtDirPopupData;
import edu.wpi.cs3733.D21.teamB.util.Popup.Poppable;
import edu.wpi.cs3733.D21.teamB.util.Popup.Popup;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import net.kurobako.gesturefx.GesturePane;

import java.io.IOException;
import java.util.Objects;

public class TxtDirPopup extends Popup<VBox, TxtDirPopupData> implements Poppable {

    private final GesturePane gpane;

    public TxtDirPopup(Pane parent, TxtDirPopupData data, GesturePane gpane) {
        super(parent, data);
        this.gpane = gpane;
    }

    public void show() {
        VBox txtDirBox;
        try {
            txtDirBox = FXMLLoader.load(
                    Objects.requireNonNull(getClass().getResource("/edu/wpi/cs3733/D21/teamB/views/map/misc/showTextDir.fxml")));

        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        StringBuilder direction = new StringBuilder();

        for (String instruction : data.getInstructions()) {
            direction.append(instruction).append("\n");
        }
        Text textInstr = (Text) txtDirBox.getChildren().get(0);
        textInstr.setText(direction.toString());

        super.show(txtDirBox);
    }

}
