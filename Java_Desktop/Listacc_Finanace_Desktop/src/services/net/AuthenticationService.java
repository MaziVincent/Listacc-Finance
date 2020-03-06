/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services.net;

import com.victorlaerte.asynctask.AsyncTask;
import controllers.Login;
import helpers.PasswordHasher;
import javafx.scene.paint.Color;
import model.Users;
import services.data.UserService;
import services.net.view_model.LoginViewModel;
import services.net.view_model.SyncInfo;

/**
 *
 * @author E-book
 */
enum AuthenticationStatus {
        Online_Success(0), Online_Failure(1), Online_Invalid(2), Online_Unavailable(3),
        Offline_Success(4), Offline_Failure(5);
        
        private int value;
        //private static Map map = new HashMap<>();
        
        private AuthenticationStatus(int value){
            this.value = value;
        }
        
        public int getValue() {
            return value;
        }
}

public class AuthenticationService extends AsyncTask<Void, String, AuthenticationStatus> {
      
    private final Login signinController;
    private final String username;
    private final String password;
    
    public AuthenticationService(String username, String password, Login signinController){
        this.username = username;
        this.password = password;
        this.signinController = signinController;
    }
    
    @Override
    public void onPreExecute() {
        //This method runs on UI Thread before background task has started
        updateStatusLabel("Signing in...", Color.BLACK);
    }
    
    @Override
    public AuthenticationStatus doInBackground(Void... params) 
    {          
        LoginViewModel login = new LoginViewModel(username, password);
        
        // check if local user exists
        UserService userService = new UserService();
        Users user = userService.getUserByEmail(username.toLowerCase());
        userService.close();
        if(user != null){
            if(user.getStatus() == 1){
                // check if passwords match
                if(PasswordHasher.validate(password, user.getSalt(), user.getPasswordHash()))
                    return AuthenticationStatus.Offline_Success;
                else
                    return AuthenticationStatus.Offline_Failure;
            }
            else
                return AuthenticationStatus.Offline_Failure;
        }
        else{
            // attempt online log in
            publishProgress("Attempting online sign in.. ");
            
            if(Network.isConnected())
            {
                // get token from web server
                String token = Network.getToken(login);
                
                if(token.length() > 2)
                {             
                    // save token to file
                    SyncInfo.saveToken(token); 
                    
                    // get relevant info web server
                    publishProgress("Downloading required information.. ");
                    if(retrieveOnlineInfo())
                        return AuthenticationStatus.Online_Success;                      
                    else
                        return AuthenticationStatus.Online_Failure;                
                }
                else 
                    return AuthenticationStatus.Online_Invalid;
            }
            else
                return AuthenticationStatus.Online_Unavailable;
        }
    }
    
    @Override
    public void onPostExecute(AuthenticationStatus result) {
        // update UI thread after success/failure
        if(null != result){
            switch (result) {
                case Online_Failure:
                    updateStatusLabel("Problem downloading information. Check network connection and try again", Color.RED);
                    //signinController.signinStatuSP.setTextFill(Color.RED);
                    break;
                case Online_Invalid:
                case Offline_Failure:
                    updateStatusLabel("Invalid username or password", Color.RED);
                    break;
                case Online_Unavailable:
                    updateStatusLabel("Please connect to the internet.", Color.RED);
                    break;
                case Offline_Success:
                case Online_Success:
                    UserService sc = new UserService();
                    Users user = sc.getUserByEmail(username);
                    sc.close();
                    updateStatusLabel("Success", Color.BLACK);
                    signinController.loadMainUI(user);
                    break;
                default:
                    break;
            }
        }
        signinController.enableComponents();
    }
    
    @Override
    public void progressCallback(String... params) {
        //This method update your UI Thread during the execution of background thread
     //   double progress = (double)params[0]
       // this.controller.updateProgress(progress);
        updateStatusLabel(params[0], null);
    }
    
    private void updateStatusLabel(String content, Color color){
        color = color == null ? Color.BLACK : color;
        this.signinController.signinStatuSP.set(content);
        this.signinController.lblLoginStatus.setTextFill(color);
    }
    
    private boolean retrieveOnlineInfo(){
        return new SynchronizationDownloadService().downloadUpdates();
    }
      
}
