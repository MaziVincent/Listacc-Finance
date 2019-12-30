/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.Glow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import model.AppModel;

/**
 *
 * @author Agozie
 */
public class MaiinUI implements Initializable, ActionListener {
    
      
        public MaiinUI(AppModel model){
            this.model = model;
        }
        
        @Override
        public void initialize(URL url, ResourceBundle rb) {
            adminPane.visibleProperty().bind(adminDisplayProp);
            incomePane.visibleProperty().bind(incomeDisplayProp);
            expenditurePane.visibleProperty().bind(expenditureDisplayProp);
            settingsPane.visibleProperty().bind(settingsDisplayProp);
        }
        @Override
	public void actionPerformed(ActionEvent e){
            
        }
        @FXML
        private void switchView(MouseEvent event)
        {
            try{
                 Glow glow = new Glow();
                 glow.setInput(new Bloom());
                 adminDisplayProp.set(false);
                 incomeDisplayProp.set(false);
                 expenditureDisplayProp.set(false);
                 settingsDisplayProp.set(false);
                 adminLabel.setEffect(null);
                 incomeLabel.setEffect(null);
                 expenditureLabel.setEffect(null);
                 settingsLabel.setEffect(null);
                 
            String sourceID = ((Label)event.getSource()).getId();
            switch(sourceID){
               
                case "adminLabel":
                    adminLabel.setEffect(glow);
                    adminDisplayProp.set(true);
                    break;
                case "incomeLabel":
                    incomeLabel.setEffect(glow);
                    incomeDisplayProp.set(true);
                    break;
                case "expenditureLabel":
                    expenditureLabel.setEffect(glow);
                    expenditureDisplayProp.set(true);
                    break;
                case "settingsLabel":
                    settingsLabel.setEffect(glow);
                    settingsDisplayProp.set(true);
                default:;
            }
            }catch(Exception ex){}
        }
         final AppModel model;
         @FXML
         TabPane adminPane;
         @FXML
         AnchorPane incomePane;
         @FXML
         AnchorPane expenditurePane;
         @FXML
         TabPane settingsPane;
         @FXML
         Label adminLabel;
         @FXML
         Label incomeLabel;
         @FXML
         Label expenditureLabel;
         @FXML
         Label settingsLabel;
         @FXML
        
        private BooleanProperty adminDisplayProp = new SimpleBooleanProperty();
        private BooleanProperty incomeDisplayProp = new SimpleBooleanProperty();
        private BooleanProperty expenditureDisplayProp = new SimpleBooleanProperty();
        private BooleanProperty settingsDisplayProp = new SimpleBooleanProperty();
        
}
