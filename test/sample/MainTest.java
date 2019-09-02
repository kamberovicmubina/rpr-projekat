package sample;

import static org.junit.jupiter.api.Assertions.*;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import sample.controllers.Controller;

import java.util.*;

@ExtendWith(ApplicationExtension.class)

public class MainTest {
    @Start
    public void start (Stage stage) throws Exception {
        DatabaseDAO databaseDAO = DatabaseDAO.getInstance();
        ResourceBundle bundle = ResourceBundle.getBundle("Translation", new Locale("en", "EN"));
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/fxml/sample.fxml"), bundle);
        loader.setController(new Controller(databaseDAO));
        Parent mainNode = loader.load();
        stage.setScene(new Scene(mainNode));
        stage.show();
        stage.toFront();
    }

    @Test
    void deleteServiceNotSelected (FxRobot robot) {
        robot.clickOn("#deleteServiceBtn");
        final javafx.stage.Stage actualAlertDialog = getTopModalStage(robot);
        assertNotNull(actualAlertDialog);
    }

    private javafx.stage.Stage getTopModalStage(FxRobot robot) {
        final List<Window> allWindows = new ArrayList<>(robot.robotContext().getWindowFinder().listWindows());
        Collections.reverse(allWindows);

        return (javafx.stage.Stage) allWindows
                .stream()
                .filter(window -> window instanceof javafx.stage.Stage)
                .filter(window -> ((javafx.stage.Stage) window).getModality() == Modality.APPLICATION_MODAL)
                .findFirst()
                .orElse(null);
    }

    @Test
    void changeClientNotSelected (FxRobot robot) {
        robot.rightClickOn("#clientList");
        robot.press(KeyCode.ALT).press(KeyCode.C).release(KeyCode.C).release(KeyCode.ALT);
        final javafx.stage.Stage newStage = getTopModalStage(robot);
        assertNotNull(newStage);
        assertEquals("Error", newStage.getTitle());
    }


}