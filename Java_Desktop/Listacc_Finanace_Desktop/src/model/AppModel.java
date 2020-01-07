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
    
    public AppModel(final Stage stage){
        this.stage = stage;
    }
    
    public Stage getStage(){
        return stage;
    }
    
}
