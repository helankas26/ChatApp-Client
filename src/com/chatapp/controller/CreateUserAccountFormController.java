/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.chatapp.controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Helanka
 */
public class CreateUserAccountFormController implements Initializable {

    @FXML
    private JFXTextField txtEmail;
    @FXML
    private JFXTextField txtUsername;
    @FXML
    private JFXPasswordField pwdPassword;
    @FXML
    private JFXTextField txtNickName;
    @FXML
    private Circle proPic;
    @FXML
    private AnchorPane createAccountContext;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {  
        try {
            String imagePath = getClass().getResource("../asserts/profileTemp.png").toURI().toString();
            uploadImage(imagePath);
        } catch (URISyntaxException ex) {
            Logger.getLogger(CreateUserAccountFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    @FXML
    private void signupOnAction(ActionEvent event) {
    }

    @FXML
    private void backToLoginOnAction(ActionEvent event) throws IOException {
        setUi("LoginForm");
    }

    @FXML
    private void imageUploadOnClicked(MouseEvent event) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image file", "*.jpg", "*.jpeg", "*.png", "*.jpg")
        );
        File image = fc.showOpenDialog(null);
        uploadImage(image.toURI().toString());
    }
    
    private void uploadImage(String imagePath) {
        Image profileTemp = new Image(imagePath);
        proPic.setFill(new ImagePattern(profileTemp));
    }
    
    private void setUi(String location) throws IOException {
        Stage stage = (Stage) createAccountContext.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/" + location + ".fxml"))));
    }
}
