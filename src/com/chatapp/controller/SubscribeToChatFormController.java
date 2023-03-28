/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.chatapp.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;

/**
 * FXML Controller class
 *
 * @author Helanka
 */
public class SubscribeToChatFormController implements Initializable {

    @FXML
    private JFXComboBox<?> cmbChatList;
    @FXML
    private HBox vChat;
    @FXML
    private Circle chatIcon;
    @FXML
    private Label lblName;
    @FXML
    private Label lblDescription;
    @FXML
    private JFXListView<?> listViewAvailableUser;
    @FXML
    private JFXListView<?> listViewAlreadyUser;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        vChat.setVisible(false);
    }    

    @FXML
    private void imageUploadOnClicked(MouseEvent event) {
    }

    @FXML
    private void chatCloseOnAction(MouseEvent event) {
    }
    
}
