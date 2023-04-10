/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.chatapp.controller;

import com.chatapp.pojos.Chat;
import com.jfoenix.controls.JFXButton;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

/**
 * FXML Controller class
 *
 * @author Helanka
 */
public class ChatComponentController implements Initializable {

    @FXML
    private Circle chatIcon;
    @FXML
    private Label lblChatName;
    @FXML
    private ImageView imgOnline;
    @FXML
    private JFXButton btnUnsubscribe;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnUnsubscribe.setVisible(true);
        imgOnline.setVisible(false);
    }    

    public void setUserData(Chat chat) {
        ByteArrayInputStream bais = new ByteArrayInputStream(chat.getAvatar());
               
        chatIcon.setFill(new ImagePattern(new Image(bais)));
        lblChatName.setText(chat.getName());
        
        if (chat.getStatus() == 1) {
            imgOnline.setVisible(true);
        } 
    }
    
    public JFXButton getBtnUnsubscribe() {
        return btnUnsubscribe;
    }
}
