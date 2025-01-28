import com.example.demo3.Controller.ApiClientUser;
import com.example.demo3.HelloApplication;
import com.example.demo3.HelloController;
import com.example.demo3.repository.MainRepository;
import com.example.demo3.Model.Person;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ApiClientUserTest extends ApplicationTest {

    private TableView<Person> table;
    private HelloController helloController;


    @Override
    public void start(Stage stage) throws Exception {
        HelloApplication helloApp = new HelloApplication();
        helloApp.start(stage);

        table = lookup("#table").queryTableView();
    }

    @Test
    public void testGetUsersAndPopulateTable() {
        try {
            List<Person> users = ApiClientUser.getUsers();

            assertNotNull(users, "Lista korisnika ne sme biti null.");
            assertFalse(users.isEmpty(), "Lista korisnika ne sme biti prazna.");

            ObservableList<Person> tableData = table.getItems();
            assertNotNull(tableData, "Podaci u tabeli ne smeju biti null.");
            assertEquals(users.size(), tableData.size(), "Broj korisnika u tabeli ne odgovara broju dobijenih korisnika.");

            for (int i = 0; i < users.size(); i++) {
                Person expected = users.get(i);
                Person actual = tableData.get(i);

                assertEquals(expected.getId(), actual.getId(), "ID korisnika se ne poklapa.");
                assertEquals(expected.getFirstName(), actual.getFirstName(), "Ime korisnika se ne poklapa.");
                assertEquals(expected.getLastName(), actual.getLastName(), "Prezime korisnika se ne poklapa.");
                assertEquals(expected.getUsername(), actual.getUsername(), "Korisničko ime se ne poklapa.");
                assertEquals(expected.getEmail(), actual.getEmail(), "Email se ne poklapa.");
                assertEquals(expected.getRole(), actual.getRole(), "Uloga korisnika se ne poklapa.");
            }
        } catch (IOException e) {
            fail("Izuzetak pri pozivu metode getUsers: " + e.getMessage());
        }
    }

    @Test
    public void testAddPerson() {
        try {

            ObservableList<Person> initialTableData = table.getItems();
            int initialSize = initialTableData.size();

            MainRepository.getInstance().put("firstName", "TestFirstName");
            MainRepository.getInstance().put("lastName", "TestLastName");
            MainRepository.getInstance().put("password", "TestUsername");
            MainRepository.getInstance().put("email", "testemail@example.com");
            MainRepository.getInstance().put("mac", "test-mac-address");

            helloController.addPerson();

            ObservableList<Person> updatedTableData = table.getItems();

            assertEquals(initialSize + 1, updatedTableData.size(), "Broj korisnika u tabeli se nije povećao.");

            Person lastPerson = updatedTableData.get(updatedTableData.size() - 1);
            assertNotNull(lastPerson, "Poslednji korisnik u tabeli ne sme biti null.");
            assertEquals("TestFirstName", lastPerson.getFirstName(), "Ime poslednjeg korisnika se ne poklapa.");
            assertEquals("TestLastName", lastPerson.getLastName(), "Prezime poslednjeg korisnika se ne poklapa.");
            assertEquals("TestUsername", lastPerson.getUsername(), "Korisničko ime poslednjeg korisnika se ne poklapa.");
            assertEquals("testemail@example.com", lastPerson.getEmail(), "Email poslednjeg korisnika se ne poklapa.");

        } catch (Exception e) {
            fail("Izuzetak prilikom testiranja metode addPerson: " + e.getMessage());
        }
    }
    @Test
    public void testDeletePerson() {
        try {
            // Kreiramo testni objekat bez ID-a
            Person testPerson = new Person("TestFirstName", "TestLastName", "TestUsername", "testemail@example.com", List.of("ADMIN"));

            ObservableList<Person> tableData = table.getItems();
            tableData.add(testPerson);
            table.getSelectionModel().select(testPerson);

            assertEquals(1, tableData.size(), "Tabela bi trebalo da sadrži tačno jednog korisnika.");
            assertEquals(testPerson, tableData.get(0), "Dodati korisnik nije isti kao očekivani.");

            helloController.deletePerson(table);

            assertTrue(tableData.isEmpty(), "Tabela bi trebalo da bude prazna nakon brisanja.");
        } catch (Exception e) {
            fail("Izuzetak prilikom testiranja metode deletePerson: " + e.getMessage());
        }
    }


}
