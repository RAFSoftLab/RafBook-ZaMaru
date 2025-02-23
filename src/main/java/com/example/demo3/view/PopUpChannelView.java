package com.example.demo3.view;
import com.example.demo3.Controller.ApiChannel;
import com.example.demo3.HelloController;
import com.example.demo3.Model.Channel;
import com.example.demo3.Model.RolePermissionDTO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class PopUpChannelView {


    TableView<String> tableView = new TableView<>();
    TableView<RolePermissionDTO> rolesTableView = new TableView<>();
    private boolean isColumnAdded = false;
    HelloController helloController = new HelloController();
    int channelId;
    private TableView<Channel> mainTable;

    private ObservableList<Channel> channelData;
    private FilteredList<Channel> filteredData;

    public void setMainTableAndData(TableView<Channel> table,
                                    ObservableList<Channel> channelData,
                                    FilteredList<Channel> filteredData) {
        this.mainTable = table;
        this.channelData = channelData;
        this.filteredData = filteredData;
    }

    private void refreshTables() {
        try {
            List<Channel> updatedChannels = ApiChannel.getChannels();

            channelData.clear();
            channelData.addAll(updatedChannels);

            Channel selectedChannel = null;
            for (Channel channel : updatedChannels) {
                if (channel.getId() == channelId) {
                    selectedChannel = channel;
                    break;
                }
            }

            if (selectedChannel != null) {
                updateRolesTable(selectedChannel.getRolePermissionDTOList());
            }

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Greška pri osvežavanju podataka: " + e.getMessage());
            alert.show();
        }
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
        System.out.println("Postavljen ID kanala: " + this.channelId);
    }

    public int getChannelId() {
        return this.channelId;
    }

    public void showPopupWindow() {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Uloge kanala");
        Image logoImage = new Image(getClass().getResource("/images/raf.jpg").toExternalForm());
        ImageView logoImageView = new ImageView(logoImage);
        Image icon = new Image(getClass().getResource("/images/raf.jpg").toExternalForm());
        popupStage.getIcons().add(icon);

        TableColumn<RolePermissionDTO, String> roleColumn = new TableColumn<>("Uloga");
        roleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRole()));

        rolesTableView.getColumns().add(roleColumn);
        rolesTableView.setId("rolesTableView");

        TextField inputField = new TextField();
        inputField.setPromptText("Upisite ulogu...");
        inputField.setId("roleField2");

        Button btnAddRole = new Button("Dodaj novu ulogu");
        btnAddRole.setId("addRole2");

        btnAddRole.setOnAction(e -> {
            helloController.addRoleToChannel(tableView, inputField,channelId);
            refreshTables();
        });

        btnAddRole.setStyle("-fx-background-color:white;-fx-text-fill:#173669;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:#173669;-fx-border-radius:10px;-fx-background-radius:10px");
        btnAddRole.setOnMouseEntered(e -> {
            btnAddRole.setStyle("-fx-background-color:#173669;-fx-text-fill:white;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:white;-fx-border-radius:10px;-fx-background-radius:10px");
        });

        btnAddRole.setOnMouseExited(e -> {
            btnAddRole.setStyle("-fx-background-color:white;-fx-text-fill:#173669;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:#173669;-fx-border-radius:10px;-fx-background-radius:10px");
        });


        Button btnDeleteRole = new Button("Obrisi ulogu");
        btnDeleteRole.setId("deleteRole2");

        btnDeleteRole.setStyle("-fx-background-color:white;-fx-text-fill:#173669;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:#173669;-fx-border-radius:10px;-fx-background-radius:10px");
        btnDeleteRole.setOnMouseEntered(e -> {
            btnDeleteRole.setStyle("-fx-background-color:#173669;-fx-text-fill:white;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:white;-fx-border-radius:10px;-fx-background-radius:10px");
        });

        btnDeleteRole.setOnMouseExited(e -> {
            btnDeleteRole.setStyle("-fx-background-color:white;-fx-text-fill:#173669;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:#173669;-fx-border-radius:10px;-fx-background-radius:10px");
        });
        btnDeleteRole.setOnAction(e -> {
            helloController.removeRoleFromChannel(rolesTableView,channelId);
        });



        VBox controlsLayout = new VBox(10, inputField, btnAddRole);
        VBox popupLayout = new VBox(10, rolesTableView, controlsLayout, btnDeleteRole);
        popupLayout.setStyle("-fx-padding: 20;");
        popupLayout.setStyle("-fx-background-color:white;");

        Scene popupScene = new Scene(popupLayout, 400, 400);
        popupStage.setScene(popupScene);

        popupStage.showAndWait();
    }

    public void updateRolesTable(List<RolePermissionDTO> roles) {
        ObservableList<RolePermissionDTO> observableRoles = FXCollections.observableArrayList(roles);
        rolesTableView.setItems(observableRoles);
    }
}
