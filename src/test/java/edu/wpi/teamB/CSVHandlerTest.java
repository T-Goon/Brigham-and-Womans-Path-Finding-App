package edu.wpi.teamB;

import edu.wpi.teamB.database.DatabaseHandler;
import edu.wpi.teamB.entities.Edge;
import edu.wpi.teamB.entities.Node;
import edu.wpi.teamB.pathfinding.Graph;
import edu.wpi.teamB.util.CSVHandler;
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
        resourcesPath = Paths.get("src/test/resources/edu/wpi/teamB/database/save");
        db = DatabaseHandler.getDatabaseHandler("test.db");
//        Graph.setGraph(db);
    }

    @BeforeEach
    void resetDB() {
        try {
            db.loadDatabase(null, null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSaveCSVNodesFilepath() throws SQLException, IOException {
        String expected = "nodeID,xcoord,ycoord,floor,building,nodeType,longName,shortName\n" +
                "1,1,1,2,Parking,PARK,Left Parking Lot Spot 10,LLot10\n";

        DatabaseHandler.getDatabaseHandler("test.db").addNode(new Node(
                "1",1,1,"2","Parking","PARK",
                "Left Parking Lot Spot 10","LLot10"));

        String actualString = CSVHandler.saveCSVNodes(resourcesPath, true);
        assertTrue(Files.exists(Paths.get(resourcesPath + "/bwBnodes.csv")));
        assertEquals(expected, actualString);

        String actualFile = new String(Files.readAllBytes(Paths.get(resourcesPath + "/bwBnodes.csv")));
        assertEquals(expected, actualFile);
    }

    @Test
    public void testSaveCSVEdgesFilepath() throws SQLException, IOException {
        String expected = "edgeID,startNode,endNode\n1,2,3\n";

        DatabaseHandler.getDatabaseHandler("test.db").addEdge(new Edge("1","2","3"));

        String actualString = CSVHandler.saveCSVEdges(resourcesPath, true);
        assertTrue(Files.exists(Paths.get(resourcesPath + "/bwBedges.csv")));
        assertEquals(expected, actualString);

        String actualFile = new String(Files.readAllBytes(Paths.get(resourcesPath + "/bwBedges.csv")));
        assertEquals(expected, actualFile);
    }
}
