package sample;

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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    public ListView<String> clientList;
    public ListView<String> servicesList;
    public Button addClientButton;
    private ObservableList<String> clientNames = FXCollections.observableArrayList();
    private ObservableList<String> servicesNames = FXCollections.observableArrayList();

    private Company companyModel;


    public Controller (Company companyModel) {
        this.companyModel = companyModel;
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for (Client c : companyModel.getClients()) {
            clientNames.add(c.getName());
        }
        clientList.setItems(clientNames);

        clientList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue){
                if (newValue != null) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/client.fxml"));
                    openNewWindow(loader);
                }
            }
        });

        servicesNames.addAll(companyModel.getServices());
        servicesList.setItems(servicesNames);


    }

    public void onAdd() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/newClient.fxml"));
        openNewWindow(loader);
    }

    public void openNewWindow(FXMLLoader loader) {
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
