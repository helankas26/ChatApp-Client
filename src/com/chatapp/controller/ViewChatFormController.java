/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.chatapp.controller;

import com.chatapp.client.Client;
import com.chatapp.pojos.Chat;
import com.chatapp.rmi.ChatRemote;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Helanka
 */
public class ViewChatFormController implements Initializable {

    @FXML
    private JFXTextField txtSearch;
    @FXML
    private GridPane gridPaneChat;
    
    private static AnchorPane adminDashboardContext;
    private ChatRemote chatRemote;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        chatRemote = Client.getChatRemote();
        
        setGridPaneChats("");
        
        txtSearch.textProperty()
               .addListener((observable, oldValue, newValue) -> {
                   setGridPaneChats(newValue);
               });
    }
    
    @FXML
    private void putChatOfflineOnAction(ActionEvent event) {
        try {
            if (chatRemote.isChatOnline()) {
                Optional<ButtonType> result = alert(
                        Alert.AlertType.CONFIRMATION,
                        "Confirmation",
                        null,
                        "Do you want to put active chat offline?"
                );

                if (result.isPresent() && result.get() == ButtonType.OK) {
                    if (chatRemote.putAllOffline()) {
                        txtSearch.clear();
                        setGridPaneChats("");
                    }
                } else {
                    event.consume();
                }
            } else {
                alert(Alert.AlertType.INFORMATION, "Already Offline", null, "All chats are offline!");
            }
        } catch (RemoteException ex) {
            alert(Alert.AlertType.ERROR, "RemoteException", null, "Failed!");
        }
    }
    
    private void setGridPaneChats(String search) {
        gridPaneChat.getChildren().clear();
        search = search.toLowerCase();
        int column = 0, row = 1;
         
        try {
            for (Chat chat : chatRemote.getAllChat()) {
                if (chat.getName().toLowerCase().contains(search) || chat.getDescription().toLowerCase().contains(search)) {
                    FXMLLoader loder = new FXMLLoader(getClass().getResource("../view/ViewChatComponent.fxml"));
                    HBox hBoxChat = loder.load();

                    ViewChatComponentController chatComponentController = loder.getController();
                    chatComponentController.setChatData(chat);
                    chatComponentController.setContext(adminDashboardContext);

                    if (column == 2) {
                        column = 0;
                        row++;
                    }

                    gridPaneChat.add(hBoxChat, column++, row);
                    GridPane.setMargin(hBoxChat, new Insets(5));
                }
            }
        } catch (RemoteException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    private Optional<ButtonType> alert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Optional<ButtonType> result = null;
        
        try {
            Stage parentStage = (Stage) adminDashboardContext.getScene().getWindow();
            Alert alert = new Alert(alertType);
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            String iconPath = getClass().getResource("../asserts/view_chat.png").toURI().toString();
            stage.getIcons().add(new Image(iconPath));
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.initOwner(parentStage);
            alert.setTitle(title);
            alert.setHeaderText(headerText);
            alert.setContentText(contentText);
            
            result = alert.showAndWait();
        } catch (URISyntaxException ex) {
            System.out.println(ex.getMessage());
        }
        return result;
    }
    
    public static void setContext(AnchorPane context) {
        adminDashboardContext = context;
    }
}
