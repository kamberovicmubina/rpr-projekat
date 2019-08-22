package sample;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;


import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ClientController implements Initializable {
    public TextField nameField;
    public DatePicker dateOfBirthPicker;
    public TextField addressField;
    public TextField phoneField;
    public TextField eMailField;
    private SimpleStringProperty nameProperty;
    private SimpleStringProperty addressProperty;
    private SimpleStringProperty phoneProperty;
    private SimpleStringProperty eMailProperty;
    private ObjectProperty<LocalDate> dateOfBirthProperty;
    private Company companyModel;

    public ClientController (Company cm) {
        companyModel = cm;
        nameProperty = new SimpleStringProperty(companyModel.getClickedClient().getName());
        addressProperty = new SimpleStringProperty(companyModel.getClickedClient().getAddress());
        phoneProperty = new SimpleStringProperty(companyModel.getClickedClient().getPhoneNumber());
        eMailProperty = new SimpleStringProperty(companyModel.getClickedClient().getEMail());
        dateOfBirthProperty = new SimpleObjectProperty<LocalDate>(companyModel.getClickedClient().getDateOfBirth());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameField.textProperty().bindBidirectional(nameProperty);
        addressField.textProperty().bindBidirectional(addressProperty);
        phoneField.textProperty().bindBidirectional(phoneProperty);
        eMailField.textProperty().bindBidirectional(eMailProperty);
        dateOfBirthPicker.valueProperty().bindBidirectional(dateOfBirthProperty);

        nameField.setText(companyModel.getClickedClient().getName());
        addressField.setText(companyModel.getClickedClient().getAddress());
        phoneField.setText(companyModel.getClickedClient().getPhoneNumber());
        eMailField.setText(companyModel.getClickedClient().getEMail());
        dateOfBirthPicker.setValue(companyModel.getClickedClient().getDateOfBirth());
    }
}
