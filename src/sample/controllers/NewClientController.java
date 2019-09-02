package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sample.classes.Client;
import sample.DatabaseDAO;
import sample.FieldsValidation;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class NewClientController implements Initializable {
    public TextField nameField;
    public TextField addressField;
    public TextField phoneField;
    public TextField eMailField;
    public DatePicker dateField;
    public Button cancelButton;
    private boolean nameValid = false;
    private boolean addressValid = false;
    private boolean phoneValid = false;
    private boolean eMailValid = false;
    private boolean dateValid = false;
    private ResourceBundle bundle;
    private DatabaseDAO dao;

    public NewClientController (DatabaseDAO databaseDAO) {
        dao = databaseDAO;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bundle = resourceBundle;

        nameField.textProperty().addListener((observableValue, o, n) -> {
            if (FieldsValidation.validName(n)) {
                setCorrect(nameField);
                nameValid = true;
            } else {
                setIncorrect(nameField);
                nameValid = false;
            }
        });

        addressField.textProperty().addListener((observableValue, o, n) -> {
            if (FieldsValidation.validAddress(n)) {
                setCorrect(addressField);
                addressValid = true;
            } else {
                setIncorrect(addressField);
                addressValid = false;
            }
        });

        phoneField.textProperty().addListener((observableValue, o, n) -> {
            if (FieldsValidation.validPhone(n)) {
                setCorrect(phoneField);
                phoneValid = true;
            } else {
                setIncorrect(phoneField);
                phoneValid = false;
            }
        });

        eMailField.textProperty().addListener((observableValue, o, n) -> {
            if (FieldsValidation.validEMail(n)) {
                setCorrect(eMailField);
                eMailValid = true;
            } else {
                setIncorrect(eMailField);
                eMailValid = false;
            }
        });

        dateField.valueProperty().addListener((observableValue, o, n) -> {
            if (FieldsValidation.validDate(n)) {
                dateField.getStyleClass().removeAll("fieldIncorrect");
                dateField.getStyleClass().add("fieldCorrect");
                dateValid = true;
            } else {
                dateField.getStyleClass().removeAll("fieldCorrect");
                dateField.getStyleClass().add("fieldIncorrect");
                dateValid = false;
            }
        });
    }

    public boolean dataValid () {
        return nameValid && dateValid && addressValid && phoneValid && eMailValid;
    }

    public void saveClicked (ActionEvent actionEvent) {
        if (!dataValid()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(bundle.getString("error"));
            alert.setHeaderText(null);
            alert.setContentText(bundle.getString("incorrectInfo"));
            alert.showAndWait();
        }
        else {
            String name = nameField.getText();
            LocalDate date = dateField.getValue();
            String address = addressField.getText();
            String phone = phoneField.getText();
            String eMail = eMailField.getText();
            Client newClient = new Client(name, date, address, phone, eMail, null);
            newClient.setId(dao.getNextAvailableClientId());
            dao.executeInsertClient(newClient);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(bundle.getString("success"));
            alert.setHeaderText(null);
            alert.setContentText(bundle.getString("clientAdded"));
            alert.show();
        }
    }

    public void cancelClicked (ActionEvent actionEvent) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    private void setCorrect (TextField field) {
        field.getStyleClass().removeAll("fieldIncorrect");
        field.getStyleClass().add("fieldCorrect");
    }

    private static void setIncorrect (TextField field) {
        field.getStyleClass().removeAll("fieldCorrect");
        field.getStyleClass().add("fieldIncorrect");
    }

}
