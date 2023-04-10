/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.chatapp.controller;

import com.chatapp.client.Client;
import com.chatapp.pojos.Chat;
import com.chatapp.pojos.Subscription;
import com.chatapp.pojos.SubscriptionId;
import com.chatapp.rmi.ChatRemote;
import com.chatapp.rmi.SubscriptionRemote;
import com.chatapp.util.Cookie;
import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.rmi.RemoteException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
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
    @FXML
    private JFXButton btnBack;
    @FXML
    private JFXButton btnNewChat;
    
    private boolean isChatActive;
    private boolean isProfileActive;
    private ChatRemote chatRemote;
    private SubscriptionRemote subscriptionRemote;

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
        
        chatRemote = Client.getChatRemote();
        subscriptionRemote = Client.getSubscriptionRemote();
        
        setSubscribedVChat("");
        
        initializeDate();
        initializeTime();
        vProfile.setVisible(false);
        
        btnBack.setVisible(false);
        txtSearchNew.setVisible(false);
        btnNewChat.setVisible(true);
        txtSearchExist.setVisible(true);
        
        isChatActive = true;
        isProfileActive = false;
        
        txtSearchExist.textProperty()
               .addListener((observable, oldValue, newValue) -> {
                   setSubscribedVChat(newValue);
               });
        
        txtSearchNew.textProperty()
               .addListener((observable, oldValue, newValue) -> {
                   setToSubscribeVChat(newValue);
               });
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
                null,
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
        btnBack.setVisible(false);
        txtSearchNew.setVisible(false);
        btnNewChat.setVisible(true);
        txtSearchExist.setVisible(true);
        txtSearchNew.clear();
        setSubscribedVChat("");
    }

    @FXML
    private void newChatOnAction(ActionEvent event) {
        btnNewChat.setVisible(false);
        txtSearchExist.setVisible(false);
        btnBack.setVisible(true);
        txtSearchNew.setVisible(true);
        txtSearchExist.clear();
        setToSubscribeVChat("");
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
    
    private void setSubscribedVChat(String search) {
        vChat.getChildren().clear();
        search = search.toLowerCase();
        
        try {
            for (Chat chat : chatRemote.getSubscribedChats(Cookie.getLoginUser())) {
                if (
                        chat.getName().toLowerCase().contains(search) ||
                        chat.getDescription().toLowerCase().contains(search)
                ) {
                    FXMLLoader loder = new FXMLLoader(getClass().getResource("../view/ChatComponent.fxml"));
                    Pane chatPane = loder.load();
                    
                    chatPane.setOnMouseClicked((event) -> {
                        try {
                            event.consume();
                            loadUi("ConversationForm");
                        } catch (IOException ex) {
                            System.out.println(ex.getMessage());
                        }
                    });
                    
                    ChatComponentController chatComponentController = loder.getController();
                    chatComponentController.setUserData(chat);
                    
                    chatComponentController.getBtnUnsubscribe().setOnMouseClicked((event) -> {
                        event.consume();
                        unsubscribe(chat);         
                    });

                    vChat.getChildren().add(chatPane);
                    VBox.setMargin(chatPane, new Insets(0, 0, 5, 0));
                }
            }
        } catch (RemoteException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    private void unsubscribe(Chat chat) {
        try {
            Optional<ButtonType> result = alert(
                    Alert.AlertType.CONFIRMATION,
                    "Confirmation",
                    "Do you want to unsubscribe?",
                    chat.getName() + " [id: " + chat.getChatId() + "]"
            );

            if (result.isPresent() && result.get() == ButtonType.OK) {
                Subscription subscription = new Subscription();
                SubscriptionId subscriptionId = new SubscriptionId();
                subscriptionId.setChatId(chat.getChatId());
                subscriptionId.setUserId(Cookie.getLoginUser().getUserId());
                subscription.setId(subscriptionId);

                if (subscriptionRemote.unsubscribe(subscription)) {
                    txtSearchExist.clear();
                    setSubscribedVChat("");
                }
            }
        } catch (RemoteException ex) {
            alert(Alert.AlertType.ERROR, "RemoteException", null, "Failed!");
        } 
    }
    
    private void setToSubscribeVChat(String search) {
        vChat.getChildren().clear();
        search = search.toLowerCase();
        
        try {
            for (Chat chat : chatRemote.getToSubscribeChats(Cookie.getLoginUser())) {
                if (
                        chat.getName().toLowerCase().contains(search) ||
                        chat.getDescription().toLowerCase().contains(search)
                ) {
                    FXMLLoader loder = new FXMLLoader(getClass().getResource("../view/ChatComponent.fxml"));
                    Pane chatPane = loder.load();
                    
                    chatPane.setOnMouseClicked((event) -> {
                        event.consume();
                        subscribe(chat);
                    });

                    ChatComponentController chatComponentController = loder.getController();
                    chatComponentController.setUserData(chat);
                    
                    chatComponentController.getBtnUnsubscribe().setVisible(false);

                    vChat.getChildren().add(chatPane);
                    VBox.setMargin(chatPane, new Insets(0, 0, 5, 0));
                }
            }
        } catch (RemoteException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    private void subscribe(Chat chat) {
        try {
            Optional<ButtonType> result = alert(
                    Alert.AlertType.CONFIRMATION,
                    "Confirmation",
                    "Do you want to subscribe?",
                    chat.getName() + " [id: " + chat.getChatId() + "]"
            );

            if (result.isPresent() && result.get() == ButtonType.OK) {
                Subscription subscription = new Subscription();
                SubscriptionId subscriptionId = new SubscriptionId();
                subscriptionId.setChatId(chat.getChatId());
                subscriptionId.setUserId(Cookie.getLoginUser().getUserId());
                subscription.setId(subscriptionId);
                subscription.setRegisteredAt(Date.from(Instant.now()));

                if (subscriptionRemote.subscribe(subscription)) {
                    txtSearchNew.clear();
                    setToSubscribeVChat("");
                }
            }
        } catch (RemoteException ex) {
            alert(Alert.AlertType.ERROR, "RemoteException", null, "Failed!");
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
}
