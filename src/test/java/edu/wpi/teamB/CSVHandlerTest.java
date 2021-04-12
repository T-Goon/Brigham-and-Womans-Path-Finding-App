package edu.wpi.teamB;

import edu.wpi.teamB.database.DatabaseHandler;
import edu.wpi.teamB.entities.Edge;
import edu.wpi.teamB.entities.Node;
import edu.wpi.teamB.util.CSVHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class CSVHandlerTest {

    private static String resourcesPathString;
    private static DatabaseHandler db;

    @BeforeAll
    static void initDB() {
        db = DatabaseHandler.getDatabaseHandler("test.db");
        resourcesPathString = new File("").getAbsolutePath() + "/src/test/resources/edu/wpi/teamB/database/load/";
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
        Path path = Paths.get("src/test/resources/edu/wpi/teamB/database/save");

        String expected = "nodeID,xcoord,ycoord,floor,building,nodeType,longName,shortName\n" +
                "1,1,1,2,Parking,PARK,Left Parking Lot Spot 10,LLot10\n";

        DatabaseHandler.getDatabaseHandler("test.db").addNode(new Node(
                "1",1,1,2,"Parking","PARK",
                "Left Parking Lot Spot 10","LLot10"));

        String actualString = CSVHandler.saveCSVNodes(path, true);
        assertTrue(Files.exists(Paths.get(path.toAbsolutePath() + "/MapBNodes.csv")));
        assertEquals(expected, actualString);

        String actualFile = new String(Files.readAllBytes(Paths.get(path.toAbsolutePath() + "/MapBNodes.csv")));
        assertEquals(expected, actualFile);
    }

    @Test
    public void testSaveCSVEdgesFilepath() throws SQLException, IOException {
        Path path = Paths.get("src/test/resources/edu/wpi/teamB/database/save");

        String expected = "edgeID,startNode,endNode\n1,2,3\n";

        DatabaseHandler.getDatabaseHandler("test.db").addEdge(new Edge("1","2","3"));

        String actualString = CSVHandler.saveCSVEdges(path, true);
        assertTrue(Files.exists(Paths.get(path.toAbsolutePath() + "/MapBEdges.csv")));
        assertEquals(expected, actualString);

        String actualFile = new String(Files.readAllBytes(Paths.get(path.toAbsolutePath() + "/MapBEdges.csv")));
        assertEquals(expected, actualFile);
    }
}
