/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

/**
 *
 * @author Agozie
 */
public class UnitNumberChangeListener implements ChangeListener<String>{
     private final TextField textField;

    public UnitNumberChangeListener(TextField textField) {
        this.textField = textField;
    }
   
    
  @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, 
        String newValue) {
        if (!newValue.matches("\\d*")) {
            textField.setText(newValue.replaceAll("[^\\d]", ""));
        }
       try{
        int unit = Integer.parseInt( textField.getText());
        if(unit < 1)
           textField.setText(1+"");
       }catch(Exception e){}
    }  
}
