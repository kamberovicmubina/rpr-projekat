package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class InputTimePeriodController implements Initializable {
    public DatePicker startTime;
    public DatePicker endTime;
    private DatabaseDAO dao;
    private ResourceBundle bundle;

    public InputTimePeriodController (DatabaseDAO dao) {
        this.dao = dao;
    }

    public ObservableList<Client> getClientsInPeriodOfTime (LocalDate start, LocalDate end) {
        if (start.isAfter(end)) return null;
        ObservableList<Client> validClients = FXCollections.observableArrayList();
        ObservableList<Client> clients = dao.executeGetClientsQuery();
        for (Client c : clients) {
            ObservableList<Contract> contracts = c.getContractList();
            for (Contract contract : contracts) {
                if (contract.getEndDate().isAfter(start)) {
                    validClients.add(c);
                    break;
                }
            }
        }
        return validClients;
    }


    public void onShowStatistic () {
        if (startTime != null && endTime != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/clientNumberStat.fxml"), bundle);
            loader.setController(new ClientNumberController(dao, getClientsInPeriodOfTime(startTime.getValue(), endTime.getValue())));
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (root != null) {
                Stage secondaryStage = new Stage();
                secondaryStage.setTitle(bundle.getString("numberStat"));
                secondaryStage.setResizable(false);
                secondaryStage.setScene(new Scene(root, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE));
                secondaryStage.initModality(Modality.APPLICATION_MODAL);
                secondaryStage.show();
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bundle = resourceBundle;
    }
}
