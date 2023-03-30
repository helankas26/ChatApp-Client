/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.chatapp.controller;

import com.chatapp.client.Client;
import com.chatapp.pojos.User;
import com.chatapp.rmi.UserRemote;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

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
    
    private UserRemote userRemote;
    private User user;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {  
        try {
            String imagePath = getClass().getResource("../asserts/profileTemp.png").toURI().toString();
            displayImage(imagePath);
        } catch (URISyntaxException ex) {
            System.out.println(ex.getMessage());
        }
        
        userRemote = Client.getUserRemote();
        user = new User();
    }    

    @FXML
    private void signupOnAction(ActionEvent event) throws IOException {
        if (!isFieldsEmpty()) {
            getInputFields();
            
            try {
                if (userRemote.registerUser(user)) {
                    Optional<ButtonType> result = alert(
                            Alert.AlertType.INFORMATION,
                            "Insert Successful",
                            "Account created Successfully!"
                    );
                    
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        setUi("LoginForm");
                    } else {
                        event.consume();
                        clearFields();
                    }
                } else {
                    alert(Alert.AlertType.ERROR, "Exception", "Unsuccessful!");
                }
            } catch (RemoteException ex) {
                alert(Alert.AlertType.ERROR, "RemoteException", "Failed!");
            }
        } else {
            alert(Alert.AlertType.WARNING, "Warning", "All fields are required");
        }
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
        displayImage(image.toURI().toString());
        uploadImage(image);
    }
    
    private void displayImage(String imagePath) {
        Image profileTemp = new Image(imagePath);
        proPic.setFill(new ImagePattern(profileTemp));
    }
    
    private void uploadImage(File image) {
        try {
            BufferedImage originalImage = ImageIO.read(image);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Optional<String> fileExtension = Optional.ofNullable(image.toString())
                    .filter(f -> f.contains("."))
                    .map(f -> f.substring(image.toString().lastIndexOf(".") + 1));
            
            ImageIO.write(originalImage, fileExtension.get(), baos);
            user.setAvatar(baos.toByteArray());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    private void setUi(String location) throws IOException {
        Stage stage = (Stage) createAccountContext.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/" + location + ".fxml"))));
    }
    
    private boolean isFieldsEmpty() {
        return txtEmail.getText().isEmpty() || txtUsername.getText().isEmpty() ||
                pwdPassword.getText().isEmpty() || txtNickName.getText().isEmpty();
    }
    
    private void getInputFields() {
        URI imagePath = null;
        try {
            imagePath = getClass().getResource("../asserts/user-empty.png").toURI();
        } catch (URISyntaxException ex) {
            System.out.println(ex.getMessage());
        }
        user.setEmail(txtEmail.getText());
        user.setUsername(txtUsername.getText());
        user.setPassword(pwdPassword.getText());
        user.setNickname(txtNickName.getText());
        user.setRole("User");
        
        if (null == user.getAvatar()) {
            File image = new File(imagePath);
            uploadImage(image);
        }
    }
    
    private void clearFields() {
        txtEmail.clear();
        txtUsername.clear();
        pwdPassword.clear();
        txtNickName.clear();
        String imagePath;
        try {
            imagePath = getClass().getResource("../asserts/profileTemp.png").toURI().toString();
            displayImage(imagePath);
        } catch (URISyntaxException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    private Optional<ButtonType> alert(Alert.AlertType alertType, String title, String contentText) {
        Optional<ButtonType> result = null;
        
        try {
            Stage parentStage = (Stage) createAccountContext.getScene().getWindow();
            Alert alert = new Alert(alertType);
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            String iconPath = getClass().getResource("../asserts/view_chat.png").toURI().toString();
            stage.getIcons().add(new Image(iconPath));
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.initOwner(parentStage);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(contentText);
            
            result = alert.showAndWait();
        } catch (URISyntaxException ex) {
            System.out.println(ex.getMessage());
        }
        return result;
    }
}
