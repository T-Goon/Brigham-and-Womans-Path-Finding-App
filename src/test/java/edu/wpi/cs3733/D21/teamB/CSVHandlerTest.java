package edu.wpi.cs3733.D21.teamB;


import edu.wpi.cs3733.D21.teamB.database.DatabaseHandler;
import edu.wpi.cs3733.D21.teamB.entities.map.data.Edge;
import edu.wpi.cs3733.D21.teamB.entities.map.data.Node;
import edu.wpi.cs3733.D21.teamB.pathfinding.Graph;
import edu.wpi.cs3733.D21.teamB.util.CSVHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class CSVHandlerTest {

    private static Path resourcesPath;
    private static DatabaseHandler db;

    @BeforeAll
    static void initDB() {
        resourcesPath = Paths.get("src/test/resources/edu/wpi/cs3733/D21/teamB/database/save");
        db = DatabaseHandler.getHandler();
        Graph.setGraph(db);
    }

    @BeforeEach
    void resetDB() {
        try {
            db.loadNodesEdges(null, null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSaveCSVNodesFilepath() throws IOException {
        String expected = "nodeID,xcoord,ycoord,floor,building,nodeType,longName,shortName\n" +
                "1,1,1,2,Parking,PARK,Left Parking Lot Spot 10,LLot10\n";

        try {
            DatabaseHandler.getHandler().addNode(new Node(
                    "1", 1, 1, "2", "Parking", "PARK",
                    "Left Parking Lot Spot 10", "LLot10"));
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        String actualString = CSVHandler.saveCSVNodes(resourcesPath, true);
        assertTrue(Files.exists(Paths.get(resourcesPath + "/bwBnodes.csv")));
        assertEquals(expected, actualString);

        String actualFile = new String(Files.readAllBytes(Paths.get(resourcesPath + "/bwBnodes.csv")));
        assertEquals(expected, actualFile);
    }

    @Test
    public void testSaveCSVEdgesFilepath() throws IOException {
        String expected = "edgeID,startNode,endNode\n1,2,3\n";

        DatabaseHandler db = DatabaseHandler.getHandler();
        try {
            db.addNode(new Node("2", 1, 1, "1", "building", "type", "longName", "shortName (which is longer than long name)"));
            db.addNode(new Node("3", 1, 1, "1", "building", "type", "longName", "shortName (which is longer than long name)"));
            db.addEdge(new Edge("1", "2", "3"));
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        String actualString = CSVHandler.saveCSVEdges(resourcesPath, true);
        assertTrue(Files.exists(Paths.get(resourcesPath + "/bwBedges.csv")));
        assertEquals(expected, actualString);

        String actualFile = new String(Files.readAllBytes(Paths.get(resourcesPath + "/bwBedges.csv")));
        assertEquals(expected, actualFile);
    }
}
