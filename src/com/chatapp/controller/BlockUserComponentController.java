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
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
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
public class BlockUserComponentController implements Initializable {

    @FXML
    private Label lblId;
    @FXML
    private Circle userIcon;
    @FXML
    private Label lblUsername;
    @FXML
    private Label lblNickname;
    @FXML
    private Label lblEmail;
    @FXML
    private HBox hBoxUser;

    private AnchorPane adminDashboardContext;
    private VBox vBoxUserBlock;
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
    private void blockUserOnAction(MouseEvent event) {
        try {
            if (user.getRole().equals("User")) {
                Optional<ButtonType> result = alert(
                        Alert.AlertType.CONFIRMATION,
                        "Confirmation",
                        "Do you want to block this user?",
                        user.getNickname()+ " [id: " + user.getUserId()+ " |username: " + user.getUsername() + "]"
                );

                if (result.isPresent() && result.get() == ButtonType.OK) {
                    if (userRemote.blockUser(user)) {
                        vBoxUserBlock.getChildren().remove(hBoxUser);
                    }
                } else {
                    event.consume();
                }
            } else {
                alert(
                        Alert.AlertType.WARNING,
                        "Warning",
                        null,
                        "Admin can not be Blocked!"
                );
            }
        } catch (RemoteException ex) {
            alert(Alert.AlertType.ERROR, "RemoteException", null, "Failed!");
        }
    }
    
    public void setUserData(User user) {
        this.user = user;
        
        if (user.getRole().equals("Admin")) {
            setFields();
            hBoxUser.setStyle("-fx-background-color: #94b5eb");
        } else {
            setFields();
        }
    }
    
    private void setFields() {
        ByteArrayInputStream bais = new ByteArrayInputStream(user.getAvatar());
        lblId.setText(user.getUserId().toString());
        userIcon.setFill(new ImagePattern(new Image(bais)));
        lblUsername.setText(user.getUsername());
        lblNickname.setText(user.getNickname());
        lblEmail.setText(user.getEmail());
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
    
    public void setContext(AnchorPane context, VBox parentVBox) {
        adminDashboardContext = context;
        vBoxUserBlock = parentVBox;
    }
}
