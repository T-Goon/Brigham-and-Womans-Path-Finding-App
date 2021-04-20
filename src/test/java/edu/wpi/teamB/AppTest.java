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
    }

    @Test
    void testGraphicalMapEditor() {
        // Get to edit map menu
        clickOn("#btnStaff");
        verifyThat("Login Page", Node::isVisible);
        clickOn("#username");
        write("admin");
        press(KeyCode.TAB);
        write("password");
        press(KeyCode.ENTER);
        verifyThat("Staff Directory", Node::isVisible);
        clickOn("#btnDirections");
        clickOn("#btnEditMap");

        // Open add node popup
        moveTo(400, 400);
        doubleClickOn(MouseButton.PRIMARY);

        // Fill in node info
        clickOn("#nodeID");
        write("nodeabcd12345");
        clickOn("#building");
        write("building123");
        clickOn("#nodeType");
        clickOn("Bathrooms");
        clickOn("#longName");
        write("some node");
        clickOn("#shortName");
        write("sn");
        clickOn("#btnAddNode");

        // Open add node popup
        moveTo(400, 500);
        doubleClickOn(MouseButton.PRIMARY);

        // Fill in node info
        clickOn("#nodeID");
        write("nodeabcd123452");
        clickOn("#building");
        write("building123");
        clickOn("#nodeType");
        clickOn("Bathrooms");
        clickOn("#longName");
        write("some node2");
        clickOn("#shortName");
        write("sn2");
        clickOn("#btnAddNode");

        // Edit Node
        verifyThat("#nodeabcd12345Icon", Node::isVisible);
        clickOn("#nodeabcd12345Icon");
        clickOn("#btnEditNode");
        clickOn("#nodeType");
        clickOn("Lab Rooms");
        clickOn("#btnUpdate");

        // Set start node for edge
        clickOn("#nodeabcd12345Icon");
        clickOn("#btnAddEdge");
        clickOn("#btnStart");
        clickOn("#btnCancel");
        clickOn("#btnCancel");

        // Set end node for edge
        clickOn("#nodeabcd123452Icon");
        clickOn("#btnAddEdge");
        clickOn("#btnEnd");
        clickOn("#btnDone");

        // Delete Edge
        clickOn("#nodeabcd12345_nodeabcd123452Icon");
        clickOn("#btnDelete");
        clickOn("#btnYes");

        // Delete nodes
        clickOn("#nodeabcd12345Icon");
        clickOn("#btnDelete");
        clickOn("#btnYes");

        clickOn("#nodeabcd123452Icon");
        clickOn("#btnDelete");
        clickOn("#btnYes");

        clickOn("#btnBack");
        clickOn("#btnBack");
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

    @Test
    void testMapGraphicalInput() {
        clickOn("#btnGuest");
        clickOn("#btnDirections");

        // Select start node
        clickOn("#bPARK01801Icon");
        clickOn("#btnStart");

        // Select end node
        clickOn("#bPARK00101Icon");
        clickOn("#btnEnd");

        clickOn("#btnFindPath");

        verifyThat(".edge", Node::isVisible);

        clickOn("#btnBack");
        clickOn("#btnBack");

    }

    @Test
    void testMapMovement() {
        clickOn("#btnGuest");
        clickOn("#btnDirections");
        moveTo("#map");
        scroll(25, VerticalDirection.UP);
        scroll(5, VerticalDirection.DOWN);
        press(MouseButton.PRIMARY);
        drag(100, 0, MouseButton.PRIMARY);
        release(MouseButton.PRIMARY);
        clickOn("#btnBack");
        clickOn("#btnBack");
    }

    @Test
    void testMapPathDisplay() {
        clickOn("#btnGuest");
        clickOn("#btnDirections");

        // Select start and end locations
        doubleClickOn("Information Locations");
        clickOn("75 Lobby Information Desk");
        clickOn("#btnStart");
        doubleClickOn("Information Locations");
        doubleClickOn("Entrances");
        clickOn("75 Francis Lobby Entrance");
        clickOn("#btnEnd");
        doubleClickOn("Entrances");

        clickOn("#btnFindPath");

        // Check that an edge is drawn
        verifyThat(".edge", Node::isVisible);

        //Check that the estimated time box is drawn
        verifyThat("#estimatedTimeDialog", Node::isVisible);
        verifyThat("15 sec", Node::isVisible);

        clickOn("#btnBack");
        clickOn("#btnBack");
    }

    @Test
    void testMapBack() {
        clickOn("#btnGuest");
        clickOn("#btnDirections");
        verifyThat("Directions", Node::isVisible);
        clickOn("#btnBack");
        clickOn("#btnBack");
    }

    @ParameterizedTest
    @MethodSource("textProvider")
    void testBackButtons(String button, String title) {
        clickOn("#btnGuest");
        clickOn("Service Requests");
        verifyThat("Service Request Directory", Node::isVisible);
        clickOn(button);
        verifyThat(title, Node::isVisible);
        clickOn("Cancel");
        verifyThat("Service Request Directory", Node::isVisible);
        clickOn("#btnBack");
        clickOn("#btnBack");
    }

    @ParameterizedTest
    @MethodSource("textProvider")
    void testSubmitForms(String button, String title) {
        clickOn("#btnGuest");
        clickOn("Service Requests");
        verifyThat("Service Request Directory", Node::isVisible);
        clickOn(button);
        verifyThat(title, Node::isVisible);
        clickOn("Submit");
        verifyThat("Form Successfully Submitted!", Node::isVisible);
        clickOn("Return to Main Screen");
        verifyThat("Service Requests", Node::isVisible);
        clickOn("#btnBack");
        verifyThat("#btnGuest", Node::isVisible);
    }

    private static Stream<Arguments> textProvider() {
        return Stream.of(
                Arguments.of("Sanitation Services", "Sanitation Services Request Form"),
                Arguments.of("Floral Delivery", "Floral Delivery Request Form"),
                Arguments.of("Medicine Delivery", "Medicine Delivery Request Form"),
                Arguments.of("Security Services", "Security Services Request Form"),
                Arguments.of("Internal Transportation", "Internal Transportation Request Form"),
                Arguments.of("External Patient Transport", "External Transportation Request Form"),
                Arguments.of("Religious Service", "Religious Request Form"),
                Arguments.of("Food Delivery", "Food Delivery Request Form"),
                Arguments.of("Laundry", "Laundry Services Request Form"),
                Arguments.of("Social Worker", "Social Worker Request Form"),
                Arguments.of("Case Manager", "Case Manager Request Form")
        );
    }
}
