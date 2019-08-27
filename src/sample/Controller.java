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
    public Button addClientButton;
    private ObservableList<String> servicesNames = FXCollections.observableArrayList();

    private Company companyModel;


    public Controller (Company companyModel) {
        this.companyModel = companyModel;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clientList.setItems(companyModel.getClients());

        clientList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Client>() {
            @Override
            public void changed(ObservableValue<? extends Client> observableValue, Client client, Client t1) {
                companyModel.setClickedClient(clientList.getSelectionModel().getSelectedItem());
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
                    secondaryStage.setScene(new Scene(root, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE));
                    secondaryStage.initModality(Modality.APPLICATION_MODAL);
                    secondaryStage.show();
                }
            }
        });
        servicesNames.addAll(companyModel.getServices());
        servicesList.setItems(servicesNames);

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
            secondaryStage.setScene(new Scene(root, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE));
            secondaryStage.initModality(Modality.APPLICATION_MODAL);
            secondaryStage.show();
        }
    }



}