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
public class DoubleValuChangeListener implements ChangeListener<String>{
    
    private final TextField textField;

    public DoubleValuChangeListener(TextField textField) {
        this.textField = textField;
    }
   
    
    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, 
        String newValue) {
        if (!newValue.matches("\\d*(.\\d)*")) {
            textField.setText(newValue.replaceAll("[^\\d*(.\\d)*]", ""));
        }
    }  
}
