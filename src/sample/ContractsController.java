package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
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
   // private Company companyModel;
    private DatabaseDAO dao;
    private Company company;
    private ResourceBundle bundle;
    private boolean contractSelected = false;
    private ObservableList<Contract> clientContracts = FXCollections.observableArrayList();

    public ContractsController (DatabaseDAO databaseDAO, Company company) {
        dao = databaseDAO;
        this.company = company;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Title.setCellValueFactory(new PropertyValueFactory<>("titleOfContract"));
        DateFrom.setCellValueFactory(new PropertyValueFactory<>("signDate"));
        DateTo.setCellValueFactory(new PropertyValueFactory<>("endDate"));
       // contractTableView.setItems(companyModel.getClickedClient().getContractList());
        clientContracts.addAll(dao.executeGetClientContractsQuery(company.getClickedClient().getId()));
        contractTableView.setItems(clientContracts);
        bundle = resourceBundle;

        contractTableView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                contractSelected = true;
                company.setClickedContract(contractTableView.getSelectionModel().getSelectedItem());
            }
        });

        contractTableView.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                contractSelected = false;
                company.setClickedContract(null);
            }
        });

    }

    public void onAddContract () {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addContract.fxml"), bundle);
        loader.setController(new AddContractController(dao, company));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (root != null) {
            Stage secondaryStage = new Stage();
            secondaryStage.setTitle(bundle.getString("addNewContract"));
            secondaryStage.setResizable(false);
            secondaryStage.setScene(new Scene(root, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE));
            secondaryStage.initModality(Modality.APPLICATION_MODAL);
            secondaryStage.show();
            secondaryStage.setOnCloseRequest(windowEvent -> {
                clientContracts.clear();
                clientContracts.addAll(dao.executeGetClientContractsQuery(company.getClickedClient().getId()));
                secondaryStage.close();
            });
        }
    }

    public void onDeleteContract () {
        if (contractSelected) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, bundle.getString("alertContractDelete"), ButtonType.YES, ButtonType.NO);
            alert.setTitle(bundle.getString("warning"));
            alert.setHeaderText(null);
            Optional<ButtonType> option = alert.showAndWait();
            if (option.get() == ButtonType.YES) {
               // companyModel.getClickedClient().getContractList().remove(contractTableView.getSelectionModel().getSelectedItem());
                dao.executeDeleteContract(company.getClickedContract().getId());
                System.out.println("IZ CONTROLLERA CLIENTS, UGOVOR SELEKTOVANI JE " + company.getClickedContract().getTitleOfContract()
                + ", id je " + company.getClickedContract().getId());
                Alert newAlert = new Alert(Alert.AlertType.CONFIRMATION, bundle.getString("contractDeleted"));
                newAlert.setTitle(bundle.getString("success"));
                newAlert.setHeaderText(null);
                newAlert.show();
                newAlert.setOnCloseRequest(dialogEvent ->  {
                    clientContracts.clear();
                    clientContracts.addAll(dao.executeGetClientContractsQuery(company.getClickedClient().getId()));
                    newAlert.close();
                });
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, bundle.getString("contractNotSelected"));
            alert.setTitle(bundle.getString("error"));
            alert.setHeaderText(null);
            alert.showAndWait();
        }
    }

}
