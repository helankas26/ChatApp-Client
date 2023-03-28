/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.chatapp.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void endChatOnAction(MouseEvent event) {
    }

    @FXML
    private void startChatOnAction(MouseEvent event) {
    }
    
}
