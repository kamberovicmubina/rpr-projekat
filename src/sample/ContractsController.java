package sample;

import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
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

    }

    public void onDeleteContract () {

    }

}
