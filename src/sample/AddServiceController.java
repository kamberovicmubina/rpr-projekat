package sample;

import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AddServiceController implements Initializable {
    public TextField serviceField;
    public Button addBtn;
   // private Company companyModel;
    private DatabaseDAO dao;
    private ResourceBundle bundle;

    public AddServiceController (DatabaseDAO databaseDAO) {
        dao = databaseDAO;
    }

    public void onAdd () {
        //dao.executeGetCompanyQuery(1).getServices().add(serviceField.getText());
        dao.executeInsertService(serviceField.getText());
        Alert newAlert = new Alert(Alert.AlertType.CONFIRMATION, bundle.getString("serviceAdded"));
        newAlert.setTitle(bundle.getString("success"));
        newAlert.setHeaderText(null);
        newAlert.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bundle = resourceBundle;
    }
}
