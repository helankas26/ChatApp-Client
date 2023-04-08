/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.chatapp.controller;

import com.chatapp.pojos.User;
import com.chatapp.util.Cookie;
import com.jfoenix.controls.JFXTextField;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

/**
 * FXML Controller class
 *
 * @author Helanka
 */
public class ViewProfileFormController implements Initializable {

    @FXML
    private Circle proPic;
    @FXML
    private JFXTextField txtId;
    @FXML
    private JFXTextField txtEmail;
    @FXML
    private JFXTextField txtUsername;
    @FXML
    private JFXTextField txtNickName;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        User user = Cookie.getLoginUser();
        
        ByteArrayInputStream bais = new ByteArrayInputStream(user.getAvatar());

        proPic.setFill(new ImagePattern(new Image(bais)));
        txtId.setText(user.getUserId().toString());
        txtEmail.setText(user.getEmail());
        txtUsername.setText(user.getUsername());
        txtNickName.setText(user.getNickname());
        
    }
}
