import com.example.demo3.Controller.ApiChannel;
import com.example.demo3.HelloApplication;
import com.example.demo3.Model.Channel;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ApiChannelTest extends ApplicationTest {

    private TableView<Channel> table;

    @Override
    public void start(Stage stage) throws Exception {
        HelloApplication helloApp = new HelloApplication();
        helloApp.start(stage);

        // Pronađi tabelu u GUI-ju
        table = lookup("table2").queryTableView();
    }

    @Test
    public void testGetChannelsAndPopulateTable() {
        try {
            List<Channel> channels = ApiChannel.getChannels();

            assertNotNull(channels, "Lista kanala ne sme biti null.");
            assertFalse(channels.isEmpty(), "Lista kanala ne sme biti prazna.");

            ObservableList<Channel> tableData = table.getItems();
            assertNotNull(tableData, "Podaci u tabeli ne smeju biti null.");
            assertEquals(channels.size(), tableData.size(), "Broj kanala u tabeli ne odgovara broju dobijenih kanala.");

            for (int i = 0; i < channels.size(); i++) {
                Channel expected = channels.get(i);
                Channel actual = tableData.get(i);

                assertEquals(expected.getId(), actual.getId(), "ID kanala se ne poklapa.");
                assertEquals(expected.getName(), actual.getName(), "Ime kanala se ne poklapa.");
                assertEquals(expected.getDescription(), actual.getDescription(), "Opis kanala se ne poklapa.");
            }
        } catch (IOException e) {
            fail("Izuzetak pri pozivu metode getChannels: " + e.getMessage());
        }
    }
}

