package edu.wpi.cs3733.D21.teamB.views.face;

import ai.djl.modality.cv.BufferedImageFactory;
import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.D21.teamB.views.login.LoginPageController;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import lombok.Getter;
import lombok.Setter;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import org.opencv.videoio.VideoCapture;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Camera {

    private final ImageView oldPicture;
    private final ImageView detectedPicture;

    // a timer for acquiring the video stream
    public static ScheduledExecutorService timer;
    // the OpenCV object that performs the video capture
    public static VideoCapture capture;
    // a flag to change the button behavior
    public static boolean cameraActive;

    // face cascade classifier
    private final CascadeClassifier faceCascade;
    private int absoluteFaceSize;

    private Mat picture;
    @Getter
    private Mat pictureTaken;
    private final boolean authenticating;

    private Thread authenticator;

    @Setter
    private LoginPageController loginPageController;

    public Camera(ImageView oldPicture, ImageView detectedPicture, JFXButton btnTakePicture, boolean authenticating) {
        this.oldPicture = oldPicture;
        this.detectedPicture = detectedPicture;
        this.authenticating = authenticating;

        capture = new VideoCapture();
        this.faceCascade = new CascadeClassifier();
        this.absoluteFaceSize = 0;

        // load the classifier
        this.faceCascade.load(new File("haarcascade_frontalface_alt.xml").getAbsolutePath());

        // set a fixed width for the frame
        detectedPicture.setFitWidth(600);
        // preserve image ratio
        detectedPicture.setPreserveRatio(true);

        if (btnTakePicture != null) {
            btnTakePicture.setOnMouseClicked((e) -> {
                takePicture();
                oldPicture.fireEvent(new KeyEvent(KeyEvent.KEY_PRESSED,
                        "/", "", KeyCode.SLASH, false, false, false, false));
            });
        }
    }

    /**
     * Toggle the camera
     */
    public void toggleCamera() {
        if (!cameraActive) {
            // start the video capture
            capture.open(0);

            // is the video stream available?
            if (capture.isOpened()) {
                cameraActive = true;

                // grab a frame every 33 ms (30 frames/sec)
                // effectively grab and process a single frame
                Runnable frameGrabber = this::grabFrame;

                timer = Executors.newSingleThreadScheduledExecutor();
                timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);

            } else {
                // log the error
                System.err.println("Failed to open the camera connection...");
            }

        } else {
            // the camera is not active at this point
            cameraActive = false;

            // stop the timer
            stopAcquisition();

        }
    }

    /**
     * Take a picture
     */
    private void takePicture() {
        pictureTaken = picture;
    }

    /**
     * has a picture been taken
     * @return boolean if a picture is taken
     */
    public boolean isPictureTaken(){
        return pictureTaken != null;
    }

    private void grabFrame() {
        Mat frame = new Mat();

        // check if the capture is open
        if (capture.isOpened()) {
            try {
                // read the current frame
                capture.read(frame);

                // if the frame is not empty, process it
                if (!frame.empty()) {
                    // face detection
                    this.detectAndDisplay(frame);
                    Image imageToShow = Utils.mat2Image(frame);
                    updateImageView(detectedPicture, imageToShow);
                }

            } catch (Exception e) {
                // log the (full) error
                System.err.println("Exception during the image elaboration: " + e);
            }
        }

    }

    /**
     * Method for face detection and tracking
     *
     * @param frame it looks for faces in this frame
     */
    private void detectAndDisplay(Mat frame) {
        MatOfRect faces = new MatOfRect();
        Mat grayFrame = new Mat();

        // convert the frame in gray scale
        Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);
        // equalize the frame histogram to improve the result
        Imgproc.equalizeHist(grayFrame, grayFrame);

        // compute minimum face size (20% of the frame height, in our case)
        if (this.absoluteFaceSize == 0) {
            int height = grayFrame.rows();
            if (Math.round(height * 0.2f) > 0) {
                this.absoluteFaceSize = Math.round(height * 0.2f);
            }
        }

        assert !grayFrame.empty();

        // detect faces
        this.faceCascade.detectMultiScale(grayFrame, faces, 1.1, 2, Objdetect.CASCADE_SCALE_IMAGE,
                new Size(this.absoluteFaceSize, this.absoluteFaceSize), new Size());

        // each rectangle in faces is a face: draw them!
        Rect[] facesArray = faces.toArray();
        for (Rect rect : facesArray) Imgproc.rectangle(frame, rect.tl(), rect.br(), new Scalar(0, 255, 0), 3);

        if(!faces.empty()){
            picture = frame.submat(facesArray[0]);
        } else {
            picture = null;
        }

        // display a taken picture
        if(pictureTaken != null && !authenticating){
            Image imageToShow = Utils.mat2Image(pictureTaken);
            updateImageView(oldPicture, imageToShow);
        }

        // match faces
        if(authenticating && (authenticator == null || !authenticator.isAlive())) {
            authenticator = new Thread(() -> {
                // Compare the faces
                if(picture != null){
                    detect();
                }

                authenticator.interrupt();
            });

            authenticator.start();
        }
    }

    /**
     * Compare 2 faces to look for a match
     */
    private void detect() {
        EmbeddingModel facenet = EmbeddingModel.getModel();

        double[] newImageEmbedding = null;
        if(picture != null) {
            try {
                newImageEmbedding = facenet.embedding((new BufferedImageFactory()).fromImage(MatConvert(picture)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String userName = null;
        try {
            userName = facenet.userFromEmbedding(newImageEmbedding);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(userName != null && cameraActive){
            loginPageController.setUserName(userName);
        }
    }

    public static BufferedImage MatConvert(Mat image) throws IOException {
        MatOfByte mat = new MatOfByte();
        Imgcodecs.imencode(".jpg",image,mat);
        byte[] array = mat.toArray();
        return ImageIO.read(new ByteArrayInputStream(array));
    }

    /**
     * Stop the acquisition from the camera and release all the resources
     */
    public static void stopAcquisition() {
        if (Camera.timer != null && !Camera.timer.isShutdown()) {
            Camera.timer.shutdownNow();
//            try {
//                // stop the timer
//                Camera.timer.shutdownNow();
////                Camera.timer.awaitTermination(33, TimeUnit.MILLISECONDS);
//            } catch (InterruptedException e) {
//                // log any exception
//                System.err.println("Exception in stopping the frame capture, trying to release the camera now... " + e);
//            }
        }

        if (capture != null) {
            if (capture.isOpened()) {
                // release the camera
                capture.release();
            }
        }

        if (Camera.cameraActive) {
            Camera.cameraActive = false;
        }
    }

    private void updateImageView(ImageView view, Image image) {
        Utils.onFXThread(view.imageProperty(), image);
    }
}
