package sample;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class ClientController implements Initializable {
    public TextField nameField;
    public DatePicker dateOfBirthPicker;
    public TextField addressField;
    public TextField phoneField;
    public TextField eMailField;
    public Button saveButton;
    public Button cancelButton;
    public Button deleteButton;
    private SimpleStringProperty nameProperty;
    private SimpleStringProperty addressProperty;
    private SimpleStringProperty phoneProperty;
    private SimpleStringProperty eMailProperty;
    private ObjectProperty<LocalDate> dateOfBirthProperty;
    private Company companyModel;
    private boolean nameValid = true;
    private boolean addressValid = true;
    private boolean phoneValid = true;
    private boolean eMailValid = true;
    private boolean dateValid = true;

    public ClientController (Company cm) {
        companyModel = cm;
        nameProperty = new SimpleStringProperty(companyModel.getClickedClient().getName());
        addressProperty = new SimpleStringProperty(companyModel.getClickedClient().getAddress());
        phoneProperty = new SimpleStringProperty(companyModel.getClickedClient().getPhoneNumber());
        eMailProperty = new SimpleStringProperty(companyModel.getClickedClient().getEMail());
        dateOfBirthProperty = new SimpleObjectProperty<>(companyModel.getClickedClient().getDateOfBirth());
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

        // validation of fields in case they are changed
        nameField.textProperty().addListener((observableValue, o, n) -> {
            if (FieldsValidation.validName(n)) {
                nameField.getStyleClass().removeAll("fieldIncorrect");
                nameField.getStyleClass().add("fieldCorrect");
                nameValid = true;
            } else {
                nameField.getStyleClass().removeAll("fieldCorrect");
                nameField.getStyleClass().add("fieldIncorrect");
                nameValid = false;
            }
        });

        addressField.textProperty().addListener((observableValue, o, n) -> {
            if (FieldsValidation.validAddress(n)) {
                addressField.getStyleClass().removeAll("fieldIncorrect");
                addressField.getStyleClass().add("fieldCorrect");
                addressValid = true;
            } else {
                addressField.getStyleClass().removeAll("fieldCorrect");
                addressField.getStyleClass().add("fieldIncorrect");
                addressValid = false;
            }
        });

        phoneField.textProperty().addListener((observableValue, o, n) -> {
            if (FieldsValidation.validPhone(n)) {
                phoneField.getStyleClass().removeAll("fieldIncorrect");
                phoneField.getStyleClass().add("fieldCorrect");
                phoneValid = true;
            } else {
                phoneField.getStyleClass().removeAll("fieldCorrect");
                phoneField.getStyleClass().add("fieldIncorrect");
                phoneValid = false;
            }
        });

        eMailField.textProperty().addListener((observableValue, o, n) -> {
            if (FieldsValidation.validEMail(n)) {
                eMailField.getStyleClass().removeAll("fieldIncorrect");
                eMailField.getStyleClass().add("fieldCorrect");
                eMailValid = true;
            } else {
                eMailField.getStyleClass().removeAll("fieldCorrect");
                eMailField.getStyleClass().add("fieldIncorrect");
                eMailValid = false;
            }
        });

        dateOfBirthPicker.valueProperty().addListener((observableValue, o, n) -> {
            if (FieldsValidation.validDate(n)) {
                dateOfBirthPicker.getStyleClass().removeAll("fieldIncorrect");
                dateOfBirthPicker.getStyleClass().add("fieldCorrect");
                dateValid = true;
            } else {
                dateOfBirthPicker.getStyleClass().removeAll("fieldCorrect");
                dateOfBirthPicker.getStyleClass().add("fieldIncorrect");
                dateValid = false;
            }
        });

    }

    public boolean dataValid () {
        return nameValid && dateValid && addressValid && phoneValid && eMailValid;
    }

    public void saveClicked () {
        if (!dataValid()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error!");
            alert.setHeaderText(null);
            alert.setContentText("New client information is not valid. Try again!");
            alert.showAndWait();
        }
        else {
            String name = nameField.getText();
            LocalDate date = dateOfBirthPicker.getValue();
            String address = addressField.getText();
            String phone = phoneField.getText();
            String eMail = eMailField.getText();
            Client newClient = new Client(name, date, address, phone, eMail, null);
            companyModel.changeClient(companyModel.getClickedClient(), newClient);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success!");
            alert.setHeaderText(null);
            alert.setContentText("Client changed!");
            alert.showAndWait();
            cancelClicked();
        }
    }

    public void cancelClicked() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void deleteClicked () {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Are you sure you want to delete this client?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Warning!");
        alert.setHeaderText(null);
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == ButtonType.YES) {
            companyModel.removeClient();
            for (Client c : companyModel.getClients()) {
                System.out.println(c.toString());
            }
            Alert newAlert = new Alert(Alert.AlertType.CONFIRMATION, "Client deleted!");
            newAlert.setTitle("SUCCESS!");
            newAlert.setHeaderText(null);
            newAlert.show();
            cancelClicked();
        }

    }
}
