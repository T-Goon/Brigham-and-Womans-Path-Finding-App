package edu.wpi.teamB.views.menus;

import com.jfoenix.controls.JFXButton;
import edu.wpi.teamB.database.DatabaseHandler;
import edu.wpi.teamB.entities.Edge;
import edu.wpi.teamB.entities.Node;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Map<String, Node> nodes = null;
        Map<String, Edge> edges = null;

        try {
            nodes = DatabaseHandler.getDatabaseHandler("main.db").getNodes();
            edges = DatabaseHandler.getDatabaseHandler("main.db").getEdges();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        ObservableList<TableColumn<String, Label>> cols = tblNodes.getColumns();
        for(TableColumn<String, Label> c : cols){
            c.getId();
            switch (c.getId()){
                case "IDcol":
                    c.setCellValueFactory(new PropertyValueFactory<>("nodeID"));
                    break;
                case "Typecol":
                    c.setCellValueFactory(new PropertyValueFactory<>("nodeType"));
                    break;
                case "Namecol":
                    c.setCellValueFactory(new PropertyValueFactory<>("longName"));
                    break;
                case ""
            }
        }

        assert nodes != null;
        for(Node n : nodes.values()){

        }
    }
}
