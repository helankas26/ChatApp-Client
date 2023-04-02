/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.chatapp.controller;

import com.chatapp.client.Client;
import com.chatapp.pojos.Chat;
import com.chatapp.rmi.ChatRemote;
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
import java.util.logging.Level;
import java.util.logging.Logger;
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

/**
 * FXML Controller class
 *
 * @author Helanka
 */
public class CreateChatFormController implements Initializable {

    @FXML
    private JFXTextField txtName;
    @FXML
    private JFXTextField txtDescription;
    @FXML
    private Circle chatIcon;
    
    private static AnchorPane adminDashboardContext;
    private ChatRemote chatRemote;
    private Chat chat;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            String imagePath = getClass().getResource("../asserts/profileTemp.png").toURI().toString();
            displayImage(imagePath);
        } catch (URISyntaxException ex) {
            Logger.getLogger(CreateChatFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        chatRemote = Client.getChatRemote();
        chat = new Chat();
    }    

    @FXML
    private void createOnAction(ActionEvent event) {
        if (!isFieldsEmpty()) {
            getInputFields();
            
            try {
                if (chatRemote.createChat(chat)) {
                    Optional<ButtonType> result = alert(
                            Alert.AlertType.INFORMATION,
                            "Insert Successful",
                            "Chat created Successfully!"
                    );
                    
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        clearFields();
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
        chatIcon.setFill(new ImagePattern(profileTemp));
    }
    
    private void uploadImage(File image) {
        try {
            BufferedImage originalImage = ImageIO.read(image);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Optional<String> fileExtension = Optional.ofNullable(image.toString())
                    .filter(f -> f.contains("."))
                    .map(f -> f.substring(f.lastIndexOf(".") + 1));
            ImageIO.write(originalImage, fileExtension.get(), baos);
            chat.setAvatar(baos.toByteArray());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private boolean isFieldsEmpty() {
        return txtName.getText().isEmpty() || txtDescription.getText().isEmpty();
    }

    private void getInputFields() {
        URI imagePath = null;
        try {
            imagePath = getClass().getResource("../asserts/chat-empty.png").toURI();
        } catch (URISyntaxException ex) {
            System.out.println(ex.getMessage());
        }
        chat.setName(txtName.getText());
        chat.setDescription(txtDescription.getText());
        
        if (null == chat.getAvatar()) {
            File image = new File(imagePath);
            uploadImage(image);
        }
    }
    
    private void clearFields() {
        txtName.clear();
        txtDescription.clear();
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
            Stage parentStage = (Stage) adminDashboardContext.getScene().getWindow();
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
    
    public static void setContext(AnchorPane context) {
        adminDashboardContext = context;
    }
}
