/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;
import model.CostCategories;
import model.display.DisplayProject;

/**
 *
 * @author Agozie
 */
public class CostCatStringConverter extends StringConverter<CostCategories> {
     final ComboBox<CostCategories> combo;
     
    public CostCatStringConverter(ComboBox combo){
        this.combo = combo;
    }
    
    @Override
    public String toString(CostCategories object) {
        return object.getName();
    }

    @Override
    public CostCategories fromString(String string) {
        return combo.getItems().stream().filter(ap -> 
            ap.getName().equals(string)).findFirst().orElse(null);
    }
}
