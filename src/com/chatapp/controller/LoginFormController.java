/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.chatapp.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Helanka
 */
public class LoginFormController implements Initializable  {

    @FXML
    private AnchorPane mainContext;
    @FXML
    private AnchorPane loginContext;
    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField pwdPassword;

    /**
     * Initializes the controller class.
     */
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }    

    @FXML
    private void loginOnAction(ActionEvent event) throws IOException {
        setUi("AdminDashboardForm");
    }

    @FXML
    private void createAccountOnAction(ActionEvent event) throws IOException {
        setUi("CreateUserAccountForm");
    }
    
    private void setUi(String location) throws IOException {
        Stage stage = (Stage) loginContext.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/" + location + ".fxml"))));
    }
}
