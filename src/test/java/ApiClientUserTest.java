import com.example.demo3.Controller.ApiChannel;
import com.example.demo3.Controller.ApiClientUser;
import com.example.demo3.HelloApplication;
import com.example.demo3.HelloController;
import com.example.demo3.Model.NewUserDTO;
import com.example.demo3.Model.Person;
import com.example.demo3.view.UserView;
import com.example.demo3.Controller.AuthClient;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testfx.framework.junit5.ApplicationTest;

import static org.mockito.ArgumentMatchers.*;

import javax.management.relation.Role;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ApiClientUserTest extends ApplicationTest {


    private TableView<Person> table;

    @Override
    public void start(Stage stage) throws Exception {
        HelloApplication helloApp = new HelloApplication();
        helloApp.start(stage);
    }


    @Test
    public void testAddUser() {
        TextField usernameField = lookup("#usernameField").query();
        PasswordField passwordField = lookup("#passwordField").query();
        Button loginButton = lookup("#loginButton").queryButton();

        clickOn(usernameField).write("mara");
        clickOn(passwordField).write("mara123");

        clickOn(loginButton);

        TextField firstNameField = (TextField) lookup("#firstNameField").query();
        TextField lastNameField = (TextField) lookup("#lastNameField").query();
        TextField usernameField2 = (TextField) lookup("#username2").query();
        TextField emailField = (TextField) lookup("#email").query();

        clickOn(firstNameField).write("Ivana");
        clickOn(lastNameField).write("Jankovic");
        clickOn(usernameField2).write("ivana123");
        clickOn(emailField).write("ivana@example.com");

        Button addButton = lookup("#addButton").queryButton();
        clickOn(addButton);

        TableView<Person> table = (TableView<Person>) lookup("#table").query();
        assertNotNull(table, "Tabela nije pronađena.");

        ObservableList<Person> items = table.getItems();
        assertNotNull(items, "Podaci u tabeli ne smeju biti null.");
        assertTrue(items.size() > 0, "Tabela ne sadrži nove korisnike.");

    }

//    @Test
//    public void testDeleteUser() {
//
//        TextField usernameField = lookup("#usernameField").query();
//        PasswordField passwordField = lookup("#passwordField").query();
//        Button loginButton = lookup("#loginButton").queryButton();
//
//        clickOn(usernameField).write("mara");
//        clickOn(passwordField).write("mara123");
//        clickOn(loginButton);
//
//        TableView<Person> table = lookup("#table").query();
//        assertNotNull(table, "Tabela nije pronađena.");
//
//        ObservableList<Person> items = table.getItems();
//        assertNotNull(items, "Podaci u tabeli ne smeju biti null.");
//        assertFalse(items.isEmpty(), "Tabela ne sme biti prazna pre brisanja.");
//        assertTrue(items.size() >= 2, "Tabela mora imati najmanje dva reda za testiranje.");
//
//        Person userToDelete = items.get(1);
//        table.getSelectionModel().select(1);
//
//        sleep(500);
//
//        assertEquals(userToDelete, table.getSelectionModel().getSelectedItem(), "Korisnik nije selektovan.");
//
//        Button deleteButton = lookup("#deleteButton").queryButton();
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
//        items = table.getItems();
//        boolean found = items.stream().anyMatch(user -> user.equals(userToDelete));
//
//        assertFalse(found, "Korisnik nije obrisan iz tabele.");
//    }


    @Test
    public void testEditUser() {

        TextField usernameField = lookup("#usernameField").query();
        PasswordField passwordField = lookup("#passwordField").query();
        Button loginButton = lookup("#loginButton").queryButton();

        clickOn(usernameField).write("mara");
        clickOn(passwordField).write("mara123");

        clickOn(loginButton);

        TableView<Person> table = lookup("#table").query();
        assertNotNull(table, "Tabela nije pronađena.");

        ObservableList<Person> items = table.getItems();
        assertNotNull(items, "Podaci u tabeli ne smeju biti null.");
        assertFalse(items.isEmpty(), "Tabela ne sme biti prazna pre ažuriranja.");
        assertTrue(items.size() >= 5, "Tabela mora imati najmanje šest redova za testiranje.");

        Person userToEdit = items.get(3);
        table.getSelectionModel().select(userToEdit);


        clickOn(table).clickOn(table);

        TextField usernameField2 = lookup("#username2").query();
        clickOn(usernameField2).eraseText(userToEdit.getUsername().length()).write("noviUserName");

        Button editButton = lookup("#editButton").query();
        clickOn(editButton);

        sleep(500);

        boolean found = items.stream().anyMatch(user -> user.getUsername().equals("noviUserName"));

        assertTrue(found, "Korisnik nije uspešno ažuriran.");
    }


    @Test
    public void testAddRole() {

        TextField usernameField = lookup("#usernameField").query();
        PasswordField passwordField = lookup("#passwordField").query();
        Button loginButton = lookup("#loginButton").queryButton();

        clickOn(usernameField).write("mara");
        clickOn(passwordField).write("mara123");
        clickOn(loginButton);

        TableView<Person> table = lookup("#table").query();
        assertNotNull(table, "Tabela nije pronađena.");

        ObservableList<Person> items = table.getItems();
        assertNotNull(items, "Podaci u tabeli ne smeju biti null.");
        assertFalse(items.isEmpty(), "Tabela ne sme biti prazna.");

        assertTrue(items.size() >= 2, "Tabela mora imati najmanje dva reda za testiranje.");

        Person userToEdit = items.get(1);
        table.getSelectionModel().select(1);

        sleep(500);
        assertEquals(userToEdit, table.getSelectionModel().getSelectedItem(), "Korisnik nije selektovan.");

        Button roleButton = lookup("#roleButton").queryButton();
        clickOn(roleButton);

        sleep(500);

        TextField roleField = lookup("#roleField").query();
        clickOn(roleField).eraseText(roleField.getText().length()).write("ADMIN");

        Button addRoleButton = lookup("#addRole").queryButton();
        clickOn(addRoleButton);

        sleep(500);
    }



    @Test
    public void testDeleteRole() {

        TextField usernameField = lookup("#usernameField").query();
        PasswordField passwordField = lookup("#passwordField").query();
        Button loginButton = lookup("#loginButton").queryButton();

        clickOn(usernameField).write("mara");
        clickOn(passwordField).write("mara123");

        clickOn(loginButton);

        TableView<Person> table = lookup("#table").query();
        assertNotNull(table, "Tabela nije pronađena.");

        ObservableList<Person> items = table.getItems();
        assertNotNull(items, "Podaci u tabeli ne smeju biti null.");
        assertFalse(items.isEmpty(), "Tabela ne sme biti prazna pre brisanja uloge.");

        assertTrue(items.size() >= 1, "Tabela mora imati najmanje jedan red za testiranje.");

        Person userToEdit = items.get(2);
        table.getSelectionModel().select(2);

        sleep(500);

        assertEquals(userToEdit, table.getSelectionModel().getSelectedItem(), "Korisnik nije selektovan.");

        Button roleButton = lookup("#roleButton").queryButton();
        clickOn(roleButton);

        sleep(500);

        TableView<Role> popUpTable = lookup("#popUpTable").query();
        assertNotNull(popUpTable, "Pop-up tabela nije pronađena.");

        ObservableList<Role> popUpItems = popUpTable.getItems();
        assertNotNull(popUpItems, "Podaci u pop-up tabeli ne smeju biti null.");
        assertFalse(popUpItems.isEmpty(), "Pop-up tabela ne sme biti prazna pre brisanja uloge.");

        assertTrue(popUpItems.size() >= 1, "Pop-up tabela mora imati najmanje jedan red za testiranje.");

        popUpTable.getSelectionModel().select(0);

        sleep(500);

        assertEquals(popUpItems.get(0), popUpTable.getSelectionModel().getSelectedItem(), "Uloga nije selektovana.");

        Button deleteRoleButton = lookup("#deleteRole").queryButton();
        clickOn(deleteRoleButton);

        sleep(500);
    }




}
