package edu.wpi.cs3733.D21.teamB.entities;

import com.jfoenix.controls.JFXButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class OnScreenKeyboard {
    private static OnScreenKeyboard single_instance = null;

    private boolean shifted;
    private boolean initialized;
    private final VBox keyboard;
    private final HBox numRow;
    private final HBox topRow;
    private final HBox midRow;
    private final HBox bottomRow;
    private Pane parent;
    private final LastFocused lastFocused;

    private OnScreenKeyboard() {
        this.shifted = false;
        this.initialized = false;
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
        keyboard.setAlignment(Pos.CENTER);
        numRow.setAlignment(Pos.CENTER);
        topRow.setAlignment(Pos.CENTER);
        midRow.setAlignment(Pos.CENTER);
        bottomRow.setAlignment(Pos.CENTER);
        keyboard.setFillWidth(false);
        keyboard.setPrefHeight(Region.USE_COMPUTED_SIZE);
        keyboard.setPrefWidth(Region.USE_COMPUTED_SIZE);
        keyboard.setMaxHeight(Region.USE_PREF_SIZE);
        keyboard.setMaxWidth(Region.USE_PREF_SIZE);
        BackgroundFill bgFill = new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY);
        Background keyboardBG = new Background(bgFill);
        keyboard.setBackground(keyboardBG);
        keyboard.setOnMouseDragged(this::drag);
    }

    private void drag(MouseEvent event) {
        Node n = (Node)event.getSource();
        n.setTranslateX(n.getTranslateX() + event.getX());
        n.setTranslateY(n.getTranslateY() + event.getY());
    }

    public void initKeyboard(Pane aParent) throws AWTException {
        keyboardAesthethic();
        ArrayList<JFXButton> buttonList = new ArrayList<>();
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
                button.setTextFill(Color.WHITE);
                char keyIs = Character.toUpperCase(button.getText().toCharArray()[0]);
                button.setOnAction(event -> {
                    if (!(lastFocused.getAnode().isFocused())) {
                        lastFocused.requestFocus();
                        for (int k = 0; k < 100; k++) {
                            robot.keyPress(KeyEvent.VK_RIGHT);
                            robot.keyRelease(KeyEvent.VK_RIGHT);
                        }
                    }
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
                } else {
                    bottomRow.getChildren().add(button);
                }
            }
        }
        JFXButton shift = new JFXButton("Shift");
        JFXButton bckSpce = new JFXButton("<-");
        bckSpce.setOnAction(event -> {
            if (!(lastFocused.getAnode().isFocused())) {
                lastFocused.requestFocus();
                for (int k = 0; k < 100; k++) {
                    robot.keyPress(KeyEvent.VK_RIGHT);
                    robot.keyRelease(KeyEvent.VK_RIGHT);
                }
            }
            if (!shifted) {
                robot.keyPress(KeyEvent.VK_BACK_SPACE);
                robot.keyRelease(KeyEvent.VK_BACK_SPACE);
            }
            if (shifted) {
                robot.keyPress(KeyEvent.VK_SHIFT);
                robot.keyPress(KeyEvent.VK_BACK_SPACE);
                robot.keyRelease(KeyEvent.VK_BACK_SPACE);
                robot.keyRelease(KeyEvent.VK_SHIFT);
            }
        });
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
        numRow.getChildren().add(bckSpce);
        bottomRow.getChildren().add(shift);
        bckSpce.setTextFill(Color.WHITE);
        shift.setTextFill(Color.WHITE);
        if(!(aParent.getChildren().contains(keyboard))) {
            aParent.getChildren().add(keyboard);
        }
        initialized = true;
    }

    public boolean getInitialized(){
        return initialized;
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
        parent.getChildren().add(keyboard);
    }
}
