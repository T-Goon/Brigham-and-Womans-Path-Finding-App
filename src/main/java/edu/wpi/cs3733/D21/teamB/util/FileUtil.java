package edu.wpi.cs3733.D21.teamB.util;

import java.io.InputStream;
import java.nio.file.*;
import java.io.*;
import java.nio.file.Files;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class FileUtil {

    public static void copy(InputStream source, String destination) {
        try {
            System.out.println("copy to " + destination);
            Files.copy(source, Paths.get(destination), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        System.out.println("DELETING: " + allContents);
        directoryToBeDeleted.delete();
    }
}
