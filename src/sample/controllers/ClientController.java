package sample.controllers;

import javafx.beans.property.*;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.*;
import sample.classes.Client;
import sample.classes.Company;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class ClientController implements Initializable {
    public TextField nameField;
    public DatePicker dateOfBirthPicker;
    public TextField addressField;
    public TextField phoneField;
    public TextField eMailField;
    public Button cancelButton;
    public Spinner<Double> profitSpinner;
    private SimpleStringProperty nameProperty;
    private SimpleStringProperty addressProperty;
    private SimpleStringProperty phoneProperty;
    private SimpleStringProperty eMailProperty;
    private ObjectProperty<LocalDate> dateOfBirthProperty;
    private DatabaseDAO dao;
    private Company company;
    private boolean nameValid = true;
    private boolean addressValid = true;
    private boolean phoneValid = true;
    private boolean eMailValid = true;
    private boolean dateValid = true;
    private ResourceBundle bundle;

    public ClientController (DatabaseDAO databaseDAO, Company company) {
        dao = databaseDAO;
        this.company = company;
        nameProperty = new SimpleStringProperty(this.company.getClickedClient().getName());
        addressProperty = new SimpleStringProperty(this.company.getClickedClient().getAddress());
        phoneProperty = new SimpleStringProperty(this.company.getClickedClient().getPhoneNumber());
        eMailProperty = new SimpleStringProperty(this.company.getClickedClient().getEMail());
        dateOfBirthProperty = new SimpleObjectProperty<>(this.company.getClickedClient().getDateOfBirth());
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bundle = resourceBundle;

        nameField.textProperty().bindBidirectional(nameProperty);
        addressField.textProperty().bindBidirectional(addressProperty);
        phoneField.textProperty().bindBidirectional(phoneProperty);
        eMailField.textProperty().bindBidirectional(eMailProperty);
        dateOfBirthPicker.valueProperty().bindBidirectional(dateOfBirthProperty);

        nameField.setText(company.getClickedClient().getName());
        addressField.setText(company.getClickedClient().getAddress());
        phoneField.setText(company.getClickedClient().getPhoneNumber());
        eMailField.setText(company.getClickedClient().getEMail());
        dateOfBirthPicker.setValue(company.getClickedClient().getDateOfBirth());

        SpinnerValueFactory<Double> valueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(1, 10000000);
        profitSpinner.setValueFactory(valueFactory);
        profitSpinner.getValueFactory().setValue(company.getClickedClient().getProfit());

        // validation of fields in case they are changed
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
            alert.setTitle(bundle.getString("error"));
            alert.setHeaderText(null);
            alert.setContentText(bundle.getString("incorrectInfo"));
            alert.showAndWait();
        }
        else {
            String name = nameField.getText();
            LocalDate date = dateOfBirthPicker.getValue();
            System.out.println(date.getYear() + " " + date.getMonthValue() + " " + date.getDayOfMonth());
            String address = addressField.getText();
            String phone = phoneField.getText();
            String eMail = eMailField.getText();
            Client newClient = new Client(name, date, address, phone, eMail, null);
            newClient.setId(company.getClickedClient().getId());
            newClient.setProfit(profitSpinner.getValue());
            newClient.setContractList(company.getClickedClient().getContractList());
            dao.executeChangeClient(newClient);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(bundle.getString("success"));
            alert.setHeaderText(null);
            alert.setContentText(bundle.getString("clientChanged"));
            alert.showAndWait();
        }
    }

    public void cancelClicked() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void deleteClicked () {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, bundle.getString("alertClientDelete"), ButtonType.YES, ButtonType.NO);
        alert.setTitle(bundle.getString("warning"));
        alert.setHeaderText(null);
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == ButtonType.YES) {
            dao.executeDeleteClient(company.getClickedClient().getId());
            Alert newAlert = new Alert(Alert.AlertType.CONFIRMATION, bundle.getString("clientDeleted"));
            newAlert.setTitle(bundle.getString("success"));
            newAlert.setHeaderText(null);
            newAlert.show();
            cancelClicked();
        }

    }

    public void contractsClicked () {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/contracts.fxml"), bundle);
        loader.setController(new ContractsController(dao, company));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (root != null) {
            Stage secondaryStage = new Stage();
            secondaryStage.setTitle(bundle.getString("clientContracts"));
            secondaryStage.setResizable(false);
            secondaryStage.setScene(new Scene(root, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE));
            secondaryStage.initModality(Modality.APPLICATION_MODAL);
            secondaryStage.show();
        }
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
