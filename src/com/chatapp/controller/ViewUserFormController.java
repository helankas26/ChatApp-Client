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
import javafx.geometry.Insets;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author Helanka
 */
public class ViewUserFormController implements Initializable {

    @FXML
    private JFXTextField txtSearch;
    @FXML
    private GridPane gridPaneUser;
    
    private static AnchorPane adminDashboardContext;
    private UserRemote userRemote;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        userRemote = Client.getUserRemote();
        
        setGridPaneUsers("");
        
        txtSearch.textProperty()
               .addListener((observable, oldValue, newValue) -> {
                   setGridPaneUsers(newValue);
               });
    }    
    
    private void setGridPaneUsers(String search) {
        gridPaneUser.getChildren().clear();
        search = search.toLowerCase();
        int column = 0, row = 1;
         
        try {
            for (User user : userRemote.getAllUser()) {
                if (
                        user.getUsername().toLowerCase().contains(search) ||
                        user.getNickname().toLowerCase().contains(search) ||
                        user.getEmail().toLowerCase().contains(search)  
                ) {
                    FXMLLoader loder = new FXMLLoader(getClass().getResource("../view/ViewUserComponent.fxml"));
                    Pane paneUser = loder.load();

                    ViewUserComponentController userComponentController = loder.getController();
                    userComponentController.setUserData(user);
                    userComponentController.setContext(adminDashboardContext);

                    if (column == 2) {
                        column = 0;
                        row++;
                    }

                    gridPaneUser.add(paneUser, column++, row);
                    GridPane.setMargin(paneUser, new Insets(5));
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
