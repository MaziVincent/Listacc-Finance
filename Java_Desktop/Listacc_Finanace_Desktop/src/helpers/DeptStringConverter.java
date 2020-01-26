/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;
import model.Departments;

/**
 *
 * @author Agozie
 */
public class DeptStringConverter extends StringConverter<Departments> {
    final ComboBox<Departments> combo;
    public DeptStringConverter(ComboBox combo){
        this.combo = combo;
    }
    @Override
    public String toString(Departments object) {
        return object.getName();
    }

    @Override
    public Departments fromString(String string) {
        return combo.getItems().stream().filter(ap -> 
            ap.getName().equals(string)).findFirst().orElse(null);
    }
}
