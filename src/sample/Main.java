//SISTEM ZA UPRAVLJANJE KLIJENTIMA KOMPANIJE (CRM)

package sample;

import javafx.application.Application;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.Locale;
import java.util.ResourceBundle;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
       /* ObservableList<Department> departments = FXCollections.observableArrayList();
        ObservableList<Employee> employees = FXCollections.observableArrayList();
        ObservableList<Client> clients = FXCollections.observableArrayList();
        LocalDate date = LocalDate.of(1998, 7,27);
        Client mubina = new Client("Mubina Kamberović", date, "D. Ozme", "018732873", "mub@gmail.com", null );
        Contract mubinaContract = new Contract("Employment contract", mubina, date, date);
        ObservableList<Contract> mubinaCOntracts = FXCollections.observableArrayList();
        mubinaCOntracts.addAll(mubinaContract);
        mubina.setContractList(mubinaCOntracts);
        Client novi = new Client("Novi klijent", date, "Obala", "088923324", "mail@yahoo.com", null);
        Contract noviContract = new Contract("Employment contract", novi, date, date);
        Contract noviContract2 = new Contract("Parking contract", novi, date, date);
        ObservableList<Contract> noviContracts = FXCollections.observableArrayList();
        noviContracts.addAll(noviContract, noviContract2);
        novi.setContractList(noviContracts);
        clients.add(mubina);
        clients.add(novi);
        ObservableList<String> services = FXCollections.observableArrayList();
        services.addAll("General Contracting", "Pre-Construction", "Design-Build Services", "Construction Management");
        Company company = new Company("Building company", "Obala 2", departments, employees, clients, null, services);*/

        DatabaseDAO databaseDAO = DatabaseDAO.getInstance();
        ResourceBundle bundle = ResourceBundle.getBundle("Translation", new Locale("en", "EN"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/sample.fxml"), bundle);
        loader.setController(new Controller(databaseDAO));
        Parent root = loader.load();
        primaryStage.setTitle("CRM");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
        DatabaseDAO.removeInstance();
    }
}