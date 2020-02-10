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
public class FocusChangeListener implements ChangeListener<Boolean> {
    private final TextField tBox;
    
    public FocusChangeListener(TextField tbox){
    this.tBox = tbox;
    }
    
    @Override
    public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
    {
        if (!newPropertyValue)
        {
            //focus removed 
            int unit =0;
            try{
               unit  = Integer.parseInt(tBox.getText().trim());
            }catch(Exception exc){}
            if(unit < 1)
                tBox.setText("1");
        }
        
    }
}
