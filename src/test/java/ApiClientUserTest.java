import com.example.demo3.HelloApplication;
import com.example.demo3.Model.Person;
import com.example.demo3.view.UserView;
import com.example.demo3.Controller.AuthClient;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import static org.junit.jupiter.api.Assertions.*;

public class ApiClientUserTest extends ApplicationTest {

    private UserView userView;
    private TableView<Person> table;

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

        TableView<Person> table = (TableView<Person>) lookup("#table").query();
        assertNotNull(table, "Tabela nije pronađena.");

        clickOn("Pretraga:").write("Ivana Jankovic");

        ObservableList<Person> items = table.getItems();
        boolean found = items.stream().anyMatch(user ->
                user.getFirstName().contains("Ivana") || user.getLastName().contains("Jankovic"));

        assertTrue(found, "Pretraga nije pronašla korisnika sa imenom 'Ivana Jankovic'.");
    }

    @Test
    public void testTableIsNotEmpty() {
        TextField usernameField = lookup("#usernameField").query();
        PasswordField passwordField = lookup("#passwordField").query();
        Button loginButton = lookup("#loginButton").queryButton();

        clickOn(usernameField).write("mara");
        clickOn(passwordField).write("mara123");

        clickOn(loginButton);

        TableView<Person> table = (TableView<Person>) lookup("#table").query();

        assertNotNull(table, "Tabela nije pronađena.");

        ObservableList<Person> items = table.getItems();

        assertNotNull(items, "Podaci u tabeli ne smeju biti null.");
        assertFalse(items.isEmpty(), "Tabela korisnika ne sme biti prazna.");
    }
//    @Test
//    public void testAddUser() {
//        TextField usernameField = lookup("#usernameField").query();
//        PasswordField passwordField = lookup("#passwordField").query();
//        Button loginButton = lookup("#loginButton").queryButton();
//
//        clickOn(usernameField).write("mara");
//        clickOn(passwordField).write("mara123");
//
//        clickOn(loginButton);
//
//        TextField firstNameField = (TextField) lookup("#firstNameField").query();
//        TextField lastNameField = (TextField) lookup("#lastNameField").query();
//        TextField usernameField2 = (TextField) lookup("#username2").query();
//        TextField emailField = (TextField) lookup("#email").query();
//
//        clickOn(firstNameField).write("Ivana");
//        clickOn(lastNameField).write("Jankovic");
//        clickOn(usernameField2).write("ivana123");
//        clickOn(emailField).write("ivana@example.com");
//
//        clickOn("Dodaj");
//
//        TableView<Person> table = (TableView<Person>) lookup("#table").query();
//        assertNotNull(table, "Tabela nije pronađena.");
//
//        ObservableList<Person> items = table.getItems();
//        assertNotNull(items, "Podaci u tabeli ne smeju biti null.");
//        assertTrue(items.size() > 0, "Tabela ne sadrži nove korisnike.");
//
//        boolean found = items.stream().anyMatch(user ->
//                user.getFirstName().equals("Ivana") &&
//                        user.getLastName().equals("Jankovic") &&
//                        user.getUsername().equals("ivana123") &&
//                        user.getEmail().equals("ivana@example.com"));
//
//        assertTrue(found, "Novi korisnik nije dodat u tabelu.");
//    }

    @Test
    public void testDeleteUser() {
        // Prijava na sistem
        TextField usernameField = lookup("#usernameField").query();
        PasswordField passwordField = lookup("#passwordField").query();
        Button loginButton = lookup("#loginButton").queryButton();

        clickOn(usernameField).write("mara");
        clickOn(passwordField).write("mara123");
        clickOn(loginButton);

        // Provera da li tabela postoji i sadrži podatke
        TableView<Person> table = lookup("#table").query();
        assertNotNull(table, "Tabela nije pronađena.");

        ObservableList<Person> items = table.getItems();
        assertNotNull(items, "Podaci u tabeli ne smeju biti null.");
        assertFalse(items.isEmpty(), "Tabela ne sme biti prazna pre brisanja.");

        // Odabir korisnika za brisanje (prvi korisnik u tabeli)
        Person userToDelete = items.get(0);

        // Simulacija klika na red u tabeli (ispravljena verzija)
        Node firstRow = lookup(".table-row-cell").nth(0).query();
        clickOn(firstRow);

        // Čekanje da GUI ažurira selekciju
        sleep(500);

        // Provera da je korisnik selektovan
        assertEquals(userToDelete, table.getSelectionModel().getSelectedItem(), "Korisnik nije selektovan.");

        // Klik na dugme za brisanje
        Button deleteButton = lookup("#deleteButton").queryButton();
        clickOn(deleteButton);

        // Pauza kako bi GUI imao vremena da osveži tabelu
        sleep(500);

        // Ažuriranje liste korisnika u tabeli
        items = table.getItems();
        boolean found = items.stream().anyMatch(user -> user.equals(userToDelete));

        // Provera da li je korisnik uklonjen iz tabele
        assertFalse(found, "Korisnik nije obrisan iz tabele.");
    }



//    @Test
//    public void testEditUser() {
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
//        TableView<Person> table = (TableView<Person>) lookup("#table").query();
//        assertNotNull(table, "Tabela nije pronađena.");
//
//        ObservableList<Person> items = table.getItems();
//        assertNotNull(items, "Podaci u tabeli ne smeju biti null.");
//        assertFalse(items.isEmpty(), "Tabela ne sme biti prazna pre ažuriranja.");
//
//        Person userToEdit = items.get(0);
//        table.getSelectionModel().select(userToEdit);
//
//        TextField firstNameField = (TextField) lookup("#firstNameField").query();
//        TextField lastNameField = (TextField) lookup("#lastNameField").query();
//        TextField usernameField2 = (TextField) lookup("#username2").query();
//        TextField emailField = (TextField) lookup("#email").query();
//        TextField roleField = (TextField) lookup("#roleField").query();
//
//        clickOn(firstNameField).eraseText(userToEdit.getFirstName().length()).write("NovoIme");
//        clickOn(lastNameField).eraseText(userToEdit.getLastName().length()).write("NoviPrezime");
//        clickOn(usernameField2).eraseText(userToEdit.getUsername().length()).write("noviUsername");
//        clickOn(emailField).eraseText(userToEdit.getEmail().length()).write("novi@example.com");
//        clickOn(roleField).eraseText(userToEdit.getRole().size()).write("Admin");
//
//        Button editButton = (Button) lookup("#editButton").query();
//        clickOn(editButton);
//
//        sleep(500);
//
//        boolean found = items.stream().anyMatch(user ->
//                user.getFirstName().equals("NovoIme") &&
//                        user.getLastName().equals("NoviPrezime") &&
//                        user.getUsername().equals("noviUsername") &&
//                        user.getEmail().equals("novi@example.com"));
//
//        assertTrue(found, "Korisnik nije uspešno ažuriran.");
//    }
}
