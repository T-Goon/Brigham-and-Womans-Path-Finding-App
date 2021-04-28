package edu.wpi.cs3733.D21.teamB.views.menus;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import edu.wpi.cs3733.D21.teamB.App;
import edu.wpi.cs3733.D21.teamB.database.DatabaseHandler;
import edu.wpi.cs3733.D21.teamB.entities.User;
import edu.wpi.cs3733.D21.teamB.util.SceneSwitcher;
import edu.wpi.cs3733.D21.teamB.util.UserWrapper;
import edu.wpi.cs3733.D21.teamB.views.BasePageController;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@SuppressWarnings("unchecked") // Added so Java doesn't get mad at the raw use of TableView that is necessary
public class UserInformationDatabaseController extends BasePageController implements Initializable {
    @FXML
    private JFXButton btnBack;

    @FXML
    private JFXButton btnExit;

    @FXML
    private JFXButton btnEmergency;

    @FXML
    private JFXButton btnAdd;

    @FXML
    private TableView tblStaff;

    @FXML
    private StackPane stackContainer;

    @FXML
    private TableColumn<String, JFXButton> editCol;

    @FXML
    private TableColumn<String, JFXButton> usernameCol;

    @FXML
    private TableColumn<String, JFXButton> firstCol;

    @FXML
    private TableColumn<String, JFXButton> lastCol;

    @FXML
    private TableColumn<String, JFXButton> employeeCol;

    @FXML
    private TableColumn<String, JFXButton> delCol;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<User> users = DatabaseHandler.getHandler().getUsers();
        ObservableList<TableColumn<String, Label>> cols = tblStaff.getColumns();
        for (TableColumn<String, Label> c : cols) {
            switch (c.getId()) {
                case "editCol":
                    c.setCellValueFactory(new PropertyValueFactory<>("btnEdit"));
                    break;
                case "usernameCol":
                    c.setCellValueFactory(new PropertyValueFactory<>("username"));
                    break;
                case "firstCol":
                    c.setCellValueFactory(new PropertyValueFactory<>("firstName"));
                    break;
                case "lastCol":
                    c.setCellValueFactory(new PropertyValueFactory<>("lastName"));
                    break;
                case "employeeCol":
                    c.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
                    break;
                case "delCol":
                    c.setCellValueFactory(new PropertyValueFactory<>("btnDel"));
                    break;
            }
        }

        if (users != null) {
            for (User u : users) {
                try {
                    tblStaff.getItems().add(new UserWrapper(u , tblStaff));
                } catch (IOException err) {
                    err.printStackTrace();
                }
            }
        }
    }

    @FXML
    public void handleButtonAction(ActionEvent e) {
        JFXButton btn = (JFXButton) e.getSource();
        super.handleButtonAction(e);
        switch (btn.getId()) {
            case "btnEmergency":
                SceneSwitcher.switchScene(getClass(),"/edu/wpi/cs3733/D21/teamB/views/menus/userInformationDatabase.fxml", "/edu/wpi/cs3733/D21/teamB/views/requestForms/emergencyForm.fxml");
                break;
            case "btnAdd":
                Stage stage = App.getPrimaryStage();
                stage.setUserData(new User("", "", "", User.AuthenticationLevel.PATIENT, new ArrayList<>()));
                SceneSwitcher.addingUser = true;
                SceneSwitcher.switchScene(getClass(),"/edu/wpi/cs3733/D21/teamB/views/menus/userInformationDatabase.fxml", "/edu/wpi/cs3733/D21/teamB/views/menus/editUserMenu.fxml");
                break;
            case "btnHelp":
                loadHelpDialog();
                break;
        }
    }

    private void loadHelpDialog(){
        JFXDialogLayout helpLayout = new JFXDialogLayout();

        Text helpText = new Text("To view an employee's full information, click on the edit button. Editing passwords is achieved by deleting a user and recreating them with a new password. Deleting a user will delete all requests assigned to that user.");
        helpText.setFont(new Font("MS Reference Sans Serif", 14));

        Label headerLabel = new Label("Help");
        headerLabel.setFont(new Font("MS Reference Sans Serif", 18));

        helpLayout.setHeading(headerLabel);
        helpLayout.setBody(helpText);
        JFXDialog helpWindow = new JFXDialog(stackContainer, helpLayout, JFXDialog.DialogTransition.CENTER);

        JFXButton button = new JFXButton("Close");
        button.setOnAction(event -> helpWindow.close());
        helpLayout.setActions(button);

        helpWindow.show();

    }
}
