package edu.wpi.cs3733.D21.teamB.views.face;

import ai.djl.modality.cv.BufferedImageFactory;
import com.jfoenix.controls.JFXButton;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
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
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FD_Controller implements Initializable {
    // FXML buttons
    @FXML
    private Button cameraButton;
    // the FXML area for showing the current frame
    @FXML
    private ImageView originalFrame,
            oldPicture,
            detectedPicture;
    // checkboxes for enabling/disabling a classifier
    @FXML
    private CheckBox haarClassifier;
    @FXML
    private CheckBox lbpClassifier;
    @FXML
    private JFXButton btnTakePicture;

    @FXML
    private Text detectText;

    // a timer for acquiring the video stream
    private ScheduledExecutorService timer;
    // the OpenCV object that performs the video capture
    private VideoCapture capture;
    // a flag to change the button behavior
    private boolean cameraActive;

    // face cascade classifier
    private CascadeClassifier faceCascade;
    private int absoluteFaceSize;

    private Mat grayFrame;

    private Mat picture;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Init();
    }

    /**
     * Init the controller, at start time
     */
    protected void Init() {
        this.capture = new VideoCapture();
        this.faceCascade = new CascadeClassifier();
        this.absoluteFaceSize = 0;

        // set a fixed width for the frame
        originalFrame.setFitWidth(600);
        // preserve image ratio
        originalFrame.setPreserveRatio(true);
    }

    /**
     * The action triggered by pushing the button on the GUI
     */
    @FXML
    protected void startCamera() {
        if (!this.cameraActive) {
            // disable setting checkboxes
            this.haarClassifier.setDisable(true);
            this.lbpClassifier.setDisable(true);

            // start the video capture
            this.capture.open(0);

            // is the video stream available?
            if (this.capture.isOpened()) {
                this.cameraActive = true;

                // grab a frame every 33 ms (30 frames/sec)
                Runnable frameGrabber = new Runnable() {

                    @Override
                    public void run() {
                        // effectively grab and process a single frame
                        Mat frame = grabFrame();
                        // convert and show the frame
                        Image imageToShow = Utils.mat2Image(frame);
                        updateImageView(originalFrame, imageToShow);
                    }
                };

                this.timer = Executors.newSingleThreadScheduledExecutor();
                this.timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);

                // update the button content
                this.cameraButton.setText("Stop Camera");
            } else {
                // log the error
                System.err.println("Failed to open the camera connection...");
            }
        } else {
            // the camera is not active at this point
            this.cameraActive = false;
            // update again the button content
            this.cameraButton.setText("Start Camera");
            // enable classifiers checkboxes
            this.haarClassifier.setDisable(false);
            this.lbpClassifier.setDisable(false);

            // stop the timer
            this.stopAcquisition();
        }
    }

    @FXML
    private void takePicture() {
        String img_name = new File("").getAbsolutePath() +
                "/src/main/resources/edu/wpi/cs3733/D21/teamB/faces/opencv_frame_0.png";
        img_name = img_name.replaceAll("\\\\", "/");
        System.out.println(img_name);
        Imgcodecs.imwrite(img_name, picture);
    }

    /**
     * Get a frame from the opened video stream (if any)
     *
     * @return the {@link Image} to show
     */
    private Mat grabFrame() {
        Mat frame = new Mat();

        // check if the capture is open
        if (this.capture.isOpened()) {
            try {
                // read the current frame
                this.capture.read(frame);

                // if the frame is not empty, process it
                if (!frame.empty()) {
                    // face detection
                    this.detectAndDisplay(frame);
                }

            } catch (Exception e) {
                // log the (full) error
                System.err.println("Exception during the image elaboration: " + e);
            }
        }

        return frame;
    }

    /**
     * Method for face detection and tracking
     *
     * @param frame it looks for faces in this frame
     */
    private void detectAndDisplay(Mat frame) {
        MatOfRect faces = new MatOfRect();
        grayFrame = new Mat();

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
        this.faceCascade.detectMultiScale(grayFrame, faces, 1.1, 2, 0 | Objdetect.CASCADE_SCALE_IMAGE,
                new Size(this.absoluteFaceSize, this.absoluteFaceSize), new Size());

        // each rectangle in faces is a face: draw them!
        Rect[] facesArray = faces.toArray();
        for (int i = 0; i < facesArray.length; i++)
            Imgproc.rectangle(frame, facesArray[i].tl(), facesArray[i].br(), new Scalar(0, 255, 0), 3);

        // Store the cropped face
        if (!faces.empty())
            picture = frame.submat(facesArray[0]);

        // Compare the faces
        String img_name = new File("").getAbsolutePath() +
                "/src/main/resources/edu/wpi/cs3733/D21/teamB/faces/opencv_frame_0.png";
        img_name = img_name.replaceAll("\\\\", "/");
        Mat loadedImg = Imgcodecs.imread(img_name, Imgcodecs.IMREAD_UNCHANGED);

        EmbeddingModel facenet = EmbeddingModel.getModel();

        detect(facenet, loadedImg, "AKAZE");
    }

    private static BufferedImage MatConvert(Mat image) throws IOException {
        MatOfByte mat = new MatOfByte();
        Imgcodecs.imencode(".jpg",image,mat);
        byte array[] = mat.toArray();
        return ImageIO.read(new ByteArrayInputStream(array));
    }

    private void detect(EmbeddingModel facenet, Mat loadedImg, String which) {

        Image imageToShow = Utils.mat2Image(loadedImg);
        updateImageView(oldPicture, imageToShow);

        Image imageToShow2 = Utils.mat2Image(picture);
        updateImageView(detectedPicture, imageToShow2);

        double[] storedImageEmbedding = null;
        double[] newImageEmbedding = null;
        try {
            storedImageEmbedding = facenet.embedding((new BufferedImageFactory()).fromImage(MatConvert(loadedImg)));
            newImageEmbedding = facenet.embedding((new BufferedImageFactory()).fromImage(MatConvert(picture)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        double dist = facenet.cosineDistance(storedImageEmbedding,newImageEmbedding);

        System.out.println(dist);

        if(dist >= .60){
            System.out.println("match");
        }
    }

    /**
     * The action triggered by selecting the Haar Classifier checkbox. It loads
     * the trained set to be used for frontal face detection.
     */
    @FXML
    protected void haarSelected(Event event) {
        // check whether the lpb checkbox is selected and deselect it
        if (this.lbpClassifier.isSelected())
            this.lbpClassifier.setSelected(false);

        this.checkboxSelection(new File("haarcascade_frontalface_alt.xml").getAbsolutePath());
    }

    /**
     * The action triggered by selecting the LBP Classifier checkbox. It loads
     * the trained set to be used for frontal face detection.
     */
    @FXML
    protected void lbpSelected(Event event) {
        // check whether the haar checkbox is selected and deselect it
        if (this.haarClassifier.isSelected())
            this.haarClassifier.setSelected(false);

        this.checkboxSelection(new File("lbpcascade_frontalface.xml").getAbsolutePath());
    }

    /**
     * Method for loading a classifier trained set from disk
     *
     * @param classifierPath the path on disk where a classifier trained set is located
     */
    private void checkboxSelection(String classifierPath) {
        // load the classifier(s)
        this.faceCascade.load(classifierPath);

        // now the video capture can start
        this.cameraButton.setDisable(false);
    }

    /**
     * Stop the acquisition from the camera and release all the resources
     */
    private void stopAcquisition() {
        if (this.timer != null && !this.timer.isShutdown()) {
            try {
                // stop the timer
                this.timer.shutdown();
                this.timer.awaitTermination(33, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                // log any exception
                System.err.println("Exception in stopping the frame capture, trying to release the camera now... " + e);
            }
        }

        if (this.capture.isOpened()) {
            // release the camera
            this.capture.release();
        }
    }

    /**
     * Update the {@link ImageView} in the JavaFX main thread
     *
     * @param view  the {@link ImageView} to update
     * @param image the {@link Image} to show
     */
    private void updateImageView(ImageView view, Image image) {
        Utils.onFXThread(view.imageProperty(), image);
    }

    /**
     * On application close, stop the acquisition from the camera
     */
    protected void setClosed() {
        this.stopAcquisition();
    }
}
