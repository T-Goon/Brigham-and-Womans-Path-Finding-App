package edu.wpi.cs3733.D21.teamB.views.map;

import com.dlsc.gmapsfx.GoogleMapView;
import com.dlsc.gmapsfx.MapComponentInitializedListener;
import com.dlsc.gmapsfx.javascript.object.*;
import com.dlsc.gmapsfx.service.directions.*;
import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.D21.teamB.App;
import edu.wpi.cs3733.D21.teamB.views.BasePageController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class DirectionsMenuController extends BasePageController implements Initializable, MapComponentInitializedListener, DirectionsServiceCallback {

    @FXML
    private StackPane mapHolder;

    @FXML
    private GoogleMapView mapView;

    private GoogleMap map;

    DirectionsService directionsService;
    DirectionsPane directionsPane;
    DirectionsRenderer directionsRenderer;

    @Override
    public void mapInitialized() {
        MapOptions mapOptions = new MapOptions();

        mapOptions.center(new LatLong(42.2743, -71.8081))
                .mapType(MapTypeIdEnum.ROADMAP)
                .panControl(true)
                .streetViewControl(false)
                .zoom(12)
                .overviewMapControl(false);

        map = mapView.createMap(mapOptions);
        directionsService = new DirectionsService();
        directionsPane = mapView.getDirec();

        map.showDirectionsPane();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mapView = new GoogleMapView("en", "AIzaSyD4MPvha5ZUWDmOuuvxPSMW0NH1h3bZUXs");
        VBox dirBox = (VBox) mapHolder.getChildren().get(0);
        mapHolder.getChildren().remove(0);
        mapHolder.getChildren().add(mapView);
        mapHolder.getChildren().add(dirBox);


        mapView.addMapInitializedListener(this);

    }

    private void getRoute(){
        DirectionsRequest request = new DirectionsRequest("Boston", "Worcester", TravelModes.DRIVING, true);
        directionsRenderer = new DirectionsRenderer(true, mapView.getMap(), directionsPane);
        directionsService.getRoute(request, this, directionsRenderer);
    }

    public void handleButtonAction(ActionEvent actionEvent) {
        JFXButton b = (JFXButton) actionEvent.getSource();

        switch (b.getId()){
            case "btnEmergency":
                getRoute();
                break;
            case "btnExit":
                Platform.exit();
                break;
        }
    }

    @Override
    public void directionsReceived(DirectionsResult results, DirectionStatus status) {

        System.out.println("Direction time");

    }
}
