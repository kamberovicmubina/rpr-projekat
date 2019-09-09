package sample.controllers;

import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import sample.DatabaseDAO;

import java.net.URL;
import java.util.ResourceBundle;

public class AddServiceController implements Initializable {
    public TextField serviceField;
    private DatabaseDAO dao;
    private ResourceBundle bundle;

    public AddServiceController (DatabaseDAO databaseDAO) {
        dao = databaseDAO;
    }

    public void onAdd () {
        if (serviceField.getText() != null && !serviceField.getText().equals("")) {
            dao.executeInsertService(serviceField.getText());
            Alert newAlert = new Alert(Alert.AlertType.CONFIRMATION, bundle.getString("serviceAdded"));
            newAlert.setTitle(bundle.getString("success"));
            newAlert.setHeaderText(null);
            newAlert.show();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bundle = resourceBundle;
    }
}
