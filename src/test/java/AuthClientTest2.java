import com.example.demo3.HelloApplication;
import com.example.demo3.Model.Channel;
import com.example.demo3.Model.Person;
import com.example.demo3.repository.MainRepository;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.api.FxAssert.verifyThat;

public class AuthClientTest2 extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        HelloApplication helloApp = new HelloApplication();
        helloApp.start(stage);
    }

    @Test
    public void testSuccessfulLogin() {
        TextField usernameField = lookup("#usernameField").query();
        PasswordField passwordField = lookup("#passwordField").query();
        Button loginButton = lookup("#loginButton").queryButton();

        clickOn(usernameField).write("mara");
        clickOn(passwordField).write("mara123");

        clickOn(loginButton);

        assertTrue(MainRepository.getInstance().get("jwt") != null, "Token nije setovan.");
    }

    @Test
    public void testFailedLogin() {

        TextField usernameField = lookup("#usernameField").query();
        PasswordField passwordField = lookup("#passwordField").query();
        Button loginButton = lookup("#loginButton").query();


        clickOn(usernameField).write("invalidUsername");
        clickOn(passwordField).write("invalidPassword");


        clickOn(loginButton);

        verifyThat(".dialog-pane", (Node dialog) -> {
            String contentText = ((DialogPane) dialog).getContentText();
            return contentText.contains("Pogrešno korisničko ime ili lozinka");
        });
    }

}
