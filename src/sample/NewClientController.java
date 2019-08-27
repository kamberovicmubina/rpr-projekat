package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;


import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class NewClientController implements Initializable {
    public TextField nameField;
    public TextField addressField;
    public TextField phoneField;
    public TextField eMailField;
    public DatePicker dateField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameField.textProperty().addListener((observableValue, o, n) -> {
            if (FieldsValidation.validName(n)) {
                nameField.getStyleClass().removeAll("fieldIncorrect");
                nameField.getStyleClass().add("fieldCorrect");
                // imeValidno = true;
            } else {
                nameField.getStyleClass().removeAll("fieldCorrect");
                nameField.getStyleClass().add("fieldIncorrect");
                //imeValidno = false;
            }
        });

        addressField.textProperty().addListener((observableValue, o, n) -> {
            if (FieldsValidation.validAddress(n)) {
                addressField.getStyleClass().removeAll("fieldIncorrect");
                addressField.getStyleClass().add("fieldCorrect");
                // imeValidno = true;
            } else {
                addressField.getStyleClass().removeAll("fieldCorrect");
                addressField.getStyleClass().add("fieldIncorrect");
                //imeValidno = false;
            }
        });

        phoneField.textProperty().addListener((observableValue, o, n) -> {
            if (FieldsValidation.validPhone(n)) {
                phoneField.getStyleClass().removeAll("fieldIncorrect");
                phoneField.getStyleClass().add("fieldCorrect");
                // imeValidno = true;
            } else {
                phoneField.getStyleClass().removeAll("fieldCorrect");
                phoneField.getStyleClass().add("fieldIncorrect");
                //imeValidno = false;
            }
        });

        eMailField.textProperty().addListener((observableValue, o, n) -> {
            if (FieldsValidation.validEMail(n)) {
                eMailField.getStyleClass().removeAll("fieldIncorrect");
                eMailField.getStyleClass().add("fieldCorrect");
                // imeValidno = true;
            } else {
                eMailField.getStyleClass().removeAll("fieldCorrect");
                eMailField.getStyleClass().add("fieldIncorrect");
                //imeValidno = false;
            }
        });

        dateField.valueProperty().addListener((observableValue, o, n) -> {
            if (FieldsValidation.validDate(n)) {
                dateField.getStyleClass().removeAll("fieldIncorrect");
                dateField.getStyleClass().add("fieldCorrect");
            } else {
                dateField.getStyleClass().removeAll("fieldCorrect");
                dateField.getStyleClass().add("fieldIncorrect");
                //imeValidno = false;
            }
        });


    }
}
