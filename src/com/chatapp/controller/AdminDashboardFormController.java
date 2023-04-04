/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.chatapp.controller;

import com.chatapp.util.Cookie;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
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
        try {
            checkUser();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
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
        Optional<ButtonType> result = alert(
                Alert.AlertType.CONFIRMATION,
                "Logout",
                "Do you really want to logout?"
        );
                
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                Cookie.setLoginUser(null);
                setUi("LoginForm");
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }

        } else {
            event.consume();
        }
    }

    @FXML
    private void viewUserOnAction(ActionEvent event) throws IOException {
        adminDashboardContext.getChildren().remove(adminMainContext);
        ViewUserFormController.setContext(adminDashboardContext);
        loadUi("ViewUserForm");
    }

    @FXML
    private void viewChatOnAction(ActionEvent event) throws IOException {
        adminDashboardContext.getChildren().remove(adminMainContext);
        ViewChatFormController.setContext(adminDashboardContext);
        loadUi("ViewChatForm");
    }

    @FXML
    private void editChatOnAction(ActionEvent event) throws IOException {
        adminDashboardContext.getChildren().remove(adminMainContext);
        EditChatFormController.setContext(adminDashboardContext);
        loadUi("EditChatForm");
    }

    @FXML
    private void blockUserOnAction(ActionEvent event) throws IOException {
        adminDashboardContext.getChildren().remove(adminMainContext);
        BlockUserFormController.setContext(adminDashboardContext);
        loadUi("BlockUserForm");
    }

    @FXML
    private void createChatOnAction(ActionEvent event) throws IOException {
        adminDashboardContext.getChildren().remove(adminMainContext);
        CreateChatFormController.setContext(adminDashboardContext);
        loadUi("CreateChatForm");
    }

    @FXML
    private void subscribeToChatOnAction(ActionEvent event) throws IOException {
        loadUi("SubscribeToChatForm");
    }
    
    private void setUi(String location) throws IOException {
        Stage stage = (Stage) adminDashboardContext.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/" + location + ".fxml"))));
        stage.setOnCloseRequest((event) -> {
            System.exit(0);
        });
    }
    
    private void loadUi(String location) throws IOException {      
        FXMLLoader loder = new FXMLLoader(getClass().getResource("../view/" + location + ".fxml"));
        adminMainContext.getChildren().clear();
        adminMainContext.getChildren().add(loder.load());
    }
    
    private void initializeDate() {
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
     
    private void initializeTime() {
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
    
    private void checkUser() throws IOException {
        if (null == Cookie.getLoginUser()) {
            setUi("LoginForm");
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
}
