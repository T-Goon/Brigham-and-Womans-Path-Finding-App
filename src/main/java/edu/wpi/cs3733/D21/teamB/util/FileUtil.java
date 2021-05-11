package edu.wpi.cs3733.D21.teamB.util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;

public class FileUtil {

    public static boolean copy(InputStream source , String destination) {
        boolean succeess = true;

        System.out.println("Copying ->" + source + "\n\tto ->" + destination);

        try {
            Files.copy(source, Paths.get(destination), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
//            logger.log(Level.WARNING, "", ex);
            ex.printStackTrace();
            succeess = false;
        }

        return succeess;

    }

    public static boolean copy(InputStream source , String destination, String dir) {
        boolean succeess = true;

        File directory = new File(dir);
        if (! directory.exists()){
            directory.mkdir();
            // If you require it to make the entire directory path including parents,
            // use directory.mkdirs(); here instead.
        }

        System.out.println("Copying ->" + source + "\n\tto ->" + destination);

        try {
            Files.copy(source, Paths.get(destination), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
//            logger.log(Level.WARNING, "", ex);
            ex.printStackTrace();
            succeess = false;
        }
        return succeess;

    }
}
