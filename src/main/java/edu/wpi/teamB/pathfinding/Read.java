package edu.wpi.teamB.pathfinding;

import edu.wpi.teamB.entities.Edge;
import edu.wpi.teamB.entities.Node;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Read {

  // tested by databases
  public static HashMap<String, Node> parseCSVNodes(String path) {

    // List<Node> list = new ArrayList<>();
    HashMap<String, Node> nodesMaps = new HashMap<>();

    // Read in the file
    String fileContent;
    try {
      fileContent =
          new String(
              Files.readAllBytes(
                  Paths.get( path)));
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }

    // Split by row, then by column, and pass in everything
    String[] rows = Arrays.copyOfRange(fileContent.split("\n"), 1, fileContent.split("\n").length);
    for (String row : rows) {
      row = row.replace("\r", "");
      String[] values = row.split(",");

      nodesMaps.put(
          values[0],
          new Node(
              values[0],
              Integer.parseInt(values[1]),
              Integer.parseInt(values[2]),
              Integer.parseInt(values[3]),
              values[4],
              values[5],
              values[6],
              values[7]));
    }
    return nodesMaps;
  }

  // tested by databases
  public static List<Edge> parseCSVEdges(String path) {

    List<Edge> list = new ArrayList<>();

    // Read in the file
    String fileContent;
    try {
      fileContent =
          new String(
              Files.readAllBytes(
                  Paths.get(path)));
    } catch (IOException e) {
      e.printStackTrace();
      return null;
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
}
