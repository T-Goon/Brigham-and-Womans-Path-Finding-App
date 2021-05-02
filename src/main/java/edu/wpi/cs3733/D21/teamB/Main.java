package edu.wpi.cs3733.D21.teamB;

import org.opencv.core.Core;

public class Main {
    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }
    public static void main(String[] args) {
        App.launch(App.class, args);
    }
}
