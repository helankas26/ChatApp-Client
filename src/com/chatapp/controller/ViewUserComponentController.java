/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.chatapp.controller;

import com.chatapp.client.Client;
import com.chatapp.pojos.User;
import com.chatapp.rmi.UserRemote;
import java.io.ByteArrayInputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Helanka
 */
public class ViewUserComponentController implements Initializable {

    @FXML
    private Circle userIcon;
    @FXML
    private Label lblUsername;
    @FXML
    private Label lblNickname;
    @FXML
    private Label lblEmail;
    @FXML
    private Pane paneUser;
    @FXML
    private VBox vBoxUser;
    
    private AnchorPane adminDashboardContext;
    private UserRemote userRemote;
    private User user;
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        userRemote = Client.getUserRemote();
    }
    
    @FXML
    private void unblockUserOnAction(MouseEvent event) {
        if (null != user.getDeletedAt()) {
            try {
                Optional<ButtonType> result = alert(
                        Alert.AlertType.CONFIRMATION,
                        "Confirmation",
                        "Do you want to unblock this user?",
                        user.getNickname()+ " [id: " + user.getUserId()+ " |username: " + user.getUsername() + "]"
                );

                if (result.isPresent() && result.get() == ButtonType.OK) {
                    if (userRemote.unblockUser(user)) {
                        paneUser.setCursor(Cursor.DEFAULT);
                        vBoxUser.setStyle("-fx-background-color: #c5c7c7; -fx-background-radius: 30px");
                        
                        setUserData(userRemote.getUser(user));
                    }
                }
            } catch (RemoteException ex) {
                alert(Alert.AlertType.ERROR, "RemoteException", null, "Failed!");
            }
        }
    }

    public void setUserData(User user) {
        this.user = user;
        ByteArrayInputStream bais = new ByteArrayInputStream(user.getAvatar());

        userIcon.setFill(new ImagePattern(new Image(bais)));
        lblUsername.setText(user.getUsername());
        lblNickname.setText(user.getNickname());
        lblEmail.setText(user.getEmail());
        
        if (null != user.getDeletedAt()) {
            paneUser.setCursor(Cursor.HAND);
            vBoxUser.setStyle("-fx-background-color: #e64949; -fx-background-radius: 30px");
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
