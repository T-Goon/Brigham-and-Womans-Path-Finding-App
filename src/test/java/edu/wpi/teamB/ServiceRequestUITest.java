package edu.wpi.teamB;

import static org.testfx.api.FxAssert.verifyThat;

import javafx.fxml.FXML;
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

@ExtendWith(ApplicationExtension.class)
public class ServiceRequestUITest extends FxRobot {

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
                clickOn("Hallway 12 Floor 3");
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
                clickOn("#loc");
                clickOn("Hallway 12 Floor 3");
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
                clickOn("#loc");
                clickOn("Hallway 12 Floor 3");
                clickOn("#medName");
                write("500 mg of black tar heroin");
                clickOn("#reason");
                write("She's a heroin addict");
                break;
            case "Security Services":
                clickOn("#assignedTo");
                write("Tim Goon");
                clickOn("#loc");
                clickOn("Hallway 12 Floor 3");
                clickOn("#comboUrgency");
                clickOn("10");
                clickOn("#description");
                write("Timmy Goon, we need you to go save the day again!");
                break;
            case "Internal Transportation":
                clickOn("#name");
                write("Smera Gora");
                clickOn("#loc");
                clickOn("Hallway 12 Floor 3");
                clickOn("#comboTranspType");
                clickOn("Wheelchair");
                clickOn("#description");
                write("AHHHHHHHHHHHHHHHH");
                break;
            case "External Transportation":
                clickOn("#name");
                write("Jonathan Metcalf");
                clickOn("#loc");
                clickOn("Hallway 12 Floor 3");
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
                clickOn("#loc");
                clickOn("Hallway 12 Floor 3");
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
                clickOn("#loc");
                clickOn("Hallway 12 Floor 3");
                clickOn("#mealChoice");
                write("Bread and water");
                clickOn("#arrivalTime");
                write("6:30 PM");
                clickOn("#description");
                write("He just likes bread, that's all");
                break;
            case "Laundry":
                clickOn("#loc");
                clickOn("Hallway 12 Floor 3");
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
                clickOn("#loc");
                clickOn("Hallway 12 Floor 3");
                clickOn("#timeForArrival");
                write("6:30 PM");
                clickOn("#messageForSocialWorker");
                write("MIKE BEDARD!!!!!!!!");
                break;
            case "Case Manager":
                clickOn("#patientName");
                write("Professor X");
                clickOn("#loc");
                clickOn("Hallway 12 Floor 3");
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
