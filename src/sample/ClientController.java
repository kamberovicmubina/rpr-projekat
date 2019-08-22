package sample;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientController implements Initializable {
    private TextField nameField;
    private DatePicker dateOfBirthPicker;
    private TextField addressField;
    private TextField phoneField;
    private TextField eMailField;
    private Button saveButton;
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

       /* nameField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String o, String n) {
                setStyleClass(nameField, n);
            }
        });

        addressField.textProperty().addListener((observableValue, o, n) -> setStyleClass(addressField, n));

        eMailField.textProperty().addListener((observableValue, o, n) -> {
            if (eMailValid(n)) {
                eMailField.getStyleClass().removeAll("fieldIncorrect");
                eMailField.getStyleClass().add("fieldCorrect");
            } else {
                eMailField.getStyleClass().removeAll("fieldCorrect");
                eMailField.getStyleClass().add("fieldIncorrect");
            }
        });

        phoneField.textProperty().addListener((observableValue, o, n) -> {
            if (phoneFieldValid(n)) {
                phoneField.getStyleClass().removeAll("fieldIncorrect");
                phoneField.getStyleClass().add("fieldCorrect");
            } else {
                phoneField.getStyleClass().removeAll("fieldCorrect");
                phoneField.getStyleClass().add("fieldIncorrect");
            }
        });

        saveButton.setOnAction(actionEvent -> {
            if (allValid()) {
                Client client = companyModel.getClickedClient();
                client.setName(nameField.getText());
                client.setDateOfBirth(dateOfBirthPicker.getValue());
                client.setAddress(addressField.getText());
                client.setPhoneNumber(phoneField.getText());
                client.setEMail(eMailField.getText());
                // set contract list
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText("Client information successfully changed!");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Error");
                alert.setContentText("Incorrect information. Try again!");
                alert.showAndWait();
            }
            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.close();
        });*/
    }

    /*private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private boolean eMailValid (String email) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(email);
        return matcher.find();
    }

    private boolean textFieldValid (String field) {
        for (char c : field.toCharArray()) {
            if (Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    private boolean phoneFieldValid (String field) {
        for (char c : field.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    private boolean allValid () {
        return eMailValid(eMailField.getText()) && textFieldValid(nameField.getText()) && textFieldValid(addressField.getText())
                && phoneFieldValid(phoneField.getText());
    }

    private void setStyleClass (TextField field, String n) {
        if (textFieldValid(n)) {
            field.getStyleClass().removeAll("fieldIncorrect");
            field.getStyleClass().add("fieldCorrect");
        } else {
            field.getStyleClass().removeAll("fieldCorrect");
            field.getStyleClass().add("fieldIncorrect");
        }
    }*/
}
