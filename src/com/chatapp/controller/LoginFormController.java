/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.chatapp.controller;

import com.chatapp.client.Client;
import com.chatapp.pojos.User;
import com.chatapp.rmi.UserRemote;
import com.chatapp.util.Cookie;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
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
    @FXML
    private Label lblUsername;
    @FXML
    private Label lblPassword;
    
    private UserRemote userRemote;

    /**
     * Initializes the controller class.
     */
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        userRemote = Client.getUserRemote();
    }    

    @FXML
    private void loginOnAction(ActionEvent event) throws IOException {
        if (isFieldsEmpty()) {
            User user = new User();
            user.setUsername(txtUsername.getText().trim());
            user.setPassword(pwdPassword.getText().trim());
            
            user = userRemote.login(user);
            
            if (null != user.getUserId()) {
                changeField(lblUsername, "");

                if (null != user.getPassword()) {
                    changeField(lblPassword, "");
                    
                    if (null == user.getDeletedAt()) {
                        
                        switch (user.getRole()) {
                            case "Admin":
                                Cookie.setLoginUser(user);
                                setUiOnCloseRequest("AdminDashboardForm");
                                break;
                            case "User":
                                Cookie.setLoginUser(user);
                                setUiOnCloseRequest("UserDashboardForm");
                                break;    
                            default:
                                Optional<ButtonType> result = alert(
                                        Alert.AlertType.ERROR,
                                        "Error",
                                        "Account can not be found!"
                                );
                                if (result.isPresent() && result.get() == ButtonType.OK) {
                                    txtUsername.clear();
                                    txtUsername.requestFocus();
                                    pwdPassword.clear();
                                }
                        }

                    } else {
                        Optional<ButtonType> result = alert(
                                Alert.AlertType.ERROR,
                                "Error",
                                "Access denied",
                                "You are not authorized to access the system!"
                        );
                        if (result.isPresent() && result.get() == ButtonType.OK) {
                            txtUsername.clear();
                            txtUsername.requestFocus();
                            pwdPassword.clear();
                        }
                    }

                } else {
                    pwdPassword.clear();
                    changeField(lblPassword, "Incorrect Password");
                    pwdPassword.requestFocus();
                }

            } else {
                txtUsername.clear();
                changeField(lblUsername, "Incorrect Username");
                pwdPassword.clear();
                txtUsername.requestFocus();
            }
        }  
    }

    @FXML
    private void createAccountOnAction(ActionEvent event) throws IOException {
        setUi("CreateUserAccountForm");
    }
    
    private void setUi(String location) throws IOException {
        Stage stage = (Stage) loginContext.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/" + location + ".fxml"))));
    }
    
    private void setUiOnCloseRequest (String location) throws IOException {
        Stage stage = (Stage) loginContext.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/" + location + ".fxml"))));
        stage.setOnCloseRequest((event) -> {
            try {
                event.consume();
                logoutOnCloseRequest(stage);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        });
    }
    
    private void logoutOnCloseRequest(Stage stage) throws IOException  {
        Optional<ButtonType> result = alert(
                Alert.AlertType.CONFIRMATION,
                "Logout",
                "Do you really want to logout?",
                stage
        );

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                Cookie.setLoginUser(null);
                stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/LoginForm.fxml"))));
                stage.setOnCloseRequest((event) -> {
                    System.exit(0);
                });
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
    
    private boolean isFieldsEmpty() {
        if (txtUsername.getText().equalsIgnoreCase("")) {
            changeField(lblUsername, "Username requied");
            if (pwdPassword.getText().equalsIgnoreCase("")) {
                changeField(lblPassword, "Password requied");
            } else {
                changeField(lblPassword, "");
            }
            txtUsername.requestFocus();
            return false;
        } else {
            changeField(lblUsername, "");
            if (pwdPassword.getText().equalsIgnoreCase("")) {
                changeField(lblPassword, "Password requied");
                pwdPassword.requestFocus();
                return false;
            } else {
                changeField(lblPassword, "");
                return true;
            }
        }
    }
    
    private void changeField(Label lable, String msg) {
        lable.setText(msg);
    }
    
    private Optional<ButtonType> alert(Alert.AlertType alertType, String title, String contentText) {
        Optional<ButtonType> result = null;
        
        try {
            Stage parentStage = (Stage) loginContext.getScene().getWindow();
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
    
    private Optional<ButtonType> alert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Optional<ButtonType> result = null;
        
        try {
            Stage parentStage = (Stage) loginContext.getScene().getWindow();
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

    private Optional<ButtonType> alert(Alert.AlertType alertType, String title, String contentText, Stage parentStage) {
        Optional<ButtonType> result = null;
        
        try {
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
