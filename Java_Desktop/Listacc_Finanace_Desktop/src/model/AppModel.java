/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;


import javafx.stage.Stage;

/**
 *
 * @author Agozie
 */
public class AppModel {
    final Stage stage;
    Users user;

    public Users getUser() {
        return user;
    }
    
    public AppModel(final Stage stage, final Users user){
        this.stage = stage;
        this.user = user;
    }
    
    public Stage getStage(){
        return stage;
    }

    public AppModel(Stage stage) {
        this.stage = stage;
    }
      
}
