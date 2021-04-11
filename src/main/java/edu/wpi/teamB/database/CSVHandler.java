package edu.wpi.teamB.database;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVHandler {

    /**
     * Parses the CSV file for nodes given by the user
     * and adds the data into the list of nodes.
     *
     * @param path path to nodes csv
     * @return the list of nodes
     */
    public static List<Node> loadCSVNodes(Path path) {

        List<Node> list = new ArrayList<>();

        // Read in the file
        String fileContent;
        try {
            fileContent = new String(Files.readAllBytes(path));
        } catch (IOException e) {
            e.printStackTrace();
            return list;
        }

        // Split by row, then by column, and pass in everything
        String[] rows = fileContent.split("\n");
        try {
            rows = Arrays.copyOfRange(rows, 1, rows.length);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("No rows properly extracted from CSV");
        }
        for (String row : rows) {
            row = row.replace("\r", "");
            String[] values = row.split(",");
            list.add(new Node(values[0], Integer.parseInt(values[1]), Integer.parseInt(values[2]), Integer.parseInt(values[3]), values[4], values[5], values[6], values[7]));
        }
        return list;
    }

    /**
     * Parses the CSV file for edges given by the user
     * and adds the data into the list of edges.
     *
     * @param path path to edges csv
     * @return the list of edges
     */
    public static List<Edge> loadCSVEdges(Path path) {

        List<Edge> list = new ArrayList<>();

        // Read in the file
        String fileContent;
        try {
            fileContent = new String(Files.readAllBytes(path));
        } catch (IOException e) {
            e.printStackTrace();
            return list;
        }

        // Split by row, then by column, and pass in everything
        String[] rows = Arrays.copyOfRange(fileContent.split("\n"), 1, fileContent.split("\n").length);
        for (String row : rows) {
            row = row.replace("\r", "");
            String[] values = row.split(",");
            list.add(new Edge(values[0], values[1], values[2]));
        }
        return list;
    }

    /**
     * Saves the database information for nodes into a csv file
     * and saves it to a location specified by the user
     *
     * @param path directory where to save nodes csv
     */
    public static void saveCSVNodes(Path path) throws SQLException {
        List<Node> nodes = DatabaseHandler.getDatabaseHandler("main.db").getNodeInformation();

        // make string representation of csv file
        StringBuilder sb = new StringBuilder();
        sb.append("nodeID,xcoord,ycoord,floor,building,nodeType,longName,shortName\n");
        for (Node n : nodes) {
            sb.append(n.getNodeID()).append(",")
                    .append(n.getXCoord()).append(",")
                    .append(n.getYCoord()).append(",")
                    .append(n.getFloor()).append(",")
                    .append(n.getBuilding()).append(",")
                    .append(n.getNodeType()).append(",")
                    .append(n.getLongName()).append(",")
                    .append(n.getShortName()).append("\n");
        }

        // Write to the file
        try {
            FileOutputStream out = new FileOutputStream(path.toAbsolutePath() + "MapBNodes.csv");
            out.write(sb.toString().getBytes(StandardCharsets.UTF_8));
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the database information for nodes into a csv file
     * and saves it to a location specified by the user
     *
     * @param path path where to save edges csv
     */
    public static void saveCSVEdges(Path path) throws SQLException {
        List<Edge> edges = DatabaseHandler.getDatabaseHandler("main.db").getEdgeInformation();

        // make string representation of csv file
        StringBuilder sb = new StringBuilder();
        sb.append("edgeID,startNode,endNode\n");
        for (Edge e : edges) {
            sb.append(e.getEdgeID()).append(",")
                    .append(e.getStartNodeName()).append(",")
                    .append(e.getEndNodeName()).append("\n");
        }

        // Write to the file
        try {
            FileOutputStream out = new FileOutputStream(path.toAbsolutePath() + "MapBEdges.csv");
            out.write(sb.toString().getBytes(StandardCharsets.UTF_8));
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
