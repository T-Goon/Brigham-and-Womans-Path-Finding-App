package edu.wpi.cs3733.D21.teamB;

import org.bytedeco.opencv.opencv_core.Mat;
import org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.opencv.core.CvType;
import org.opencv.imgproc.Imgproc;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FaceTest {

    @Test
    public void test(){
//        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat mat = new Mat(3, 3);
        mat.diag(3);
        System.out.println("mat = " + mat);
        assertEquals(1, 1);
    }
}
