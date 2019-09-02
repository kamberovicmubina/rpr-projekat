package sample.controllers;

import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import sample.classes.Client;
import sample.DatabaseDAO;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientNumberController implements Initializable {
    public ListView<Client> nameListView;
    private DatabaseDAO dao;
    ResourceBundle bundle;
    ObservableList<Client> clientsName;
    public TextField numberTextField;

    public ClientNumberController(DatabaseDAO dao, ObservableList<Client> clientsInPeriodOfTime) {
        this.dao = dao;
        clientsName = clientsInPeriodOfTime;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bundle = resourceBundle;
        nameListView.setItems(clientsName);
        numberTextField.setText(String.valueOf(clientsName.size()));
    }
}
