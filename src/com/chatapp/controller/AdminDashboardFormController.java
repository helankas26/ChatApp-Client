/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.chatapp.controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Helanka
 */
public class AdminDashboardFormController implements Initializable {

    @FXML
    private AnchorPane loginContext;
    @FXML
    private VBox vUser;
    @FXML
    private VBox vChat;
    @FXML
    private AnchorPane adminDashboardContext;
    @FXML
    private AnchorPane adminMainContext;
    @FXML
    private Pane paneMenu;
    @FXML
    private Label lblDate;
    @FXML
    private Label lblTime;
    @FXML
    private ImageView imgAdminMainContext;
    
    private boolean isChatActive;
    private boolean isUserActive;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeDate();
        initializeTime();
        vUser.setVisible(false); 
        
        isChatActive = true;
        isUserActive = false;
    }    

    @FXML
    private void chatOnAction(ActionEvent event) {
        if (!isChatActive) {
            vUser.setVisible(false);
            vChat.setVisible(true);
            adminMainContext.getChildren().clear();
            adminMainContext.getChildren().add(imgAdminMainContext);
            isChatActive = !isChatActive;
            isUserActive = !isChatActive;
        }
    }

    @FXML
    private void userOnAction(ActionEvent event) {
        if (!isUserActive) {
            vChat.setVisible(false);
            vUser.setVisible(true);
            adminMainContext.getChildren().clear();
            adminMainContext.getChildren().add(imgAdminMainContext);
            isUserActive = !isUserActive;
            isChatActive = !isUserActive;
        }
    }

    @FXML
    private void logoutOnAction(ActionEvent event) throws IOException {
        setUi("LoginForm");
    }

    @FXML
    private void viewUserOnAction(ActionEvent event) throws IOException {
        loadUi("ViewUserForm");
    }


    @FXML
    private void viewChatOnAction(ActionEvent event) throws IOException {
        loadUi("ViewChatForm");
    }


    @FXML
    private void editChatOnAction(ActionEvent event) throws IOException {
        loadUi("EditChatForm");
    }

    @FXML
    private void removeUserOnAction(ActionEvent event) throws IOException {
        loadUi("RemoveUserForm");
    }

    @FXML
    private void createChatOnAction(ActionEvent event) throws IOException {
        loadUi("CreateChatForm");
    }

    @FXML
    private void subscribeToChatOnAction(ActionEvent event) throws IOException {
        loadUi("SubscribeToChatForm");
    }
    
    private void setUi(String location) throws IOException {
        Stage stage = (Stage) loginContext.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/" + location + ".fxml"))));
    }
    
    private void loadUi(String location) throws IOException {      
        FXMLLoader loder = new FXMLLoader(getClass().getResource("../view/" + location + ".fxml"));
        adminMainContext.getChildren().clear();
        adminMainContext.getChildren().add(loder.load());
    }
    
    public void initializeDate() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0),
                        e -> {
                            DateTimeFormatter dtf = DateTimeFormatter
                                    .ofPattern("yyyy-MM-dd");
                            lblDate.setText(LocalDate.now().format(dtf));
                        }
                ),
                new KeyFrame(Duration.seconds(1))
        );

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();      
    }
     
    public void initializeTime() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0),
                        e -> {
                            DateTimeFormatter dtf = DateTimeFormatter
                                    .ofPattern("HH:mm:ss");
                            lblTime.setText(LocalTime.now().format(dtf));
                        }
                ),
                new KeyFrame(Duration.seconds(1))
        );

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
}
