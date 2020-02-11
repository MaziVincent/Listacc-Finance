/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listacc_finanace_desktop;

import controllers.Login;
import controllers.MaiinUI;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import model.AppModel;
import model.Users;
import services.data.UserService;

/**
 *
 * @author Agozie
 */
public class Listacc_Finanace_Desktop extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
            }
        });
        try{
        FXMLLoader firstLoader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
                               final AppModel model = new AppModel(primaryStage) ;
                               firstLoader.setController(new Login(model));
                               Parent root = firstLoader.load();
                               Scene scene = new Scene(root, 612, 396);
                               primaryStage.setTitle("Listacc");
                               primaryStage.setScene(scene);
                               primaryStage.show();
        }catch(Exception exc){exc.printStackTrace();}
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
