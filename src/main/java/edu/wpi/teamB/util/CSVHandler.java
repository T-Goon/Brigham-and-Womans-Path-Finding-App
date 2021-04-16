package edu.wpi.teamB.util;

import edu.wpi.teamB.database.DatabaseHandler;
import edu.wpi.teamB.entities.map.Edge;
import edu.wpi.teamB.entities.map.Node;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class CSVHandler {

    /**
     * Parses the CSV file for nodes given by the user
     * and adds the data into the list of nodes.
     *
     * @param path path to nodes csv
     * @return the list of nodes
     */
    public static List<Node> loadCSVNodes(String path) {

        List<Node> list = new ArrayList<>();
        InputStream s = CSVHandler.class.getResourceAsStream(path);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(s)));

        List<String> lines = bufferedReader.lines().collect(Collectors.toList());
        lines.remove(0);

        // Read in the file
        for (String line : lines) {
            line = line.replace("\r", "");
            String[] values = line.split(",");
            list.add(new Node(values[0], Integer.parseInt(values[1]), Integer.parseInt(values[2]), values[3], values[4], values[5], values[6], values[7]));
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
    public static List<Edge> loadCSVEdges(String path) {

        List<Edge> list = new ArrayList<>();
        InputStream s = CSVHandler.class.getResourceAsStream(path);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(s)));

        List<String> lines = bufferedReader.lines().collect(Collectors.toList());
        lines.remove(0);

        // Read in the file
        for (String line : lines) {
            line = line.replace("\r", "");
            String[] values = line.split(",");
            list.add(new Edge(values[0].trim(), values[1].trim(), values[2].trim()));
        }

        return list;
    }


    /**
     * Parses the CSV file for nodes given by the user
     * and adds the data into the list of nodes.
     *
     * @param path path to nodes csv
     * @return the list of nodes
     */
    public static List<Node> loadCSVNodesFromExternalPath(Path path) {

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
            list.add(new Node(values[0].trim(), Integer.parseInt(values[1].trim()), Integer.parseInt(values[2].trim()), values[3].trim(), values[4].trim(), values[5].trim(), values[6].trim(), values[7].trim()));
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
    public static List<Edge> loadCSVEdgesFromExternalPath(Path path) {

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
        try {
            rows = Arrays.copyOfRange(rows, 1, rows.length);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("No rows properly extracted from CSV");
        }

        for (String row : rows) {
            row = row.replace("\r", "");
            String[] values = row.split(",");
            list.add(new Edge(values[0], values[1], values[2]));
        }
        return list;
    }


    /**
     * Saves the database information for nodes into a csv file
     * and saves it to a directory specified by the user
     *
     * @param path    directory where to save nodes csv
     * @param testing whether it's being tested or not
     * @return the string representation of the csv file
     */
    public static String saveCSVNodes(Path path, boolean testing) {
        // Check if testing or running the application
        String dbName = "main.db";
        if (testing) dbName = "test.db";

        Map<String, Node> nodes = DatabaseHandler.getDatabaseHandler(dbName).getNodes();

        // make string representation of csv file
        StringBuilder sb = new StringBuilder();
        sb.append("nodeID,xcoord,ycoord,floor,building,nodeType,longName,shortName\n");
        for (Node n : nodes.values()) {
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
            FileOutputStream out = new FileOutputStream(path.toAbsolutePath() + "/bwBnodes.csv");
            out.write(sb.toString().getBytes(StandardCharsets.UTF_8));
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    /**
     * Saves the database information for edges into a csv file
     * and saves it to a directory specified by the user
     *
     * @param path    directory where to save edges csv
     * @param testing whether it's being tested or not
     * @return the string representation of the csv file
     */
    public static String saveCSVEdges(Path path, boolean testing) {
        // Check if testing or running the application
        String dbName = "main.db";
        if (testing) dbName = "test.db";

        Map<String, Edge> edges = DatabaseHandler.getDatabaseHandler(dbName).getEdges();

        // make string representation of csv file
        StringBuilder sb = new StringBuilder();
        sb.append("edgeID,startNode,endNode\n");
        for (Edge e : edges.values()) {
            sb.append(e.getEdgeID()).append(",")
                    .append(e.getStartNodeID()).append(",")
                    .append(e.getEndNodeID()).append("\n");
        }

        // Write to the file
        try {
            FileOutputStream out = new FileOutputStream(path.toAbsolutePath() + "/bwBedges.csv");
            out.write(sb.toString().getBytes(StandardCharsets.UTF_8));
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }
}
