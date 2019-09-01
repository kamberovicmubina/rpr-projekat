package sample;

import static org.junit.jupiter.api.Assertions.*;

import com.sun.jdi.ObjectCollectedException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.util.Locale;
import java.util.ResourceBundle;

@ExtendWith(ApplicationExtension.class)

class MainTest {
    @Start
    public void start (Stage stage) throws Exception {
        DatabaseDAO databaseDAO = DatabaseDAO.getInstance();
        ResourceBundle bundle = ResourceBundle.getBundle("Translation", new Locale("en", "EN"));
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/fxml/sample.fxml"), bundle);
        loader.setController(new Controller(databaseDAO));
        Parent mainNode = loader.load();
        //Parent mainNode = FXMLLoader.setController(new Controller(databaseDAO)).load(Main.class.getResource("/fxml/sample.fxml"), bundle);
        stage.setScene(new Scene(mainNode));
        stage.show();
        stage.toFront();
    }


}