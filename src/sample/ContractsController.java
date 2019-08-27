package sample;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class ContractsController implements Initializable {
    public TableView<Contract> contractTableView;
    public TableColumn<Contract, String> Title;
    public TableColumn<Contract, LocalDate> DateFrom;
    public TableColumn<Contract, LocalDate> DateTo;
    private Company companyModel;

    public ContractsController (Company cm) {
        companyModel = cm;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Title.setCellValueFactory(new PropertyValueFactory<>("titleOfContract"));
        DateFrom.setCellValueFactory(new PropertyValueFactory<>("signDate"));
        DateTo.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        contractTableView.setItems(companyModel.getClickedClient().getContractList());

    }

    public void onAddContract () {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addContract.fxml"));
        loader.setController(new AddContractController(companyModel));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (root != null) {
            Stage secondaryStage = new Stage();
            secondaryStage.setTitle("Add new client contract");
            secondaryStage.setScene(new Scene(root, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE));
            secondaryStage.initModality(Modality.APPLICATION_MODAL);
            secondaryStage.show();
        }
    }

    public void onDeleteContract () {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Are you sure you want to delete this contract?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Warning!");
        alert.setHeaderText(null);
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == ButtonType.YES) {
            companyModel.getClickedClient().getContractList().remove(contractTableView.getSelectionModel().getSelectedItem());
            Alert newAlert = new Alert(Alert.AlertType.CONFIRMATION, "Contract deleted!");
            newAlert.setTitle("SUCCESS!");
            newAlert.setHeaderText(null);
            newAlert.show();

        }
    }

}
