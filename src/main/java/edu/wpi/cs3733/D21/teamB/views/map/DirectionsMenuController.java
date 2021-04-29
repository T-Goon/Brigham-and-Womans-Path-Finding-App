package edu.wpi.cs3733.D21.teamB.views.map;

import com.dlsc.gmapsfx.GoogleMapView;
import com.dlsc.gmapsfx.MapComponentInitializedListener;
import com.dlsc.gmapsfx.javascript.object.GoogleMap;
import com.dlsc.gmapsfx.javascript.object.LatLong;
import com.dlsc.gmapsfx.javascript.object.MapOptions;
import com.dlsc.gmapsfx.javascript.object.MapTypeIdEnum;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class DirectionsMenuController implements Initializable, MapComponentInitializedListener {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private GoogleMapView mapView;

    private GoogleMap map;

    @Override
    public void mapInitialized() {
        MapOptions mapOptions = new MapOptions();

        mapOptions.center(new LatLong(42.2743, -71.8081))
                .mapType(MapTypeIdEnum.HYBRID)
                .panControl(true)
                .rotateControl(false)
                .streetViewControl(false)
                .scaleControl(true)
                .zoom(12);

        map = mapView.createMap(mapOptions);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mapView = new GoogleMapView("en", "AIzaSyD4MPvha5ZUWDmOuuvxPSMW0NH1h3bZUXs");
        anchorPane.getChildren().add(mapView);

        AnchorPane.setTopAnchor(mapView, 0.0);
        AnchorPane.setBottomAnchor(mapView, 0.0);
        AnchorPane.setLeftAnchor(mapView, 0.0);
        AnchorPane.setRightAnchor(mapView, 0.0);


        mapView.addMapInitializedListener(this);
    }

    public void handleButtonAction(ActionEvent actionEvent) {
    }
}
