package edu.wpi.teamB;

import static org.testfx.api.FxAssert.verifyThat;

import edu.wpi.teamB.database.DatabaseHandler;
import edu.wpi.teamB.entities.map.data.Edge;
import edu.wpi.teamB.util.CSVHandler;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationExtension;

import java.sql.SQLException;
import java.util.List;

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

        DatabaseHandler db = DatabaseHandler.getDatabaseHandler("main.db");
        List<edu.wpi.teamB.entities.map.data.Node> nodes = CSVHandler.loadCSVNodes("/edu/wpi/teamB/csvFiles/bwBnodes.csv");
        List<Edge> edges = CSVHandler.loadCSVEdges("/edu/wpi/teamB/csvFiles/bwBedges.csv");

        try {
            db.loadNodesEdges(nodes, edges);
        } catch (SQLException e) {
            e.printStackTrace();
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
}
