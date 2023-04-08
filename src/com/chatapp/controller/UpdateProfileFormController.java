/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.chatapp.controller;

import com.chatapp.client.Client;
import com.chatapp.pojos.User;
import com.chatapp.rmi.UserRemote;
import com.chatapp.util.Cookie;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
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
import javafx.fxml.Initializable;
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
import org.hibernate.exception.ConstraintViolationException;

/**
 * FXML Controller class
 *
 * @author Helanka
 */
public class UpdateProfileFormController implements Initializable {

    @FXML
    private Circle proPic;
    @FXML
    private JFXTextField txtUsername;
    @FXML
    private JFXPasswordField pwdPassword;
    @FXML
    private JFXTextField txtNickName;
    
    private static AnchorPane userDashboardContext;
    private UserRemote userRemote;
    private User user;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        userRemote = Client.getUserRemote();
        
        user = Cookie.getLoginUser();
        
        ByteArrayInputStream bais = new ByteArrayInputStream(user.getAvatar());

        proPic.setFill(new ImagePattern(new Image(bais)));
        txtUsername.setText(user.getUsername());
        pwdPassword.setText(user.getPassword());
        txtNickName.setText(user.getNickname());
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

    @FXML
    private void updateOnAction(ActionEvent event) {
        if (!isFieldsEmpty()) {
            getInputFields();
            
            try {
                if (userRemote.updateUser(user)) {
                    alert(
                            Alert.AlertType.INFORMATION,
                            "Update Successful",
                            null,
                            "Chat Updated Successfully!"
                    );
                }
            } catch (ConstraintViolationException ex) {
                alert(
                        Alert.AlertType.ERROR,
                        "SQLException",
                        "Username already taken by another user.",
                        "Try previous or new Username!"
                );
            } catch (RemoteException ex) {
                alert(Alert.AlertType.ERROR, "RemoteException", null, "Failed!");
            }
        } else {
            alert(Alert.AlertType.WARNING, "Warning", null, "All fields are required");
        }
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
    
    private boolean isFieldsEmpty() {
        return txtUsername.getText().isEmpty() || pwdPassword.getText().isEmpty()
                || txtNickName.getText().isEmpty();
    }

    private void getInputFields() {
        URI imagePath = null;
        try {
            imagePath = getClass().getResource("../asserts/user-empty.png").toURI();
        } catch (URISyntaxException ex) {
            System.out.println(ex.getMessage());
        }
        user.setUsername(txtUsername.getText());
        user.setPassword(pwdPassword.getText());
        user.setNickname(txtNickName.getText());
        
        if (null == user.getAvatar()) {
            File image = new File(imagePath);
            uploadImage(image);
        }
    }
    
    private Optional<ButtonType> alert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Optional<ButtonType> result = null;
        
        try {
            Stage parentStage = (Stage) userDashboardContext.getScene().getWindow();
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

    public static void setContext(AnchorPane context) {
        userDashboardContext = context;
    }
}
