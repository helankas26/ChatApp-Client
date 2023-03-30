/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chatapp;

import java.io.IOException;
import java.net.Socket;
import java.rmi.RMISecurityManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Helanka
 */
public class AppInitializer extends Application{

    public static void main(String[] args){
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new RMISecurityManager());
        }
        
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        String iconPath = getClass().getResource("asserts/view_chat.png").toURI().toString();
        
        try {
            Socket s = new Socket("localhost", 4000);
            System.out.println("Client Connected to server.....");
            
            primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("view/LoginForm.fxml"))));
            primaryStage.setResizable(false);
            primaryStage.getIcons().add(new Image(iconPath));
            primaryStage.setTitle("Chat App");
            primaryStage.show();
            primaryStage.centerOnScreen();
            
        } catch (IOException ex) {
            Alert alert = new Alert(AlertType.WARNING);
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(iconPath));
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setTitle("Warning");
            alert.setHeaderText(ex.getMessage());
            alert.setContentText("Server is not up and running at the moment \nTry again later");
            alert.showAndWait();
            
            System.out.println("Server is not up and running at the moment.....");
            System.out.println(ex.getMessage());
        }
 
    }
    
}
