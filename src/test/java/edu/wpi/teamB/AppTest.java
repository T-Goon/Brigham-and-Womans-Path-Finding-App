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
        clickOn("#btnStaff");
        verifyThat("Login Page", Node::isVisible);
        clickOn("#username");
        write("admin");
        press(KeyCode.TAB);
        write("password");
        press(KeyCode.ENTER);
        verifyThat("Staff Directory", Node::isVisible);
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
        clickOn("#btnStaff");
        verifyThat("Login Page", Node::isVisible);
        clickOn("#username");
        write("admin");
        press(KeyCode.TAB);
        write("password");
        press(KeyCode.ENTER);
        verifyThat("Staff Directory", Node::isVisible);
        clickOn("Service Requests");
        verifyThat("Service Request Directory", Node::isVisible);
        clickOn(button);
        verifyThat(title, Node::isVisible);

        switch (button) {
            case "Sanitation Services":
                clickOn("#loc");
                write("ACONF00102");
                clickOn("#comboTypeService");
                clickOn("Wet");
                clickOn("#comboSizeService");
                clickOn("Large");
                clickOn("#description");
                write("There's blood everywhere");
                clickOn("#safetyHazard");
                clickOn("#biologicalSubstance");
                break;
            case "Floral Delivery":
                clickOn("#patientName");
                write("Alex Bolduc");
                clickOn("#roomNumber");
                write("ACONF00102");
                clickOn("#deliveryDate");
                write("4/2/2021");
                clickOn("#startTime");
                write("4:20 PM");
                clickOn("#endTime");
                write("4:22 PM");
                clickOn("#message");
                write("I still love you, please take me back");
                clickOn("#roses");
                break;
            case "Medicine Delivery":
                clickOn("#name");
                write("Suela Miloshi");
                clickOn("#roomNum");
                write("ACONF00102");
                clickOn("#medName");
                write("500 mg of black tar heroin");
                clickOn("#reason");
                write("She's a heroin addict");
                break;
            case "Security Services":
                clickOn("#assignedTo");
                write("Tim Goon");
                clickOn("#loc");
                write("ACONF00102");
                clickOn("#comboUrgency");
                clickOn("10");
                clickOn("#description");
                write("Timmy Goon, we need you to go save the day again!");
                break;
            case "Internal Transportation":
                clickOn("#name");
                write("Smera Gora");
                clickOn("#roomNum");
                write("ACONF00102");
                clickOn("#comboTranspType");
                clickOn("Wheelchair");
                clickOn("#description");
                write("AHHHHHHHHHHHHHHHH");
                break;
            case "External Transportation":
                clickOn("#name");
                write("Jonathan Metcalf");
                clickOn("#roomNum");
                write("ACONF00102");
                clickOn("#destination");
                write("123 Hospital St");
                clickOn("#comboTranspType");
                clickOn("Helicopter");
                clickOn("#description");
                write("He just wants a helicopter ride");
                clickOn("#allergies");
                write("none");
                clickOn("#infectious");
                break;
            case "Religious Service":
                clickOn("#name");
                write("Molly Sunray");
                clickOn("#roomNum");
                write("ACONF00102");
                clickOn("#date");
                write("4/20/2021");
                clickOn("#startTime");
                write("4:20 PM");
                clickOn("#endTime");
                write("4:22 PM");
                clickOn("#faith");
                write("Church of Satan");
                clickOn("#description");
                write("Her eyes are glowing red");
                clickOn("#infectious");
                break;
            case "Food Delivery":
                clickOn("#name");
                write("John Doe");
                clickOn("#roomNum");
                write("ACONF00102");
                clickOn("#mealChoice");
                write("Bread and water");
                clickOn("#arrivalTime");
                write("6:30 PM");
                clickOn("#description");
                write("He just likes bread, that's all");
                break;
            case "Laundry":
                clickOn("#loc");
                write("ACONF00102");
                clickOn("#comboTypeService");
                clickOn("Regular Cycle");
                clickOn("#comboSizeService");
                clickOn("Large");
                clickOn("#description");
                write("Enormous and everywhere");
                clickOn("#roomOccupied");
                break;
            case "Social Worker":
                clickOn("#patientName");
                write("Mike Bedard");
                clickOn("#roomNumber");
                write("ACONF00102");
                clickOn("#timeForArrival");
                write("6:30 PM");
                clickOn("#messageForSocialWorker");
                write("MIKE BEDARD!!!!!!!!");
                break;
            case "Case Manager":
                clickOn("#patientName");
                write("Professor X");
                clickOn("#roomNumber");
                write("ACONF00102");
                clickOn("#timeForArrival");
                write("6:30 PM");
                clickOn("#messageForCaseManager");
                write("AHHHHHHHHHHHHHHHHHHHHHHHHHH");
                break;
        }

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
                Arguments.of("External Transportation", "External Transportation Request Form"),
                Arguments.of("Religious Service", "Religious Request Form"),
                Arguments.of("Food Delivery", "Food Delivery Request Form"),
                Arguments.of("Laundry", "Laundry Services Request Form"),
                Arguments.of("Social Worker", "Social Worker Request Form"),
                Arguments.of("Case Manager", "Case Manager Request Form")
        );
    }
}
