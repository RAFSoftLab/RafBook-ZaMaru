package com.example.demo3;
import com.example.demo3.Controller.ApiChannel;
import com.example.demo3.Controller.ApiClientUser;
import com.example.demo3.Controller.AuthClient;
import com.example.demo3.Model.Channel;
import com.example.demo3.Model.NewChannelDTO;
import com.example.demo3.Model.NewUserDTO;
import com.example.demo3.Model.Person;
import com.example.demo3.repository.MainRepository;
import com.example.demo3.view.View;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import java.util.Arrays;
import java.util.Optional;

import static com.example.demo3.Controller.ApiClientUser.*;

public class HelloController {

    //for user
    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField roleField;

    @FXML
    private Button addButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button editButton;

    @FXML
    private TableView<Person> table;

    @FXML
    private TableColumn<Person, Integer> idColumn;

    @FXML
    private TableColumn<Person, String> firstNameColumn;

    @FXML
    private TableColumn<Person, String> lastNameColumn;

    @FXML
    private TableColumn<Person, String> usernameColumn;

    @FXML
    private TableColumn<Person, String> emailColumn;

    @FXML
    private TableColumn<Person, String> roleColumn;

    private ObservableList<Person> userData = FXCollections.observableArrayList();

    @FXML
    private TableView<Channel> table2;

    @FXML
    private TableColumn<Channel, String> nameColumn;

    @FXML
    private TableColumn<Channel, String> descriptionColumn;

    private ObservableList<Channel> channelData = FXCollections.observableArrayList();

    ApiChannel apiChannel = new ApiChannel();




    ApiClientUser apiClientUser = new ApiClientUser();
    ApiChannel apichannel=new ApiChannel();

    private void refreshUserData() {
        try {
            List<Person> users = ApiClientUser.getUsers();
            userData.clear();
            userData.addAll(users);
            table.refresh(); // Force table refresh
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Error loading users: " + e.getMessage());
            alert.show();
        }
    }
    private void refreshChannelData() {
        try {
            List<Channel> channels = ApiChannel.getChannels();
            channelData.clear();
            channelData.addAll(channels);
            table2.refresh(); // Change this from table to table2
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Error loading channels: " + e.getMessage());
            alert.show();
        }
    }

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        roleColumn.setCellValueFactory(cellData -> {
            List<String> roles = cellData.getValue().getRole();
            return new SimpleStringProperty(String.join(", ", roles));
        });


        table.setItems(userData);
        refreshUserData();

        addButton.setOnAction(event -> addPerson());
        deleteButton.setOnAction(event -> deletePerson(table));




        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        if (channelData == null) {
            channelData = FXCollections.observableArrayList();
        }

