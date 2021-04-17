package edu.wpi.teamB.views.mapEditor;

import com.jfoenix.controls.JFXButton;
import edu.wpi.teamB.App;
import edu.wpi.teamB.util.GraphicalEditorNodeData;
import edu.wpi.teamB.util.GraphicalNodeDelData;
import edu.wpi.teamB.util.GraphicalNodeEditData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import lombok.Getter;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class NodePopupWindowController implements Initializable {

    @FXML
    @Getter
    private VBox root;

    @FXML
    private VBox mainMenu;

    @FXML
    private Text nodeName;

    @FXML
    private JFXButton btnEditNode;

    @FXML
    private JFXButton btnAddEdge;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXButton btnCancel;

    private GraphicalEditorNodeData data;

    private VBox areYouSureMenu;

    private VBox nodeEditMenu;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        data = (GraphicalEditorNodeData) App.getPrimaryStage().getUserData();

        nodeName.setText(data.getLongName());
    }

    @FXML
    private void handleButtonAction(ActionEvent event){

        JFXButton btn = (JFXButton) event.getSource();

        switch (btn.getId()){
            case "btnEditNode":
                root.getChildren().remove(mainMenu);

                App.getPrimaryStage().setUserData(new GraphicalNodeEditData(data, this));

                try{
                    nodeEditMenu = FXMLLoader.load(Objects.requireNonNull(
                            getClass().getClassLoader().getResource("edu/wpi/teamB/views/mapEditor/editNodePopup.fxml")));
                } catch (IOException e){
                    e.printStackTrace();
                }

                root.getChildren().add(nodeEditMenu);
                break;
            case "btnAddEdge":
                break;
            case "btnDelete":
                root.getChildren().remove(mainMenu);

                App.getPrimaryStage().setUserData(new GraphicalNodeDelData(
                        data.getNodeID(),
                        data.getX(),
                        data.getY(),
                        data.getFloor(),
                        data.getBuilding(),
                        data.getNodeType(),
                        data.getLongName(),
                        data.getShortName(),
                        null,
                        data.getNodeHolder(),
                        data.getPfmc(),
                        this));

                try{
                    areYouSureMenu = FXMLLoader.load(Objects.requireNonNull(
                            getClass().getClassLoader().getResource("edu/wpi/teamB/views/mapEditor/delNodeAreYouSure.fxml")));
                } catch (IOException e){
                    e.printStackTrace();
                }

                root.getChildren().add(areYouSureMenu);

                break;
            case "btnCancel":
                data.getNodeHolder().getChildren().remove(root);
                break;
        }
    }

    void areYouSureToMain(){
        root.getChildren().remove(areYouSureMenu);
        areYouSureMenu = null;

        root.getChildren().add(mainMenu);
    }

    void editToMain(){
        root.getChildren().remove(nodeEditMenu);
        nodeEditMenu = null;

        root.getChildren().add(mainMenu);
    }
}
