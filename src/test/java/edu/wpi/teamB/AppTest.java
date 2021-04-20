package edu.wpi.teamB;

import static org.testfx.api.FxAssert.verifyThat;

import javafx.geometry.VerticalDirection;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationExtension;

import java.util.stream.Stream;

/**
 * This is an integration test for the entire application. Rather than running a single scene
 * individually, it will run the entire application as if you were running it.
 */
@ExtendWith(ApplicationExtension.class)
public class AppTest extends FxRobot {

    /**
     * Setup test suite.
     */
    @BeforeAll
    public static void setup() throws Exception {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(App.class);
        while (App.getPrimaryStage().getScene().lookup("#gif") != null) {
            // Wait for the database to finish initializing
        }
    }


    @Test
    void testLoginPageTabAndEnter() {
        clickOn("#btnStaff");
        verifyThat("Login Page", Node::isVisible);
        clickOn("#username");
        write("admin");
        press(KeyCode.TAB);
        write("password");
        press(KeyCode.ENTER);
        verifyThat("Staff Directory", Node::isVisible);
        clickOn("#btnBack");
    }

    @Test
    void testGuestLogin() {
        clickOn("#btnGuest");
        verifyThat("#btnCovid", Node::isVisible);
        clickOn("#btnBack");
    }


}
