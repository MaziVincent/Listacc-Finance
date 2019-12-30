/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listacc_finanace_desktop;

import controllers.MaiinUI;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.AppModel;

/**
 *
 * @author Agozie
 */
public class Listacc_Finanace_Desktop extends Application {
    final AppModel model = new AppModel() ;
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
        FXMLLoader firstLoader = new FXMLLoader(getClass().getResource("/fxml/MainUI.fxml"));
                               firstLoader.setController(new MaiinUI(model));
                               Parent root = firstLoader.load();
                               Scene scene = new Scene(root, 1366, 768);
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
