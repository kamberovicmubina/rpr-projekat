package sample;

import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AddServiceController implements Initializable {
    public TextField serviceField;
    public Button addBtn;
    private Company companyModel;

    public AddServiceController (Company cm) {
        companyModel = cm;
    }

    public void onAdd () {
        companyModel.getServices().add(serviceField.getText());
        Alert newAlert = new Alert(Alert.AlertType.CONFIRMATION, "New service added!");
        newAlert.setTitle("SUCCESS!");
        newAlert.setHeaderText(null);
        newAlert.show();
        Stage stage = (Stage) addBtn.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
