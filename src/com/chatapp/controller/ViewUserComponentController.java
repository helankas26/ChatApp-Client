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
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;

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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void imageUploadOnClicked(MouseEvent event) {
    }
    
}
