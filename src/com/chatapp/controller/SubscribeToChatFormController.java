/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.chatapp.controller;

import com.chatapp.client.Client;
import com.chatapp.pojos.Chat;
import com.chatapp.pojos.Subscription;
import com.chatapp.pojos.SubscriptionId;
import com.chatapp.pojos.User;
import com.chatapp.rmi.ChatRemote;
import com.chatapp.rmi.SubscriptionRemote;
import com.chatapp.rmi.UserRemote;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.rmi.RemoteException;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Helanka
 */
public class SubscribeToChatFormController implements Initializable {

    @FXML
    private JFXComboBox<Chat> cmbChatList;
    @FXML
    private HBox vChat;
    @FXML
    private Circle chatIcon;
    @FXML
    private Label lblName;
    @FXML
    private Label lblDescription;
    @FXML
    private JFXListView<Label> listViewAvailableUser;
    @FXML
    private JFXListView<Label> listViewAlreadyUser;
    @FXML
    private Label lblAddUser;
    @FXML
    private Label lblRemoveUser;
    
    private static AnchorPane adminDashboardContext;
    private ChatRemote chatRemote;
    private UserRemote userRemote;
    private SubscriptionRemote subscriptionRemote;
    private Chat chat;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        chatRemote = Client.getChatRemote();
        userRemote = Client.getUserRemote();
        subscriptionRemote = Client.getSubscriptionRemote();
        
        setCmbChatList();
        vChat.setVisible(false);
        disableFields();
    }    

    @FXML
    private void cmbChatListOnAction(ActionEvent event) {
        if (cmbChatList.getValue() != null) {
            chat = cmbChatList.getSelectionModel().getSelectedItem();
            
            ByteArrayInputStream bais = new ByteArrayInputStream(chat.getAvatar());
        
            chatIcon.setFill(new ImagePattern(new Image(bais)));
            lblName.setText(chat.getName());
            lblDescription.setText(chat.getDescription());
            vChat.setVisible(true);
            
            setListViewAvailableUser();
            setListViewAlreadyUser();
            
            lblAddUser.setDisable(false);
            listViewAlreadyUser.setDisable(false);
            lblRemoveUser.setDisable(false);
            listViewAvailableUser.setDisable(false);
        }
    }
    
    @FXML
    private void chatCloseOnAction(MouseEvent event) {
        lblName.setText("");
        lblDescription.setText("");
        chatIcon.setFill(null);
        vChat.setVisible(false);
        cmbChatList.getSelectionModel().clearSelection();
        
        disableFields();
        
        chat = null;
    }
    
    private void disableFields() {
        listViewAvailableUser.getItems().clear();
        listViewAlreadyUser.getItems().clear();
        
        lblAddUser.setDisable(true);
        listViewAlreadyUser.setDisable(true);
        lblRemoveUser.setDisable(true);
        listViewAvailableUser.setDisable(true);
    }
    
    private void setCmbChatList() {
        try {
            ObservableList<Chat> chatList = FXCollections.observableArrayList(chatRemote.getAllChat());
            cmbChatList.setItems(chatList);
        } catch (RemoteException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    private void setListViewAvailableUser() {
        listViewAvailableUser.getItems().clear();
        
        try {
            for (User user : userRemote.getToSubscribeUsers(chat)) {
                FXMLLoader loder = new FXMLLoader(getClass().getResource("../view/SubscribeAvailableComponent.fxml"));
                Label lblUser = loder.load();

                lblUser.setOnMouseClicked((event) -> {
                    event.consume();
                    subscribe(user);
                });
                
                SubscribeAvailableComponentController componentController = loder.getController();
                componentController.setUserData(user);

                listViewAvailableUser.getItems().add(lblUser);
            }
        } catch (RemoteException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    private void subscribe(User user) {
        try {
            if (null == user.getDeletedAt()) {
                Optional<ButtonType> result = alert(
                        Alert.AlertType.CONFIRMATION,
                        "Confirmation",
                        "Do you want to add this user?",
                        user.getNickname()+ " [id: " + user.getUserId()+ " |email: " + user.getEmail() + "]"
                );

                if (result.isPresent() && result.get() == ButtonType.OK) {
                    Subscription subscription = new Subscription();
                    SubscriptionId subscriptionId = new SubscriptionId();
                    subscriptionId.setChatId(chat.getChatId());
                    subscriptionId.setUserId(user.getUserId());
                    subscription.setId(subscriptionId);
                    subscription.setRegisteredAt(Date.from(Instant.now()));

                    if (subscriptionRemote.subscribe(subscription)) {
                        setListViewAvailableUser();
                        setListViewAlreadyUser();
                    }
                }
            } else {
                alert(
                        Alert.AlertType.WARNING,
                        "Warning",
                        "This user has been Blocked!",
                        "Blocked user can not be added to chat"
                );
            }
        } catch (RemoteException ex) {
            alert(Alert.AlertType.ERROR, "RemoteException", null, "Failed!");
        }
    }
    
    private void setListViewAlreadyUser() {
        listViewAlreadyUser.getItems().clear();
        
        try {
            for (User user : userRemote.getSubscribedUsers(chat)) {
                FXMLLoader loder = new FXMLLoader(getClass().getResource("../view/SubscribeAlreadyComponent.fxml"));
                Label lblUser = loder.load();
                
                lblUser.setOnMouseClicked((event) -> {
                    event.consume();
                    unsubscribe(user);
                });

                SubscribeAlreadyComponentController componentController = loder.getController();
                componentController.setUserData(user);

                listViewAlreadyUser.getItems().add(lblUser);
            }
        } catch (RemoteException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    private void unsubscribe(User user) {
        try {
            Optional<ButtonType> result = alert(
                    Alert.AlertType.CONFIRMATION,
                    "Confirmation",
                    "Do you want to remove this user?",
                    user.getNickname()+ " [id: " + user.getUserId()+ " |email: " + user.getEmail() + "]"
            );

            if (result.isPresent() && result.get() == ButtonType.OK) {
                Subscription subscription = new Subscription();
                SubscriptionId subscriptionId = new SubscriptionId();
                subscriptionId.setChatId(chat.getChatId());
                subscriptionId.setUserId(user.getUserId());
                subscription.setId(subscriptionId);

                if (subscriptionRemote.unsubscribe(subscription)) {
                    setListViewAlreadyUser();
                    setListViewAvailableUser();
                }
            }
        } catch (RemoteException ex) {
            alert(Alert.AlertType.ERROR, "RemoteException", null, "Failed!");
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
