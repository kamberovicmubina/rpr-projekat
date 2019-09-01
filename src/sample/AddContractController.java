package sample;

import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

public class AddContractController implements Initializable {
    public TextField titleField;
    public DatePicker fromPicker;
    public DatePicker toPicker;
    public Button cancelBtn;
    private DatabaseDAO dao;
    private Company company;
    private ResourceBundle bundle;

    public AddContractController (DatabaseDAO databaseDAO, Company company) {
        dao = databaseDAO;
        this.company = company;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bundle = resourceBundle;
    }

    public void onSave () {
        if (FieldsValidation.validDate(fromPicker.getValue()) && FieldsValidation.dateInFuture(toPicker.getValue())) {
            Contract newContract = new Contract(titleField.getText(), company.getClickedClient(), fromPicker.getValue(), toPicker.getValue());
            newContract.setId(dao.getNextAvailableContractId());
            dao.executeInsertContract(newContract);
            Alert newAlert = new Alert(Alert.AlertType.CONFIRMATION, bundle.getString("contractAdded"));
            newAlert.setTitle(bundle.getString("success"));
            newAlert.setHeaderText(null);
            newAlert.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(bundle.getString("error"));
            alert.setHeaderText(null);
            alert.setContentText(bundle.getString("incorrectInfo"));
            alert.showAndWait();
        }
    }

    public void onCancel () {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }
}
