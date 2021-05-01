package edu.wpi.cs3733.D21.teamB.entities;

import com.jfoenix.controls.JFXButton;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class OnScreenKeyboard {
    private static OnScreenKeyboard single_instance = null;

    private boolean shifted;
    private VBox keyboard;
    private HBox numRow;
    private HBox topRow;
    private HBox midRow;
    private HBox bottomRow;
    private Pane parent;
    private LastFocused lastFocused;

    private OnScreenKeyboard(){
        this.shifted = false;
        this.keyboard = new VBox();
        this.numRow = new HBox();
        this.topRow = new HBox();
        this.midRow = new HBox();
        this.bottomRow = new HBox();
        this.parent = null;
        lastFocused = LastFocused.getInstance();
    }

    public static OnScreenKeyboard getInstance(){
        if (single_instance == null)
        {
            single_instance = new OnScreenKeyboard();
        }
        return single_instance;
    }

    public void keyboardAesthethic(){
        keyboard.setVisible(false);
        keyboard.getChildren().addAll(numRow, topRow, midRow, bottomRow);
        keyboard.setFillWidth(false);
        keyboard.setPrefHeight(Region.USE_COMPUTED_SIZE);
        keyboard.setPrefWidth(Region.USE_COMPUTED_SIZE);
        keyboard.setMaxHeight(Region.USE_PREF_SIZE);
        keyboard.setMaxWidth(Region.USE_PREF_SIZE);
        BackgroundFill bgFill = new BackgroundFill(javafx.scene.paint.Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY);
        Background keyboardBG = new Background(bgFill);
        keyboard.setBackground(keyboardBG);
    }

    public void initKeyboard(Pane aParent) throws AWTException {
        keyboardAesthethic();
        ArrayList<JFXButton> buttonList = new ArrayList<JFXButton>();
        ArrayList<String> altText = new ArrayList<>();
        ArrayList<String> baseText = new ArrayList<>();
        Robot robot = new Robot();
        String row1 = "1234567890-=";
        String sRow1 = "!@#$%^&*()_+";
        String row2 = "qwertyuiop[]";
        String sRow2 = "QWERTYUIOP{}";
        String row3 = "asdfghjkl;";
        String sRow3 = "ASDFGHJKL:";
        String row4 = "zxcvbnm,./";
        String sRow4 = "ZXCVBNM<>?";
        String[] rows = {row1, row2, row3, row4};
        String[] sRows = {sRow1, sRow2, sRow3, sRow4};
        for (int i = 0; i < rows.length; i++) {
            char[] keys = rows[i].toCharArray();
            char[] altkeys = sRows[i].toCharArray();
            for (int j = 0; j < keys.length; j++) {
                JFXButton button = new JFXButton(Character.toString(keys[j]));
                altText.add(Character.toString(altkeys[j]));
                baseText.add(Character.toString(keys[j]));
                buttonList.add(button);
                button.setTextFill(Color.YELLOW);
                Character keyIs = Character.toUpperCase(button.getText().toCharArray()[0]);
                button.setOnAction(event -> {
                    lastFocused.requestFocus();
                    if (!shifted) {
                        robot.keyPress(keyIs);
                        robot.keyRelease(keyIs);
                    }
                    if (shifted) {
                        robot.keyPress(KeyEvent.VK_SHIFT);
                        robot.keyPress(keyIs);
                        robot.keyRelease(keyIs);
                        robot.keyRelease(KeyEvent.VK_SHIFT);
                    }
                });
                if (i == 0) {
                    numRow.getChildren().add(button);
                } else if (i == 1) {
                    topRow.getChildren().add(button);
                } else if (i == 2) {
                    midRow.getChildren().add(button);
                } else if (i == 3) {
                    bottomRow.getChildren().add(button);
                }
            }
        }
        JFXButton shift = new JFXButton("Shift");
        shift.setOnAction(event -> {
            OnScreenKeyboard osk = OnScreenKeyboard.getInstance();
            if (!osk.shifted) {
                for (int i = 0; i < buttonList.size(); i++) {
                    JFXButton currButton = buttonList.get(i);
                    currButton.setText(altText.get(i));
                }
                osk.setShifted(true);
            } else {
                for (int i = 0; i < buttonList.size(); i++) {
                    JFXButton currButton = buttonList.get(i);
                    currButton.setText(baseText.get(i));
                }
                osk.setShifted(false);
            }
        });
        bottomRow.getChildren().add(shift);
        shift.setTextFill(Color.YELLOW);
        aParent.getChildren().add(keyboard);
        keyboard.setLayoutY(500);
        keyboard.setLayoutX(500);
    }

    public VBox getKeyboard() {
        return keyboard;
    }
    public void setShifted(boolean shifted) {
        this.shifted = shifted;
    }

    public Pane getParent() {
        return parent;
    }

    public void setParent(Pane parent) {
        this.parent = parent;
    }
}
