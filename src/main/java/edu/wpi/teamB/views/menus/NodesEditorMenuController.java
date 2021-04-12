package edu.wpi.teamB.views.menus;

import com.jfoenix.controls.JFXButton;
import edu.wpi.teamB.database.DatabaseHandler;
import edu.wpi.teamB.entities.Edge;
import edu.wpi.teamB.entities.Node;
import edu.wpi.teamB.util.NodeWrapper;
import edu.wpi.teamB.util.SceneSwitcher;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.ResourceBundle;

public class NodesEditorMenuController implements Initializable {

    @FXML
    private JFXButton btnBack;

    @FXML
    private TableView tblNodes;
    @FXML
    private TableColumn<String, JFXButton> editBtnCol;
    @FXML
    private TableColumn<String, Label> idCol;
    @FXML
    private TableColumn<String, Label> nameCol;
    @FXML
    private TableColumn<String, Label> typeCol;
    @FXML
    private TableColumn<String, Label> edgesCol;
    @FXML
    private TableColumn<String, JFXButton> delCol;

    @SneakyThrows
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Map<String, Node> nodes = null;

        try {
            nodes = DatabaseHandler.getDatabaseHandler("main.db").getNodes();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        ObservableList<TableColumn<String, Label>> cols = tblNodes.getColumns();
        for(TableColumn<String, Label> c : cols){
            c.getId();
            switch (c.getId()){
                case "editBtnCol":
                    c.setCellValueFactory(new PropertyValueFactory<>("editBtn"));
                    break;
                case "idCol":
                    c.setCellValueFactory(new PropertyValueFactory<>("id"));
                    break;
                case "nameCol":
                    c.setCellValueFactory(new PropertyValueFactory<>("name"));
                    break;
                case "typeCol":
                    c.setCellValueFactory(new PropertyValueFactory<>("type"));
                    break;
                case "edgesCol":
                    c.setCellValueFactory(new PropertyValueFactory<>("edges"));
                    break;
                case "delCol":
                    c.setCellValueFactory(new PropertyValueFactory<>("delBtn"));
                    break;
            }
        }

        assert nodes != null;
        for(Node n : nodes.values()){
            tblNodes.getItems().add(new NodeWrapper(n));
        }
    }

    @FXML
    private void handleButtonAction(ActionEvent e) throws IOException {
        JFXButton btn = (JFXButton) e.getSource();

        switch (btn.getId()){
            case "btnBack":
                SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/menus/editorIntermediateMenu.fxml");
                break;
        }
    }
}
