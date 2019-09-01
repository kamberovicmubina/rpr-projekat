package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import net.sf.jasperreports.engine.JRException;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
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
    public MenuItem bosnianOption;
    public MenuItem englishOption;
    public Button profitButton;
    public BorderPane borderPane;
    private ResourceBundle bundle;
    private DatabaseDAO dao;
    private Company company;
    private ObservableList<String> servicesObservableList = FXCollections.observableArrayList();
    private ObservableList<Client> clientsObservableList = FXCollections.observableArrayList();

    public Controller (DatabaseDAO databaseDAO) {
        dao = databaseDAO;
        company = dao.executeGetCompanyQuery(1);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bundle = resourceBundle;
        clientsObservableList.addAll(dao.executeGetClientsQuery());
        clientList.setItems(clientsObservableList);

        clientList.setOnMouseClicked(mouseEvent -> {
            company.setClickedClient(clientList.getSelectionModel().getSelectedItem());
            if (mouseEvent.getClickCount() == 2) {
                try {
                    onChange();
                } catch (ObjectNotSelectedException e) {
                    e.printStackTrace();
                }
            }
        });
        servicesObservableList.addAll(dao.executeGetServices());
        servicesList.setItems(servicesObservableList);

    }

    public void onAdd() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/newClient.fxml"), bundle);
        loader.setController(new NewClientController(dao));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (root != null) {
            Stage secondaryStage = new Stage();
            secondaryStage.setTitle(bundle.getString("newClient"));
            secondaryStage.setResizable(false);
            secondaryStage.setScene(new Scene(root, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE));
            secondaryStage.initModality(Modality.APPLICATION_MODAL);
            secondaryStage.show();
            secondaryStage.setOnCloseRequest(windowEvent -> {
                clientsObservableList.clear();
                clientsObservableList.addAll(dao.executeGetClientsQuery());
                secondaryStage.close();
            });
        }
    }

    public void onDelete () throws ObjectNotSelectedException {
        try {
            if (company.getClickedClient() != null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, bundle.getString("alertClientDelete"), ButtonType.YES, ButtonType.NO);
                alert.setTitle(bundle.getString("warning"));
                alert.setHeaderText(null);
                Optional<ButtonType> option = alert.showAndWait();
                if (option.get() == ButtonType.YES) {
                    //companyModel.removeClient();
                    dao.executeDeleteClient(company.getClickedClient().getId());
                    Alert newAlert = new Alert(Alert.AlertType.CONFIRMATION, bundle.getString("clientDeleted"));
                    newAlert.setTitle(bundle.getString("success"));
                    newAlert.setHeaderText(null);
                    newAlert.show();
                    newAlert.setOnCloseRequest(dialogEvent -> {
                        clientsObservableList.clear();
                        clientsObservableList.addAll(dao.executeGetClientsQuery());
                        newAlert.close();
                    });
                }
            } else {
                throw new ObjectNotSelectedException(bundle.getString("clientNotSelected"));
            }
        } catch (ObjectNotSelectedException o) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, o.getMessage());
            alert.setTitle(bundle.getString("error"));
            alert.setHeaderText(null);
            alert.showAndWait();
        }
    }

    public void onChange () throws ObjectNotSelectedException {
        try {
            if (company.getClickedClient() != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/client.fxml"), bundle);
                loader.setController(new ClientController(dao, company));
                Parent root = null;
                try {
                    root = loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (root != null) {
                    Stage secondaryStage = new Stage();
                    secondaryStage.setTitle(company.getClickedClient().getName());
                    secondaryStage.setResizable(false);
                    secondaryStage.setScene(new Scene(root, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE));
                    secondaryStage.initModality(Modality.APPLICATION_MODAL);
                    secondaryStage.show();
                    secondaryStage.setOnCloseRequest(windowEvent -> {
                        clientsObservableList.clear();
                        clientsObservableList.addAll(dao.executeGetClientsQuery());
                        secondaryStage.close();
                    });
                }
            } else {
                throw new ObjectNotSelectedException(bundle.getString("clientChangeNotSelected"));

            }
        } catch (ObjectNotSelectedException o) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, o.getMessage());
            alert.setTitle(bundle.getString("error"));
            alert.setHeaderText(null);
            alert.showAndWait();
        }
    }

    public void onAddService () {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addService.fxml"), bundle);
        loader.setController(new AddServiceController(dao));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (root != null) {
            Stage secondaryStage = new Stage();
            secondaryStage.setTitle(bundle.getString("newService"));
            secondaryStage.setResizable(false);
            secondaryStage.setScene(new Scene(root, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE));
            secondaryStage.initModality(Modality.APPLICATION_MODAL);
            secondaryStage.show();
            secondaryStage.setOnCloseRequest(windowEvent -> {
                servicesObservableList.clear();
                servicesObservableList.addAll(dao.executeGetServices());
                secondaryStage.close();
            });
        }
    }

    public void onDeleteService () throws ObjectNotSelectedException{
        try {
            String service = servicesList.getSelectionModel().getSelectedItem();
            if (service != null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, bundle.getString("alertServiceDelete"), ButtonType.YES, ButtonType.NO);
                alert.setTitle(bundle.getString("warning"));
                alert.setHeaderText(null);
                Optional<ButtonType> option = alert.showAndWait();
                if (option.get() == ButtonType.YES) {
                   // companyModel.getServices().remove(service);
                    dao.executeDeleteService(service);
                    servicesObservableList.clear();
                    servicesObservableList.addAll(dao.executeGetServices());
                    Alert newAlert = new Alert(Alert.AlertType.CONFIRMATION, bundle.getString("serviceDeleted"));
                    newAlert.setTitle(bundle.getString("success"));
                    newAlert.setHeaderText(null);
                    newAlert.show();
                }
            } else {
                throw new ObjectNotSelectedException(bundle.getString("serviceNotSelected"));
            }
        } catch (ObjectNotSelectedException o) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, o.getMessage());
            alert.setTitle(bundle.getString("error"));
            alert.setHeaderText(null);
            alert.showAndWait();
        }
    }

    public void reloadScene () {
        try{
            bundle = ResourceBundle.getBundle("Translation");
            Scene scene = borderPane.getScene();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/sample.fxml"), bundle);
            loader.setController(this);
            scene.setRoot(loader.load());
        } catch (IOException e) {

        }
    }

    public void onBosnian () {
        Locale.setDefault(new Locale("bs", "BA"));
        reloadScene();
    }

    public void onEnglish () {
        Locale.setDefault(new Locale("en", "EN"));
        reloadScene();
    }

    public void printProfit (ActionEvent actionEvent) {
        try {
            new ProfitReport().showReport(dao.getConnection());
        } catch (JRException e1) {
            e1.printStackTrace();
        }

    }

}