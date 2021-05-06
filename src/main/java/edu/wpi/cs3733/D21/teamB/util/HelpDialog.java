package edu.wpi.cs3733.D21.teamB.util;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class HelpDialog {

    /**
     * Loads a help dialog on whatever stackpane is given to it
     *
     * @param stackPane the stackpane of the page
     * @param text      the help text to show
     */
    public static void loadHelpDialog(StackPane stackPane, String text) {
        JFXDialogLayout helpLayout = new JFXDialogLayout();

        Text helpText = new Text(text);
        helpText.setFont(new Font("MS Reference Sans Serif", 14));

        Label headerLabel = new Label("Help");
        headerLabel.setFont(new Font("MS Reference Sans Serif", 18));

        helpLayout.setHeading(headerLabel);
        helpLayout.setBody(helpText);
        JFXDialog helpWindow = new JFXDialog(stackPane, helpLayout, JFXDialog.DialogTransition.CENTER);

        JFXButton button = new JFXButton("Close");
        button.setOnAction(event -> helpWindow.close());
        helpLayout.setActions(button);
        button.setDefaultButton(true);
        helpWindow.show();
    }
}
