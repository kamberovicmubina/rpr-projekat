//SISTEM ZA UPRAVLJANJE KLIJENTIMA KOMPANIJE (CRM)

package sample;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;
import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        DatabaseDAO databaseDAO = DatabaseDAO.getInstance();
        ResourceBundle bundle = ResourceBundle.getBundle("Translation", new Locale("en", "EN"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/sample.fxml"), bundle);
        loader.setController(new Controller(databaseDAO));
        Parent root = loader.load();
        primaryStage.setTitle("CRM");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        primaryStage.show();
        regenerateFile();
    }

    void regenerateFile() {
        DatabaseDAO.removeInstance();
        File dbfile = new File("database.db");
        dbfile.delete();
    }
    public static void main(String[] args) {
        launch(args);
        DatabaseDAO.removeInstance();

    }
}