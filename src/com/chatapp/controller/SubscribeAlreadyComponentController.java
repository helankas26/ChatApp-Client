/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.chatapp.controller;

import com.chatapp.pojos.User;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

/**
 * FXML Controller class
 *
 * @author Helanka
 */
public class SubscribeAlreadyComponentController implements Initializable {
    
    @FXML
    private Label lblUser;
    @FXML
    private Circle userIcon;
    
    private User user;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //TODO
    }    

    public void setUserData(User user) {
        this.user = user;
        
        if (user.getRole().equals("Admin")) {
            setFields();
            lblUser.setStyle("-fx-background-color: #94b5eb");
        } else {
            setFields();
        }
    }
    
    private void setFields() {
        ByteArrayInputStream bais = new ByteArrayInputStream(user.getAvatar());
        userIcon.setFill(new ImagePattern(new Image(bais)));
        lblUser.setText("id: " + user.getUserId() + "\n" + user.getNickname());
    }
}
