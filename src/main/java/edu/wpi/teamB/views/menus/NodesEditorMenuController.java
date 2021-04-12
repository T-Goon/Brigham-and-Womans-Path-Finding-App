package edu.wpi.teamB.views.menus;

import com.jfoenix.controls.JFXButton;
import edu.wpi.teamB.database.DatabaseHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javax.swing.text.TableView;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.ResourceBundle;

public class NodesEditorMenuController implements Initializable {

    @FXML
    private JFXButton btnBack;

    @FXML
    private TableView tblNodes;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            HashMap<> nodes = DatabaseHandler.getDatabaseHandler("main.db").getNodeInformation();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
