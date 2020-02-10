/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services.net;

import android.util.Pair;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.victorlaerte.asynctask.AsyncTask;
import java.util.List;
import javafx.beans.property.StringProperty;
import model.Changes;
import model.Departments;
import services.data.ChangeService;
import services.data.DepartmentService;
import services.net.view_model.DepartmentUploadItem;
import services.net.view_model.OnlineEntryMapping;

/**
 *
 * @author E-book
 */
public class SynchronizationUploadService extends AsyncTask<Void, String, Void>  { // Params, Progress, Result
    
    private final StringProperty activitySp;
    private ChangeService changeService;
    
    public SynchronizationUploadService(StringProperty activitySp){
        this.activitySp = activitySp;
    }
    
    @Override
    public void onPreExecute() {
        changeService = new ChangeService();
        //This method runs on UI Thread before background task has started
        
        //activitySp.set("Syncing is in process...");
        // connectionStatus.setTextFill(Color.GREEN);
    }
    
    @Override
    public Void doInBackground(Void... params) {
        //This method runs on background thread
        if(Network.isConnected() && !Network.isSyncingUp){
            
            // signify that operation is in progress so no other sync begins
            Network.isSyncingUp = true;
            
            publishProgress("Syncing is in process...");
            
            // get token
            Network.token = Network.getTokenUsingHash(Network.login);
            
            // upload according to data hierarchy
            uploadDepartments();
            // uploadPersons();
            // uploadUsers();
            // uploadClients();
            // uploadProjects();
            // uploadCostCategories();
            // uploadExpenditures();
            // uploadServices();
            // uploadIncomes();
            // uploadChanges();
            
            // clean up operation
            cleanUpUploadOperations();
            
            // signify that operation has completed so another can begin
            Network.isSyncingUp = false;
        }
        return null;
    }
    
    @Override
    public void onPostExecute(Void result) {
        // update UI thread after success/failure
        if(!Network.isSyncingDown) 
            updateStatusLabel(""); // clear label if download is not on the way
    }
    
    @Override
    public void progressCallback(String... params) {
        //This method update your UI Thread during the execution of background thread
       updateStatusLabel(params[0]);
    }
    
    private void updateStatusLabel(String content){
        activitySp.set(content);
    }
    
    private void uploadDepartments(){
        // get department changes from changes table
        List<Changes> departmentChanges = changeService.getUnpushedChanges(Departments.class.toString());
        
        // get full department info for unpushed changes
        DepartmentService departmentService = new DepartmentService();
        
        List<DepartmentUploadItem> departmentList = departmentService.getUnpushedChanges(departmentChanges);
        if(departmentList != null && departmentList.size() > 0)
        {
            
            Gson g = new Gson();
                    
            // upload department information
            Pair<Boolean, String> result = Network.uploadDepartmentData(departmentList);
            
            // set online entry id for newly created items
            if(result.first){
                List<OnlineEntryMapping> mapping = g.fromJson(result.second, new TypeToken<List<OnlineEntryMapping>>(){}.getType());
                departmentService.updateNewDepartments(mapping);
            }
            
            // mark change as pushed
            changeService.updateChangesAsPushed(departmentChanges);
        }
        
        departmentService.close();
    }
    
    /*private void uploadBiometrics(){
        BiometricsService biometriceService = new BiometricsService();
        List<BiometricsUploadEntryViewModel> biometricsUploadList = biometriceService.getUnSyncedBiometrics();
        if(biometricsUploadList != null && biometricsUploadList.size() > 0)
        {                       
            // upload
            Pair<Boolean, String> result = Network.uploadBiometricsData(biometricsUploadList);

            // update db
            if(result.first){
                Gson g = new Gson();
                List<OnlineEntryMapping> mapping = g.fromJson(result.second, new TypeToken<List<OnlineEntryMapping>>(){}.getType());
                biometriceService.updateBiometricsAsSynced(mapping);
            }
        }
        biometriceService.close();
    }*/
    
    
    private void cleanUpUploadOperations(){
        // close database connection
        changeService.close();
        
        // record 
    }
}
