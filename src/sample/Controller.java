package sample;

import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    public ListView<Client> clientList;
    public ListView<String> servicesList;
    public MenuItem addClient;
    public MenuItem deleteClient;
    public MenuItem changeClient;
    public Button addClientButton;
    public Button addServiceBtn;
    public Button deleteServiceBtn;

    private Company companyModel;


    public Controller (Company companyModel) {
        this.companyModel = companyModel;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clientList.setItems(companyModel.getClients());

        clientList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                companyModel.setClickedClient(clientList.getSelectionModel().getSelectedItem());
                if (mouseEvent.getClickCount() == 2) {
                    onChange();
                }
            }
        });
        servicesList.setItems(companyModel.getServices());

    }

    public void onAdd() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/newClient.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (root != null) {
            Stage secondaryStage = new Stage();
            secondaryStage.setTitle("New Client");
            secondaryStage.setResizable(false);
            secondaryStage.setScene(new Scene(root, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE));
            secondaryStage.initModality(Modality.APPLICATION_MODAL);
            secondaryStage.show();
        }
    }

    public void onDelete () {
        if (companyModel.getClickedClient() != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Are you sure you want to delete this client?", ButtonType.YES, ButtonType.NO);
            alert.setTitle("Warning!");
            alert.setHeaderText(null);
            Optional<ButtonType> option = alert.showAndWait();
            if (option.get() == ButtonType.YES) {
                companyModel.removeClient();
                Alert newAlert = new Alert(Alert.AlertType.CONFIRMATION, "Client deleted!");
                newAlert.setTitle("SUCCESS!");
                newAlert.setHeaderText(null);
                newAlert.show();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Please select the client you want to delete!");
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.showAndWait();
        }
    }

    public void onChange () {
        if (companyModel.getClickedClient() != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/client.fxml"));
            loader.setController(new ClientController(companyModel));
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (root != null) {
                Stage secondaryStage = new Stage();
                secondaryStage.setTitle(companyModel.getClickedClient().getName());
                secondaryStage.setResizable(false);
                secondaryStage.setScene(new Scene(root, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE));
                secondaryStage.initModality(Modality.APPLICATION_MODAL);
                secondaryStage.show();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Please select the client you want to change!");
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.showAndWait();
        }
    }

    public void onAddService () {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addService.fxml"));
        loader.setController(new AddServiceController(companyModel));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (root != null) {
            Stage secondaryStage = new Stage();
            secondaryStage.setTitle("New service");
            secondaryStage.setResizable(false);
            secondaryStage.setScene(new Scene(root, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE));
            secondaryStage.initModality(Modality.APPLICATION_MODAL);
            secondaryStage.show();
        }
    }

    public void onDeleteService () {
        String service = servicesList.getSelectionModel().getSelectedItem();
        if (service != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Are you sure you want to delete this service?", ButtonType.YES, ButtonType.NO);
            alert.setTitle("Warning!");
            alert.setHeaderText(null);
            Optional<ButtonType> option = alert.showAndWait();
            if (option.get() == ButtonType.YES) {
                companyModel.getServices().remove(service);
                Alert newAlert = new Alert(Alert.AlertType.CONFIRMATION, "Service deleted!");
                newAlert.setTitle("SUCCESS!");
                newAlert.setHeaderText(null);
                newAlert.show();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Please select the service you want to delete!");
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.showAndWait();
        }
    }

}