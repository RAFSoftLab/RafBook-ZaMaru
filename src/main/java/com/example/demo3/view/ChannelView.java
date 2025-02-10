package com.example.demo3.view;

import com.example.demo3.HelloController;
import com.example.demo3.Model.Channel;
import com.example.demo3.Model.Person;
import com.example.demo3.Model.RolePermissionDTO;
import com.example.demo3.repository.MainRepository;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.demo3.Controller.ApiChannel.getChannels;

public class ChannelView {
    HelloController helloController=new HelloController();
    PopUpChannelView popUpChannelView=new PopUpChannelView();




    public VBox createChannelTabContent() {
        TableView<Channel> table2 = new TableView<>();
        table2.setId("table2");

        ObservableList<Channel> channelData = FXCollections.observableArrayList();
        FilteredList<Channel> filteredData = new FilteredList<>(channelData, p -> true);

        try {
            List<Channel> channels = getChannels();
            channelData.addAll(channels);
        } catch (IOException e) {
            e.printStackTrace();
        }

        table2.setItems(filteredData);

        TableColumn<Channel, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Channel, String> nameColumn = new TableColumn<>("Naziv");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Channel, String> descriptionColumn = new TableColumn<>("Opis");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableColumn<Channel, String> roleColumn = new TableColumn<>("Uloge");
        roleColumn.setCellValueFactory(cellData -> {
            List<RolePermissionDTO> roles = cellData.getValue().getRolePermissionDTOList();
            String rolesAsString = roles.stream()
                    .map(RolePermissionDTO::getRole) // Uzimamo samo nazive uloga
                    .collect(Collectors.joining(", ")); // Spajamo u jedan string sa zarezima
            return new SimpleStringProperty(rolesAsString);
        });

        table2.getColumns().addAll(idColumn, nameColumn, descriptionColumn,roleColumn);

        helloController.setTable2(table2);

        popUpChannelView.setMainTableAndData(table2,channelData,filteredData);

        table2.getSelectionModel().selectedItemProperty().addListener((observable, oldSelection, newSelection) -> {
            if (newSelection != null) {
                int selectedChannelId = newSelection.getId(); // Uzima se ID selektovanog kanala
                List<RolePermissionDTO> roles = newSelection.getRolePermissionDTOList(); // Uzima se lista uloga kanala

                System.out.println("Uloge selektovanog kanala: " + roles);

                if (popUpChannelView != null) {
                    // Ažuriranje tabele u pop-up prozoru sa ulogama selektovanog kanala
                    popUpChannelView.updateRolesTable(roles);

                    // Postavljanje ID-ja selektovanog kanala
                    popUpChannelView.setChannelId(selectedChannelId);
                }
            } else {
                System.out.println("Nijedan kanal nije selektovan.");
                if (popUpChannelView != null) {
                    // Ako nije selektovan kanal, očisti tabelu uloga u pop-up prozoru
                    popUpChannelView.updateRolesTable(Collections.emptyList());
                }
            }
        });

        Label pretraga = new Label("Pretraga:");

        TextField searchField = new TextField();
        searchField.setPromptText("Pretraži kanale...");
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(channel -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                return channel.getName().toLowerCase().contains(lowerCaseFilter) ||
                        channel.getDescription().toLowerCase().contains(lowerCaseFilter);
            });
        });

        table2.setItems(filteredData);



        TextField nameField = new TextField();
        nameField.setPromptText("Naziv");
        nameField.setId("name");

        TextField descriptionField = new TextField();
        descriptionField.setPromptText("Opis");
        descriptionField.setId("description");

        Button addButton2 = new Button("Dodaj");
        addButton2.setId("addButton2");
        addButton2.setStyle("-fx-background-color:white;-fx-text-fill:#173669;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:#173669;-fx-border-radius:10px;-fx-background-radius:10px");
        addButton2.setOnMouseEntered(e -> {
            addButton2.setStyle("-fx-background-color:#173669;-fx-text-fill:white;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:white;-fx-border-radius:10px;-fx-background-radius:10px");
        });
        addButton2.setOnMouseExited(e -> {
            addButton2.setStyle("-fx-background-color:white;-fx-text-fill:#173669;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:#173669;-fx-border-radius:10px;-fx-background-radius:10px");
        });
        addButton2.setOnAction(e -> {
            if (nameField.getText().isEmpty() || descriptionField.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Sva polja moraju biti popunjena");
                alert.show();
                return;
            }

            MainRepository.getInstance().put("name", nameField.getText());
            MainRepository.getInstance().put("description", descriptionField.getText());

            helloController.addChannel();
            nameField.clear();
            descriptionField.clear();
        });

        Button deleteButton = new Button("Obriši");
        deleteButton.setStyle("-fx-background-color:white;-fx-text-fill:#173669;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:#173669;-fx-border-radius:10px;-fx-background-radius:10px");
        deleteButton.setOnMouseEntered(e -> {
            deleteButton.setStyle("-fx-background-color:#173669;-fx-text-fill:white;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:white;-fx-border-radius:10px;-fx-background-radius:10px");
        });

        deleteButton.setOnMouseExited(e -> {
            deleteButton.setStyle("-fx-background-color:white;-fx-text-fill:#173669;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:#173669;-fx-border-radius:10px;-fx-background-radius:10px");
        });
        Button editButton = new Button("Izmeni");
        editButton.setStyle("-fx-background-color:white;-fx-text-fill:#173669;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:#173669;-fx-border-radius:10px;-fx-background-radius:10px");
        editButton.setOnMouseEntered(e -> {
            editButton.setStyle("-fx-background-color:#173669;-fx-text-fill:white;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:white;-fx-border-radius:10px;-fx-background-radius:10px");
        });

        editButton.setOnMouseExited(e -> {
            editButton.setStyle("-fx-background-color:white;-fx-text-fill:#173669;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:#173669;-fx-border-radius:10px;-fx-background-radius:10px");
        });

        Button roleButton2=new Button("Uloge");
        roleButton2.setId("roleButton2");
        roleButton2.setStyle("-fx-background-color:white;-fx-text-fill:#173669;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:#173669;-fx-border-radius:10px;-fx-background-radius:10px");
        roleButton2.setOnMouseEntered(e -> {
            roleButton2.setStyle("-fx-background-color:#173669;-fx-text-fill:white;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:white;-fx-border-radius:10px;-fx-background-radius:10px");
        });

        roleButton2.setOnMouseExited(e -> {
            roleButton2.setStyle("-fx-background-color:white;-fx-text-fill:#173669;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:#173669;-fx-border-radius:10px;-fx-background-radius:10px");
        });
        roleButton2.setOnAction(e -> popUpChannelView.showPopupWindow());

        Image image4 = new Image(getClass().getResource("/images/raf3.png").toExternalForm());
        ImageView image5 = new ImageView(image4);
        image5.setFitWidth(200);
        image5.setPreserveRatio(true);

        HBox inputLayout = new HBox(10,nameField, descriptionField, addButton2, editButton, deleteButton,roleButton2);
        VBox vbox = new VBox(10, pretraga, searchField, table2, inputLayout, image5);
        vbox.setStyle("-fx-background-color:white");
        return vbox;

    }
}
