package sample;

import static org.junit.jupiter.api.Assertions.*;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
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
import sample.controllers.NewClientController;

import java.io.IOException;
import java.util.*;
import java.util.List;

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
    @Test
    void testAddClientAddress (FxRobot robot) {
        robot.clickOn("#addClientButton");
        Platform.runLater(() -> {
            final Stage newStage = getTopModalStage(robot);
            newStage.show();
            newStage.toFront();
            TextField address = robot.lookup("#addressField").queryAs(TextField.class);
            robot.clickOn("#addressField");
            assertAll(() -> {
                robot.write("Test adresa");
                assertTrue(address.getStyleClass().contains("fieldCorrect"));
            }, () -> {
                for (int i = 0; i < 11; i++) robot.press(KeyCode.BACK_SPACE);
                robot.write("21");
                assertTrue(address.getStyleClass().contains("fieldIncorrect"));
            });

        });
    }

    @Test
    void testAddClientName (FxRobot robot) {
        robot.clickOn("#addClientButton");
        Platform.runLater(() -> {
            final Stage newStage = getTopModalStage(robot);
            newStage.show();
            newStage.toFront();
            TextField name = robot.lookup("#nameField").queryAs(TextField.class);
            robot.clickOn("#nameField");
            assertAll(() -> {
                robot.write("Test klijent");
                assertTrue(name.getStyleClass().contains("fieldCorrect"));
            },
                    () -> {
                        robot.write("123");
                        assertTrue(name.getStyleClass().contains("fieldIncorrect"));
                    });

        });
    }


    @Test
    void testAddClientCorrectPhone (FxRobot robot) {
        robot.clickOn("#addClientButton");
        Platform.runLater(() -> {
            final Stage newStage = getTopModalStage(robot);
            newStage.show();
            newStage.toFront();
            TextField phone = robot.lookup("#phoneField").queryAs(TextField.class);
            robot.clickOn("#phoneField");
            robot.write("023334443");
            assertTrue(phone.getStyleClass().contains("fieldCorrect"));
        });
    }
    @Test
    void testAddClientCorrectMailAndAdd (FxRobot robot) {
        robot.clickOn("#addClientButton");
        Platform.runLater(() -> {
            final Stage newStage = getTopModalStage(robot);
            newStage.show();
            newStage.toFront();
            TextField mail = robot.lookup("#eMailField").queryAs(TextField.class);
            robot.clickOn("#eMailField");
            assertTrue(mail.getStyleClass().contains("defaultClass"));
            robot.write("test@etf.unsa.ba");
            assertTrue(mail.getStyleClass().contains("fieldCorrect"));
            robot.clickOn("#saveButton");
            final Stage newStage1 = getTopModalStage(robot);
            assertNotNull(newStage1);
            assertEquals("Error", newStage1.getTitle());
        });
    }

    @Test
    void testEmptyClientAdd (FxRobot robot) {
        robot.clickOn("#addClientButton");
        Platform.runLater(() -> {
            final Stage newStage = getTopModalStage(robot);
            newStage.show();
            newStage.toFront();
            robot.clickOn("#saveButton");
            final Stage newStage1 = getTopModalStage(robot);
            assertNotNull(newStage1);
            assertEquals("Error", newStage1.getTitle());
        });
    }


}