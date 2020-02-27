/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services.net.view_model;

import services.net.Network;

/**
 *
 * @author E-book
 */
public class LoginViewModel{
    private String emailAddress;
    private String password;
    // private String passwordHash;
    private String clientName, clientType, clientMacAddress;
    
    public LoginViewModel(String emailAddress, String password) {
        this.emailAddress = emailAddress;
        this.password = password;
        // this.passwordHash = PasswordHasher.getHash(password);
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    
    /*public String getPasswordHash() {
        return passwordHash;
    }*/
    
    public static LoginViewModel getFullDetails(LoginViewModel model){
        model.clientName = Network.GetHostName();
        String macAddress = Network.getMacAddress2();
        model.clientMacAddress = macAddress == null ? Network.GetAddress("mac") : macAddress;
        if(model.clientMacAddress == null || model.clientMacAddress.isEmpty()) return null;
        model.clientType = "PC";
        
        return model;
    }
}
