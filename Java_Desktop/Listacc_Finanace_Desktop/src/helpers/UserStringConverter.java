/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;
import model.Users;
import model.display.DisplayUser;

/**
 *
 * @author Agozie
 */
public class UserStringConverter extends StringConverter <DisplayUser>  {
    final ComboBox<DisplayUser> combo;
    public UserStringConverter(ComboBox combo){
        this.combo = combo;
    }
    
    @Override
    public String toString(DisplayUser object) {
        return object.getFirstName() + " " +object.getLastName();
    }

    @Override
    public DisplayUser fromString(String string) {
        return combo.getItems().stream().filter(ap -> 
            (ap.getFirstName() + " " + ap.getLastName()).equals(string)).findFirst().orElse(null);
    }
}
