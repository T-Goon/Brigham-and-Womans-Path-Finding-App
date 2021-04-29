package edu.wpi.cs3733.D21.teamB;

import static org.testfx.api.FxAssert.verifyThat;

import edu.wpi.cs3733.D21.teamB.App;
import edu.wpi.cs3733.D21.teamB.database.DatabaseHandler;
import edu.wpi.cs3733.D21.teamB.entities.map.data.Edge;
import edu.wpi.cs3733.D21.teamB.util.CSVHandler;
import javafx.geometry.VerticalDirection;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationExtension;

import java.sql.SQLException;
import java.util.List;

@ExtendWith(ApplicationExtension.class)
public class MapTests extends FxRobot{

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

        DatabaseHandler db = DatabaseHandler.getHandler();
        List<edu.wpi.cs3733.D21.teamB.entities.map.data.Node> nodes = CSVHandler.loadCSVNodes("/edu/wpi/cs3733/D21/teamB/csvFiles/bwBnodes.csv");
        List<Edge> edges = CSVHandler.loadCSVEdges("/edu/wpi/cs3733/D21/teamB/csvFiles/bwBedges.csv");

        try {
            db.loadNodesEdges(nodes, edges);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testGraphicalMapEditor() {
        // Get to edit map menu
        clickOn("#btnLogin");
        verifyThat("Login Page", Node::isVisible);
        clickOn("#username");
        write("admin");
        press(KeyCode.TAB);
        write("admin");
        press(KeyCode.ENTER);
        verifyThat("Admin Directory", Node::isVisible);
        clickOn("#btnDirections");
        clickOn("#btnEditMap");

        // Open add node popup
        moveTo(400, 400);
        doubleClickOn(MouseButton.PRIMARY);

        // Fill in node info
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
        verifyThat("#bBATH00101Icon", Node::isVisible);
        clickOn("#bBATH00101Icon");
        clickOn("#btnEditNode");
        clickOn("#building");
        write("123");
        clickOn("#btnUpdate");

        // Mode a node with a drag
        moveTo("#bBATH00101Icon");
        press(MouseButton.PRIMARY);
        drag(100, 0, MouseButton.PRIMARY);
        release(MouseButton.PRIMARY);

        // Set start node for edge
        clickOn("#bBATH00101Icon");
        clickOn("#btnAddEdge");

        // Set end node for edge
        clickOn("#bBATH00201Icon");
        clickOn("#btnYes");

        // Delete Edge
        clickOn("#bBATH00101_bBATH00201Icon");
        clickOn("#btnDelete");
        clickOn("#btnYes");

        // Delete nodes
        clickOn("#bBATH00101Icon");
        clickOn("#btnDelete");
        clickOn("#btnYes");

        clickOn("#bBATH00201Icon");
        clickOn("#btnDelete");
        clickOn("#btnYes");

        clickOn("#btnBack");
    }

    @Test
    void testMapGraphicalInput() {
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

    }

    @Test
    void testMapMovement() {
        clickOn("#btnDirections");
        moveTo("#map");
        scroll(25, VerticalDirection.UP);
        scroll(5, VerticalDirection.DOWN);
        press(MouseButton.PRIMARY);
        drag(100, 0, MouseButton.PRIMARY);
        release(MouseButton.PRIMARY);
        clickOn("#btnBack");
    }

    @Test
    void testMapPathDisplay() {
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
    }

    @Test
    void testMapBack() {
        verifyThat("#btnDirections", Node::isVisible);
        clickOn("#btnDirections");
        clickOn("#btnBack");
    }

}
