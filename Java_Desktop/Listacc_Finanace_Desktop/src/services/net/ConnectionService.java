/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services.net;

import com.victorlaerte.asynctask.AsyncTask;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 *
 * @author E-book
 */
public class ConnectionService extends AsyncTask<SimpleStringProperty, String, SimpleBooleanProperty>  {
    private SimpleStringProperty status;
    private final SimpleObjectProperty<Paint> color;
    private final SimpleBooleanProperty bool = new SimpleBooleanProperty();
    
    public ConnectionService(SimpleObjectProperty color){
        this.color = color;
    }
    
    @Override
    public void onPreExecute() {
    
        //This method runs on UI Thread before background task has started
      //  this.controller.updateProgressLabel("Starting Download")
    }
    
    @Override
    public SimpleBooleanProperty  doInBackground(SimpleStringProperty...status) {
        //This method runs on background thread
        this.status = status[0];
        if(Network.isConnected())
        {
            bool.set(true);
        }
        else bool.set(false);
        
        return bool;
    }
    
    @Override
    public void onPostExecute(SimpleBooleanProperty param) {
        if(param.get())
        {
            status.set("Connected");
            color.set(Color.GREEN);
        }
        else
        {
            status.set("Not Connected");
            color.set(Color.RED);
        }
    }
    
    @Override
    public void progressCallback(String... params) {
        //This method update your UI Thread during the execution of background thread
     //   double progress = (double)params[0]
       // this.controller.updateProgress(progress);
    }
}
