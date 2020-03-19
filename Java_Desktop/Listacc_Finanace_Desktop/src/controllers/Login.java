/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.AppModel;
import model.Users;
import services.net.AuthenticationService;
import services.net.ConnectionService;

/**
 *
 * @author Agozie
 */
public class Login implements Initializable  {
    @FXML
    TextField txtUsername;
    @FXML
    PasswordField txtPassword;
    @FXML
    Label lblConnectionStatus;
    @FXML
    Button btnSignin;
    @FXML
    public Label lblLoginStatus;
        
    final private SimpleStringProperty connectionSp = new SimpleStringProperty();
    SimpleObjectProperty colorConnectionProperty = new SimpleObjectProperty<>();
    public StringProperty signinStatuSP = new SimpleStringProperty();
    final AppModel model;
    String password;
       
    public Login(AppModel model)
    {
        this.model = model;
    }
        
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnSignin.requestFocus();
        
        // set bindings
        lblConnectionStatus.textProperty().bind(connectionSp);
        lblConnectionStatus.textFillProperty().bind(colorConnectionProperty);
        lblLoginStatus.textProperty().bind(signinStatuSP);

        // check for internet connection
        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(() -> {
            Platform.runLater(() -> {
                ConnectionService conn = new ConnectionService(colorConnectionProperty);
                conn.execute(connectionSp);
            });
        }, 0, 20, TimeUnit.SECONDS);
    }

    @FXML
    private void signIn(ActionEvent evt)
    {
        disableComponents();
        
        String username = txtUsername.getText().trim();
        password = txtPassword.getText().trim();
        // UserService userService = new UserService();
        // Users user = userService.getUserById(8);
        if(username.length() > 4 && password.length() > 4)
        {
            AuthenticationService authService = new AuthenticationService(username, password, this);
            authService.execute();
        }      
        else
        {
            signinStatuSP.set("Invalid username or password");
            lblLoginStatus.setTextFill(Color.RED);
            enableComponents();
        }

        // loadMainUI(user);
    }
    
    
    // MODIFY UI
    private void disableComponents(){
        txtUsername.setDisable(true);
        txtPassword.setDisable(true);
        btnSignin.setDisable(true);
    }
    
    public void enableComponents(){
        txtUsername.setDisable(false);
        txtPassword.setDisable(false);
        btnSignin.setDisable(false);
    }
    
    public void loadMainUI(Users user){
        Platform.runLater(() ->{
            try{
                Parent root;
                FXMLLoader firstLoader = new FXMLLoader(getClass().getResource("/fxml/MainUI.fxml"));
                
                Stage primaryStage = model.getStage();
                user.setPassword(password);
                MaiinUI mainUI = new MaiinUI(new AppModel(primaryStage, user));
                firstLoader.setController(mainUI);
                root = firstLoader.load();
                Scene scene = new Scene(root, 1366, 768);
                
                // show main ui
                primaryStage.setTitle("Listacc");
                primaryStage.setScene(scene);
                primaryStage.centerOnScreen();
                primaryStage.show();
                primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent e) {
                     Platform.exit();
                     System.exit(0);
                    }
                  });

            }
            catch(Exception exc){
                exc.printStackTrace();      
                lblLoginStatus.setText("There was a problem initializing");
            }

        });
    }
}
