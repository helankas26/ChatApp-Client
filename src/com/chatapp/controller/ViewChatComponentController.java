/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.chatapp.controller;

import com.chatapp.client.Client;
import com.chatapp.pojos.Chat;
import com.chatapp.rmi.ChatRemote;
import java.io.ByteArrayInputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Helanka
 */
public class ViewChatComponentController implements Initializable {

    @FXML
    private ImageView imgChat;
    @FXML
    private Label lblName;
    @FXML
    private ImageView imgOnline;
    @FXML
    private ImageView imgOffline;
    @FXML
    private ImageView btnEnd;
    @FXML
    private ImageView btnStart;
    
    private AnchorPane adminDashboardContext;
    private ChatRemote chatRemote;
    private Chat chat;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        chatRemote = Client.getChatRemote();
    }    

    @FXML
    private void endChatOnAction(MouseEvent event) {
        try {
            Optional<ButtonType> result = alert(
                    Alert.AlertType.CONFIRMATION,
                    "Confirmation",
                    "Do you want to end this chat?",
                    chat.getName() + " [id: " + chat.getChatId() + "]"
            );
            
            if (result.isPresent() && result.get() == ButtonType.OK) {
                if (chatRemote.putOffline(chat)) {
                    btnEnd.setVisible(false);
                    imgOnline.setVisible(false);
                    btnStart.setVisible(true);
                    imgOffline.setVisible(true);
                }
            } else {
                event.consume();
            }
        } catch (RemoteException ex) {
            alert(Alert.AlertType.ERROR, "RemoteException", null, "Failed!");
        }
    }

    @FXML
    private void startChatOnAction(MouseEvent event) {
        try {
            if (chatRemote.isChatOnline()) {
                alert(Alert.AlertType.WARNING, "Already online", null, "Only one chat at a time!");
            } else {
                Optional<ButtonType> result = alert(
                        Alert.AlertType.CONFIRMATION,
                        "Confirmation",
                        "Do you want to start this chat?",
                        chat.getName() + " [id: " + chat.getChatId() + "]"
                );

                if (result.isPresent() && result.get() == ButtonType.OK) {
                    if (chatRemote.putOnline(chat)) {
                        btnStart.setVisible(false);
                        imgOffline.setVisible(false);
                        btnEnd.setVisible(true);
                        imgOnline.setVisible(true);
                    }
                } else {
                    event.consume();
                }
            }
        } catch (RemoteException ex) {
            alert(Alert.AlertType.ERROR, "RemoteException", null, "Failed!");
        }
    }
    
    public void setChatData(Chat chat) {
        this.chat = chat;
        ByteArrayInputStream bais = new ByteArrayInputStream(chat.getAvatar());
               
        imgChat.setImage(new Image(bais));
        lblName.setText(chat.getName());
        
        if (chat.getStatus() == 0) {
            btnEnd.setVisible(false);
            imgOnline.setVisible(false);
            btnStart.setVisible(true);
            imgOffline.setVisible(true);
            
        } else {
            btnStart.setVisible(false);
            imgOffline.setVisible(false);
            btnEnd.setVisible(true);
            imgOnline.setVisible(true);
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
    
    public void setContext(AnchorPane context) {
        adminDashboardContext = context;
    }
}
