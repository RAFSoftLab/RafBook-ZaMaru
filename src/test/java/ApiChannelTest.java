import com.example.demo3.HelloApplication;
import com.example.demo3.Model.Channel;
import com.example.demo3.view.ChannelView;
import com.example.demo3.Controller.AuthClient;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import static org.junit.jupiter.api.Assertions.*;


public class ApiChannelTest extends ApplicationTest {

    private ChannelView channelView;
    private TableView<Channel> table;

    @Override
    public void start(Stage stage) throws Exception {
        HelloApplication helloApp = new HelloApplication();
        helloApp.start(stage);
    }


    @Test
    public void testSearchFunctionality() {
        TextField usernameField = lookup("#usernameField").query();
        PasswordField passwordField = lookup("#passwordField").query();
        Button loginButton = lookup("#loginButton").queryButton();

        clickOn(usernameField).write("mara");
        clickOn(passwordField).write("mara123");

        clickOn(loginButton);

        TabPane tabPane = lookup("#tabPane").query();
        tabPane.getSelectionModel().select(1);

        TableView<Channel> table2 = (TableView<Channel>) lookup("#table2").query();
        assertNotNull(table2, "Tabela nije pronađena.");

        clickOn("Pretraga:").write("General");

        ObservableList<Channel> items = table2.getItems();
        boolean found = items.stream().anyMatch(channel ->
                channel.getName().contains("General"));

        assertTrue(found, "Pretraga nije pronašla kanal sa nazivom 'Novi Kanal'.");
    }



    @Test
    public void testTableIsNotEmpty() {
        TextField usernameField = lookup("#usernameField").query();
        PasswordField passwordField = lookup("#passwordField").query();
        Button loginButton = lookup("#loginButton").queryButton();

        clickOn(usernameField).write("mara");
        clickOn(passwordField).write("mara123");

        clickOn(loginButton);

        TabPane tabPane = lookup("#tabPane").query();
        tabPane.getSelectionModel().select(1);

        TableView<Channel> table2 = (TableView<Channel>) lookup("#table2").query();

        assertNotNull(table2, "Tabela nije pronađena.");

        ObservableList<Channel> items = table2.getItems();

        assertNotNull(items, "Podaci u tabeli ne smeju biti null.");
        assertFalse(items.isEmpty(), "Tabela kanala ne sme biti prazna.");
    }

    @Test
    public void testAddChannel() {

        TextField usernameField = lookup("#usernameField").query();
        PasswordField passwordField = lookup("#passwordField").query();
        Button loginButton = lookup("#loginButton").queryButton();

        clickOn(usernameField).write("mara");
        clickOn(passwordField).write("mara123");

        clickOn(loginButton);

        TabPane tabPane = lookup("#tabPane").query();
        tabPane.getSelectionModel().select(1);

        TextField nameField = (TextField) lookup("#name").nth(0).query();
        TextField descriptionField = (TextField) lookup("#description").nth(1).query();

        clickOn(nameField).write("Novi Kanal");
        clickOn(descriptionField).write("Opis novog kanala");

        clickOn("Dodaj");


        TableView<Channel> table2 = (TableView<Channel>) lookup("#table2").query();
        assertNotNull(table2, "Tabela nije pronađena.");

        ObservableList<Channel> items = table2.getItems();
        assertNotNull(items, "Podaci u tabeli ne smeju biti null.");
        assertTrue(items.size() > 0, "Tabela ne sadrži nove kanale.");

        boolean found = items.stream().anyMatch(channel ->
                channel.getName().equals("Novi Kanal") && channel.getDescription().equals("Opis novog kanala"));

        assertTrue(found, "Novi kanal nije dodat u tabelu.");
    }

}
