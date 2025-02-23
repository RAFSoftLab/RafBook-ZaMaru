import com.example.demo3.HelloApplication;
import com.example.demo3.Model.Channel;
import com.example.demo3.Model.RolePermissionDTO;
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
        ComboBox<String> categoryComboBox = lookup("#categoryComboBox").query();

        clickOn(nameField).write("Novi Kanal");
        clickOn(descriptionField).write("Opis novog kanala");

        clickOn(categoryComboBox);
        categoryComboBox.getSelectionModel().select(0);

        clickOn("Dodaj");

        TableView<Channel> table2 = (TableView<Channel>) lookup("#table2").query();
        assertNotNull(table2, "Tabela nije pronađena.");

        ObservableList<Channel> items = table2.getItems();
        assertNotNull(items, "Podaci u tabeli ne smeju biti null.");
        assertTrue(items.size() > 0, "Tabela ne sadrži nove kanale.");

        boolean found = items.stream().anyMatch(channel ->
                channel.getName().equals("Novi Kanal") &&
                        channel.getDescription().equals("Opis novog kanala"));

        assertTrue(found, "Novi kanal nije dodat u tabelu.");
    }

    @Test
    public void testAddCategory(){
        TextField usernameField = lookup("#usernameField").query();
        PasswordField passwordField = lookup("#passwordField").query();
        Button loginButton = lookup("#loginButton").queryButton();

        clickOn(usernameField).write("mara");
        clickOn(passwordField).write("mara123");

        clickOn(loginButton);

        TabPane tabPane = lookup("#tabPane").query();
        tabPane.getSelectionModel().select(1);

        TextField categoryField = (TextField) lookup("#catName").nth(0).query();
        TextField categoryField2 = (TextField) lookup("#catDesc").nth(1).query();

        clickOn(categoryField).write("Nova kategorija");
        clickOn(categoryField2).write("Opis nove kategorije");

        Button addCategory = lookup("#addButton3").queryButton();
        clickOn(addCategory);


    }

    @Test
    public void testAddRoleToChannel(){
        TextField usernameField = lookup("#usernameField").query();
        PasswordField passwordField = lookup("#passwordField").query();
        Button loginButton = lookup("#loginButton").queryButton();

        clickOn(usernameField).write("mara");
        clickOn(passwordField).write("mara123");

        clickOn(loginButton);

        TabPane tabPane = lookup("#tabPane").query();
        tabPane.getSelectionModel().select(1);

        TableView<Channel> table2 = lookup("#table2").query();
        assertNotNull(table2, "Tabela nije pronađena.");

        ObservableList<Channel> items = table2.getItems();
        assertNotNull(items, "Podaci u tabeli ne smeju biti null.");
        assertFalse(items.isEmpty(), "Tabela ne sme biti prazna pre dodavanja uloge.");
        assertTrue(items.size() >= 6, "Tabela mora imati najmanje šest redova za testiranje.");

        Channel channelToEdit = items.get(5);
        table2.getSelectionModel().select(5);

        sleep(500);

        assertEquals(channelToEdit, table2.getSelectionModel().getSelectedItem(), "Kanal nije selektovan.");

        Button roleButton = lookup("#roleButton2").queryButton();
        clickOn(roleButton);

        sleep(500);

        TextField roleField = lookup("#roleField2").query();
        clickOn(roleField).eraseText(roleField.getText().length()).write("STUDENT");

        Button addRoleButton = lookup("#addRole2").queryButton();
        clickOn(addRoleButton);




    }

    @Test
    public void testDeleteRoleFromChannel() {
        TextField usernameField = lookup("#usernameField").query();
        PasswordField passwordField = lookup("#passwordField").query();
        Button loginButton = lookup("#loginButton").queryButton();

        clickOn(usernameField).write("mara");
        clickOn(passwordField).write("mara123");
        clickOn(loginButton);

        TabPane tabPane = lookup("#tabPane").query();
        tabPane.getSelectionModel().select(1);

        TableView<Channel> table2 = lookup("#table2").query();
        assertNotNull(table2, "Tabela nije pronađena.");

        ObservableList<Channel> items = table2.getItems();
        assertNotNull(items, "Podaci u tabeli ne smeju biti null.");
        assertFalse(items.isEmpty(), "Tabela ne sme biti prazna pre brisanja uloge.");
        assertTrue(items.size() >= 6, "Tabela mora imati najmanje šest redova za testiranje.");

        Channel channelToEdit = items.get(5);
        table2.getSelectionModel().select(5);

        sleep(500);
        assertEquals(channelToEdit, table2.getSelectionModel().getSelectedItem(), "Kanal nije selektovan.");

        Button roleButton = lookup("#roleButton2").queryButton();
        clickOn(roleButton);

        sleep(500);

        TableView<RolePermissionDTO> popUpTable = lookup("#rolesTableView").query();
        assertNotNull(popUpTable, "Pop-up tabela nije pronađena.");

        ObservableList<RolePermissionDTO> popUpItems = popUpTable.getItems();
        assertNotNull(popUpItems, "Podaci u pop-up tabeli ne smeju biti null.");
        assertFalse(popUpItems.isEmpty(), "Pop-up tabela ne sme biti prazna pre brisanja uloge.");
        assertTrue(popUpItems.size() >= 1, "Pop-up tabela mora imati najmanje jedan red za testiranje.");

        popUpTable.getSelectionModel().select(0);

        sleep(500);
        assertEquals(popUpItems.get(0), popUpTable.getSelectionModel().getSelectedItem(), "Uloga nije selektovana.");

        Button deleteRoleButton = lookup("#deleteRole2").queryButton();
        clickOn(deleteRoleButton);

        sleep(500);
    }


}
