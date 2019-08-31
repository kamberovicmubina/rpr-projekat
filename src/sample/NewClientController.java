package sample;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class NewClientController implements Initializable {
    public TextField nameField;
    public TextField addressField;
    public TextField phoneField;
    public TextField eMailField;
    public DatePicker dateField;
    public Button saveButton;
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
            //Company.addClient(newClient);
            dao.executeInsertClient(newClient);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(bundle.getString("success"));
            alert.setHeaderText(null);
            alert.setContentText(bundle.getString("clientAdded"));
            alert.show();
           /* Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.close();*/
        }
    }

    public void cancelClicked (ActionEvent actionEvent) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

}
