package com.example.demo3;

import com.example.demo3.Controller.ApiChannel;
import com.example.demo3.Model.Channel;
import com.example.demo3.Model.NewChannelDTO;
import com.example.demo3.repository.MainRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.List;

public class HelloControllerChannel{

    // Table attributes for channels
    @FXML
    private TableView<Channel> table;

    @FXML
    private TableColumn<Channel, String> nameColumn;

    @FXML
    private TableColumn<Channel, String> descriptionColumn;

    private ObservableList<Channel> channelData = FXCollections.observableArrayList();

    ApiChannel apiChannel = new ApiChannel();

    private void refreshChannelData() {
        try {
            List<Channel> channels = ApiChannel.getChannels();
            channelData.clear();
            channelData.addAll(channels);
            table.refresh(); // Force table refresh
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Error loading channels: " + e.getMessage());
            alert.show();
        }
    }

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        table.setItems(channelData);
        refreshChannelData();
    }

    public void setTable(TableView<Channel> table) {
        this.table = table;
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
                List<Channel> channels = ApiChannel.getChannels();
                channelData = FXCollections.observableArrayList(channels);
                table.setItems(channelData);

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Channel successfully added!");
                alert.show();
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
}
