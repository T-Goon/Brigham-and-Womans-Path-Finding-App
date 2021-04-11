package edu.wpi.teamB.database;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVHandler {
    /**
     * Parses the CSV file for edges given in ./csvfiles
     * and adds the data into the list of edges.
     *
     * @param path path to edges csv
     * @return the list of edges
     */
    public static List<Edge> parseCSVEdges(Path path) {

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
     * Parses the CSV file for nodes given in ./csvfiles
     * and adds the data into the list of nodes.
     *
     * @param path path to nodes csv
     * @return the list of nodes
     */
    public static List<Node> parseCSVNodes(Path path) {

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
}
