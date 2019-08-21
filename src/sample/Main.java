//SISTEM ZA UPRAVLJANJE KLIJENTIMA KOMPANIJE (CRM)

package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Date;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        ObservableList<Department> departments = FXCollections.observableArrayList();
        ObservableList<Employee> employees = FXCollections.observableArrayList();
        ObservableList<Client> clients = FXCollections.observableArrayList();
        Date date = new Date(1998, 7,27);
        clients.add(new Client("Mubina Kamberović", date, "D. Ozme", "018732873", "mub@gmail.com", null ));
        Company company = new Company("Building company", "Obala 2", departments, employees, clients, null);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/sample.fxml"));
        loader.setController(new Controller(company));
        Parent root = loader.load();
        primaryStage.setTitle("CRM");
        primaryStage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
