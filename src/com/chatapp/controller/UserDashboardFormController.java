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
import javafx.scene.control.TextField;
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
public class UserDashboardFormController implements Initializable {

    @FXML
    private AnchorPane userDashboardContext;
    @FXML
    private Pane paneMenu;
    @FXML
    private VBox vProfile;
    @FXML
    private Pane paneChat;
    @FXML
    private TextField txtSearchNew;
    @FXML
    private TextField txtSearchExist;
    @FXML
    private VBox vChat;
    @FXML
    private Label lblDate;
    @FXML
    private Label lblTime;
    @FXML
    private AnchorPane userMainContext;
    @FXML
    private ImageView imgUserMainContext;
    
    private boolean isChatActive;
    private boolean isProfileActive;

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
        vProfile.setVisible(false);
        
        isChatActive = true;
        isProfileActive = false;
    }    

    @FXML
    private void chatOnAction(ActionEvent event) {
        if (!isChatActive) {
            vProfile.setVisible(false);
            paneChat.setVisible(true);
            userMainContext.getChildren().clear();
            userMainContext.getChildren().add(imgUserMainContext);
            isChatActive = !isChatActive;
            isProfileActive = !isChatActive;
        }
    }

    @FXML
    private void profileOnAction(ActionEvent event) {
        if (!isProfileActive) {
            paneChat.setVisible(false);
            vProfile.setVisible(true);
            userMainContext.getChildren().clear();
            userMainContext.getChildren().add(imgUserMainContext);
            isProfileActive = !isProfileActive;
            isChatActive = !isProfileActive;
        }
    }

    @FXML
    private void logoutOnAction(ActionEvent event) {
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
    private void myProfileOnAction(ActionEvent event) throws IOException {
        userDashboardContext.getChildren().remove(userMainContext);
        loadUi("ViewProfileForm");
    }

    @FXML
    private void updateProfileOnAction(ActionEvent event) throws IOException {
        userDashboardContext.getChildren().remove(userMainContext);
        UpdateProfileFormController.setContext(userDashboardContext);
        loadUi("UpdateProfileForm");
    }

    @FXML
    private void backOnAction(ActionEvent event) {
    }

    @FXML
    private void newChatOnAction(ActionEvent event) {
    }
    
    private void setUi(String location) throws IOException {
        Stage stage = (Stage) userDashboardContext.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/" + location + ".fxml"))));
        stage.setOnCloseRequest((event) -> {
            System.exit(0);
        });
    }

    private void loadUi(String location) throws IOException {      
        FXMLLoader loder = new FXMLLoader(getClass().getResource("../view/" + location + ".fxml"));
        userMainContext.getChildren().clear();
        userMainContext.getChildren().add(loder.load());
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
            Stage parentStage = (Stage) userDashboardContext.getScene().getWindow();
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
