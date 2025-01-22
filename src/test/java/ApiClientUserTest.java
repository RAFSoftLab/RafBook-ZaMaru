import com.example.demo3.Controller.ApiClientUser;
import com.example.demo3.HelloApplication;
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
}
