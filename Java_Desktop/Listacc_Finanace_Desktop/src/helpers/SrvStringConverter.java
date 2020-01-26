/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;
import model.display.DisplayService;

/**
 *
 * @author Agozie
 */
public class SrvStringConverter extends StringConverter<DisplayService>{
    final ComboBox<DisplayService> combo;
    public SrvStringConverter(ComboBox combo){
        this.combo = combo;
    }
    @Override
    public String toString(DisplayService object) {
        return object.getName();
    }

    @Override
    public DisplayService fromString(String string) {
        return combo.getItems().stream().filter(ap -> 
            ap.getName().equals(string)).findFirst().orElse(null);
    }
}
