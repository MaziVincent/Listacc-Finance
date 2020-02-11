/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.AppModel;
import model.Users;
import services.data.UserService;

/**
 *
 * @author Agozie
 */
public class Login implements Initializable  {
        @FXML
        TextField textField;
        @FXML
        PasswordField passwordField;
        @FXML
        Label lblConnectionStatus;
        @FXML
        Button btnSignin;
        @FXML
        Label  lblLoginStatus;
        final AppModel model;
       
        public Login(AppModel model)
        {
            this.model = model;
        }
        @Override
        public void initialize(URL url, ResourceBundle rb) {
            btnSignin.requestFocus();
        }
        
        @FXML
        private void signIn(ActionEvent evt)
        {
          
                btnSignin.setDisable(true);
        
            if(true)
            {
                 Platform.runLater(() -> {
                 lblLoginStatus.setText("Initializing");
                 }); 
                Platform.runLater(() ->{
                    try{   
                        Parent root;
                        FXMLLoader firstLoader = new FXMLLoader(getClass().getResource("/fxml/MainUI.fxml"));
                                //get User from databse 
                                 Users user = new UserService().getUserById(1);
                                 Stage primaryStage = model.getStage();
                                 MaiinUI mainUI = new MaiinUI(new AppModel(primaryStage, user));
                               firstLoader.setController(mainUI);
                               root = firstLoader.load();
                               Scene scene = new Scene(root, 1366, 768);
                               
                               primaryStage.setTitle("Listacc");
                               primaryStage.setScene(scene);
                               primaryStage.centerOnScreen();
                               primaryStage.show();
                               
                         }catch(Exception exc){exc.printStackTrace();
                         lblLoginStatus.setText("There was a problem initializing");
                         }
                
                     });
                           
               
            }else{
                lblLoginStatus.setText("Invalid Login Credentials");
            }
            
            Platform.runLater(() -> {
                btnSignin.setDisable(false);
            });
        }
}
