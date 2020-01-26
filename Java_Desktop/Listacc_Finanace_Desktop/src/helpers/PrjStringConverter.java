/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;


import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;
import model.display.DisplayProject;

/**
 *
 * @author Agozie
 */
public class PrjStringConverter extends StringConverter<DisplayProject> {
    final ComboBox<DisplayProject> combo;
    public PrjStringConverter(ComboBox combo){
        this.combo = combo;
    }
    @Override
    public String toString(DisplayProject object) {
        return object.getName();
    }

    @Override
    public DisplayProject fromString(String string) {
        return combo.getItems().stream().filter(ap -> 
            ap.getName().equals(string)).findFirst().orElse(null);
    }
}
