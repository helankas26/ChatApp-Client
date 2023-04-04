/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.chatapp.controller;

import com.chatapp.client.Client;
import com.chatapp.pojos.User;
import com.chatapp.rmi.UserRemote;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Helanka
 */
public class BlockUserFormController implements Initializable {

    @FXML
    private JFXTextField txtSearch;
    @FXML
    private VBox vBoxUserBlock;
    
    private static AnchorPane adminDashboardContext;
    private UserRemote userRemote;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        userRemote = Client.getUserRemote();
        
        setVBoxUsers("");
        
        txtSearch.textProperty()
               .addListener((observable, oldValue, newValue) -> {
                   setVBoxUsers(newValue);
               });
    }
    
    private void setVBoxUsers(String search) {
        vBoxUserBlock.getChildren().clear();
        search = search.toLowerCase();
         
        try {
            for (User user : userRemote.getAllUser()) {
                if (
                        user.getUsername().toLowerCase().contains(search) ||
                        user.getNickname().toLowerCase().contains(search) ||
                        user.getEmail().toLowerCase().contains(search)  
                ) {
                    if (null == user.getDeletedAt()) {
                        FXMLLoader loder = new FXMLLoader(getClass().getResource("../view/BlockUserComponent.fxml"));
                        HBox hBoxUser = loder.load();

                        BlockUserComponentController userComponentController = loder.getController();
                        userComponentController.setUserData(user);
                        userComponentController.setContext(adminDashboardContext, vBoxUserBlock);

                        vBoxUserBlock.getChildren().add(hBoxUser);
                    }
                }
            }
        } catch (RemoteException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void setContext(AnchorPane context) {
        adminDashboardContext = context;
    }    
}
