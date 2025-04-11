import com.example.demo3.Controller.ApiChannel;
import com.example.demo3.HelloApplication;
import com.example.demo3.HelloController;
import com.example.demo3.Model.Channel;
import com.example.demo3.Model.NewChannelDTO;
import com.example.demo3.Model.RolePermissionDTO;
import com.example.demo3.view.ChannelView;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.testfx.framework.junit5.ApplicationTest;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;

import java.io.IOException;


public class ApiChannelTest extends ApplicationTest {

    private ChannelView channelView;
    private TableView<Channel> table;


    @Override
    public void start(Stage stage) throws Exception {
        HelloApplication helloApp = new HelloApplication();
        helloApp.start(stage);
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

        ComboBox<String> comboBox1 = lookup("#comboBox1").query();
        clickOn(comboBox1);
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);

        ComboBox<String> comboBox2 = lookup("#comboBox2").query();
        clickOn(comboBox2);
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);

        ComboBox<String> comboBox3 = lookup("#comboBox3").query();
        clickOn(comboBox3);
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);

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



//
//
//    @Test
//    public void testDeleteChannel() {
//
//        TextField usernameField = lookup("#usernameField").query();
//        PasswordField passwordField = lookup("#passwordField").query();
//        Button loginButton = lookup("#loginButton").queryButton();
//
//        clickOn(usernameField).write("mara");
//        clickOn(passwordField).write("mara123");
//
//        clickOn(loginButton);
//
//        TabPane tabPane = lookup("#tabPane").query();
//        tabPane.getSelectionModel().select(1);
//
//        TableView<Channel> table2 = lookup("#table2").query();
//        assertNotNull(table2, "Tabela kanala nije pronađena.");
//
//        ObservableList<Channel> items = table2.getItems();
//        assertNotNull(items, "Podaci u tabeli kanala ne smeju biti null.");
//        assertFalse(items.isEmpty(), "Tabela kanala ne sme biti prazna pre brisanja.");
//        assertTrue(items.size() >= 2, "Tabela kanala mora imati najmanje dva reda za testiranje.");
//
//        Channel channelToDelete = items.get(1);
//        table2.getSelectionModel().select(1);
//
//        sleep(500);
//
//        assertEquals(channelToDelete, table2.getSelectionModel().getSelectedItem(), "Kanal nije selektovan.");
//
//        Button deleteButton = lookup("#deleteButton2").queryButton();
//        clickOn(deleteButton);
//
//        sleep(500);
//
//        DialogPane confirmationDialog = lookup(".dialog-pane").query();
//        assertNotNull(confirmationDialog, "Alert za potvrdu brisanja nije prikazan.");
//
//        Button yesButton = lookup(".button").match((Button b) -> b.getText().equalsIgnoreCase("Da")).query();
//        assertNotNull(yesButton, "Dugme 'Da' nije pronađeno u alert dijalogu.");
//        clickOn(yesButton);
//
//        sleep(500);
//
//        items = table2.getItems();
//        boolean found = items.stream().anyMatch(channel -> channel.equals(channelToDelete));
//
//        assertFalse(found, "Kanal nije obrisan iz tabele.");
//    }

    @Test
    public void testEditChannel() {

        TextField usernameField = lookup("#usernameField").query();
        PasswordField passwordField = lookup("#passwordField").query();
        Button loginButton = lookup("#loginButton").queryButton();

        clickOn(usernameField).write("mara");
        clickOn(passwordField).write("mara123");
        clickOn(loginButton);

        TabPane tabPane = lookup("#tabPane").query();
        tabPane.getSelectionModel().select(1);

        TableView<Channel> table2 = lookup("#table2").query();
        assertNotNull(table2, "Tabela kanala nije pronađena.");

        ObservableList<Channel> items = table2.getItems();
        assertNotNull(items, "Podaci u tabeli ne smeju biti null.");
        assertFalse(items.isEmpty(), "Tabela kanala ne sme biti prazna pre ažuriranja.");
        assertTrue(items.size() >= 1, "Tabela mora imati barem jedan red za testiranje.");

        Channel channelToEdit = items.get(3);
        table2.getSelectionModel().select(channelToEdit);
        clickOn(table2).clickOn(table2);

        TextField nameField = lookup("#name").nth(0).query();
        TextField descriptionField = lookup("#description").nth(1).query();

        clickOn(nameField).eraseText(channelToEdit.getName().length()).write("novoIme");
        clickOn(descriptionField).eraseText(channelToEdit.getDescription().length()).write("noviOpis");

        Button editButton = lookup("#editDugme").query();
        clickOn(editButton);

        sleep(500);

        boolean found = items.stream()
                .anyMatch(channel -> channel.getName().equals("novoIme"));

        assertTrue(found, "Kanal nije uspešno ažuriran.");

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

        ComboBox<String> comboBox4 = lookup("#comboBox4").queryComboBox();
        clickOn(comboBox4);
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);

        ComboBox<String> comboBox5 = lookup("#comboBox5").queryComboBox();
        clickOn(comboBox5);
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);

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