        table2.setItems(channelData);
        refreshChannelData();

    }

    public void setTable(TableView<Person> table) {
        this.table = table;
    }

    public void setTable2(TableView<Channel> table2) {
        this.table2 = table2;
    }

    public static boolean createUser(String firstName, String lastName, String username,
                                     String email, String role, String mac) throws IOException {
        // Create  NewUserDTO or object the API expects
        // Make the HTTP POST request to create the user
        // Return true if successful, false otherwise

        // Example implementation (adjust according to your API):
        URL url = new URL("your-api-endpoint/users");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", "Bearer " + AuthClient.getToken());
        conn.setDoOutput(true);

        String jsonInputString = String.format(
                "{\"firstName\": \"%s\", \"lastName\": \"%s\", \"username\": \"%s\", " +
                        "\"email\": \"%s\", \"role\": \"%s\", \"mac\": \"%s\"}",
                firstName, lastName, username, email, role, mac);

        try(OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        return conn.getResponseCode() == HttpURLConnection.HTTP_OK;
    }

    public static boolean createChannel(String name, String description) throws IOException {
        URL url = new URL("your-api-endpoint/text-channel");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", "Bearer " + AuthClient.getToken());
        conn.setDoOutput(true);

        String jsonInputString = String.format(
                "{\"name\": \"%s\", \"description\": \"%s\", \"type\": \"%s\"}",
                name, description);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        return conn.getResponseCode() == HttpURLConnection.HTTP_OK;
    }


    // Keep as void:
    public void addPerson() {
        try {
            String firstName = MainRepository.getInstance().get("firstName");
            String lastName = MainRepository.getInstance().get("lastName");
            String username = MainRepository.getInstance().get("password");
            String email = MainRepository.getInstance().get("email");
            String role = MainRepository.getInstance().get("role");
            String mac = MainRepository.getInstance().get("mac");

            NewUserDTO newUser = new NewUserDTO();
            newUser.setFirstName(firstName);
            newUser.setLastName(lastName);
            newUser.setUsername(username);
            newUser.setPassword(username);
            newUser.setEmail(email.toLowerCase());
            newUser.setRole("ADMIN");
            newUser.setMacAddress(mac);

            boolean success = ApiClientUser.addUser(newUser);

            if (success) {
                List<Person> users = ApiClientUser.getUsers();
                userData = FXCollections.observableArrayList(users);
                table.setItems(userData);

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "User successfully added!");
                alert.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Error adding person: " + e.getMessage());
            alert.show();
        }
    }



    public void deletePerson(TableView<Person> table) {
        Person selectedUser = table.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Morate selektovati korisnika za brisanje.");
            alert.show();
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION, "Da li ste sigurni da želite obrisati korisnika?");
        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isEmpty() || result.get() != ButtonType.OK) {
            return;
        }

        try {
            boolean isDeleted = apiClientUser.deleteUser(selectedUser);
            if (isDeleted) {
                // Refresh the table data from the server
                try {
                    List<Person> users = ApiClientUser.getUsers();
                    table.setItems(FXCollections.observableArrayList(users));
                    table.refresh();
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "Korisnik je uspešno obrisan.");
                    successAlert.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Brisanje nije uspelo. Proverite server.");
                errorAlert.show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Došlo je do greške prilikom komunikacije sa serverom.");
            errorAlert.show();
        }
    }

    public void editPerson() {
        try {
            // Preuzmi podatke iz MainRepository
            String firstName = MainRepository.getInstance().get("firstName");
            String lastName = MainRepository.getInstance().get("lastName");
            String username = MainRepository.getInstance().get("username");
            String email = MainRepository.getInstance().get("email");
            String role = MainRepository.getInstance().get("role");

            // Proveri da li je selektovan korisnik iz tabele
            Person selectedPerson = table.getSelectionModel().getSelectedItem();
            if (selectedPerson == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Nijedan korisnik nije selektovan za ažuriranje!");
                alert.show();
                return;
            }

            // Postavi ažurirane vrednosti na selektovanog korisnika
            selectedPerson.setFirstName(firstName);
            selectedPerson.setLastName(lastName);
            selectedPerson.setUsername(username);
            selectedPerson.setEmail(email);
            selectedPerson.setRole(Arrays.asList(role.split(", "))); // Pretvori string u listu

            // Pozovi API za ažuriranje korisnika
            boolean success = ApiClientUser.editUser(selectedPerson);
            if (success) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Korisnik uspešno ažuriran!");
                alert.show();

                // Osveži podatke u tabeli
                table.refresh();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Ažuriranje korisnika nije uspelo!");
                alert.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Došlo je do greške prilikom ažuriranja korisnika: " + e.getMessage());
            alert.show();
        }
    }






    public void addChannel() {
        try {
            String name = MainRepository.getInstance().get("name");
            String description = MainRepository.getInstance().get("description");

            NewChannelDTO newChannel = new NewChannelDTO();
            newChannel.setName(name);
            newChannel.setDescription(description);

            boolean success = ApiChannel.addChannel(newChannel);

            if (success) {
                System.out.println("Channel added successfully, refreshing table...");

                // Fetch updated channel list
                List<Channel> channels = ApiChannel.getChannels();
                System.out.println("Fetched " + channels.size() + " channels");

                // Clear existing data
                channelData.clear();

                // Add new data
                channelData = FXCollections.observableArrayList(channels);

                // Update table items
                if (table2 != null) {
                    table2.setItems(null); // Clear the items first
                    table2.setItems(channelData);
                    table2.refresh();
                    System.out.println("Table refreshed with " + table2.getItems().size() + " items");
                } else {
                    System.out.println("table2 is null!");
                }

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Channel successfully added!");
                alert.show();
            } else {
                System.out.println("Channel addition failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Error adding channel: " + e.getMessage());
            alert.show();
        }
    }

    public ObservableList<Channel> getChannelData() {
        return channelData;
    }


    public ObservableList<Person> getUserData() {
        return userData;
    }



}