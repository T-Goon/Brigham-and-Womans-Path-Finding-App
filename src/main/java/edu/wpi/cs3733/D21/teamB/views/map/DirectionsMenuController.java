package edu.wpi.cs3733.D21.teamB.views.map;

import com.dlsc.gmapsfx.GoogleMapView;
import com.dlsc.gmapsfx.MapComponentInitializedListener;
import com.dlsc.gmapsfx.javascript.object.*;
import com.dlsc.gmapsfx.service.directions.*;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import edu.wpi.cs3733.D21.teamB.util.SceneSwitcher;
import edu.wpi.cs3733.D21.teamB.views.BasePageController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

public class DirectionsMenuController extends BasePageController implements Initializable, MapComponentInitializedListener, DirectionsServiceCallback {

    @FXML
    private StackPane mapHolder;

    @FXML
    private JFXTextField txtStartLocation;

    @FXML
    private JFXComboBox<String> comboEndLocation;

    @FXML
    private JFXButton btnSubmit;

    @FXML
    private JFXButton btnBack;

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
                .rotateControl(true)
                .zoom(12)
                .overviewMapControl(false);

        map = mapView.createMap(mapOptions);
        directionsService = new DirectionsService();
        directionsPane = mapView.getDirec();

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        comboEndLocation.getItems().add("BWH Visitor Parking");
        comboEndLocation.getItems().add("15-51 New Whitney St Parking");


        mapView = new GoogleMapView("en", "AIzaSyD4MPvha5ZUWDmOuuvxPSMW0NH1h3bZUXs");
        mapHolder.getChildren().add(0, mapView);


        mapView.addMapInitializedListener(this);


        RequiredFieldValidator startLocationValidator = new RequiredFieldValidator();
        startLocationValidator.setMessage("Please enter a starting location");
        txtStartLocation.getValidators().add(startLocationValidator);

        RequiredFieldValidator endLocationValidator = new RequiredFieldValidator();
        startLocationValidator.setMessage("Please select a ending location");
        comboEndLocation.getValidators().add(endLocationValidator);


        txtStartLocation.setOnKeyPressed(event -> {
            if(event.getCode().equals(KeyCode.ENTER)&&canSubmit()){
                getRoute();
            }
        });

        comboEndLocation.setOnKeyPressed(event -> {
            if(event.getCode().equals(KeyCode.ENTER)&&canSubmit()){
                getRoute();
            }
        });

    }

    private void getRoute() {
        if(directionsRenderer != null) {
            directionsRenderer.clearDirections();
            directionsRenderer.getJSObject().eval("var summaryPanel = document.getElementById('directions'); summaryPanel.innerHTML=\"\"");
        }

        map.showDirectionsPane();
        DirectionsRequest request = new DirectionsRequest(txtStartLocation.getText(), comboEndLocation.getSelectionModel().getSelectedItem(), TravelModes.DRIVING, true);
        directionsRenderer = new DirectionsRenderer(true, mapView.getMap(), directionsPane);
        directionsService.getRoute(request, this, directionsRenderer);
    }

    public void handleButtonAction(ActionEvent actionEvent) {
        super.handleButtonAction(actionEvent);
        JFXButton b = (JFXButton) actionEvent.getSource();

        switch (b.getId()) {
            case "btnSubmit":
                getRoute();
                break;
            case "btnEmergency":
                SceneSwitcher.switchScene(getClass(), "/edu/wpi/cs3733/D21/teamB/views/map/directionsMenu.fxml", "/edu/wpi/cs3733/D21/teamB/views/requestForms/emergencyForm.fxml");
                break;
        }
    }

    @Override
    public void directionsReceived(DirectionsResult results, DirectionStatus status) {
    }

    public void validateButton(ActionEvent actionEvent) {
        btnSubmit.setDisable(!canSubmit());
    }

    private boolean canSubmit(){
        return !txtStartLocation.getText().isEmpty() && !comboEndLocation.getSelectionModel().getSelectedItem().isEmpty();
    }
}
