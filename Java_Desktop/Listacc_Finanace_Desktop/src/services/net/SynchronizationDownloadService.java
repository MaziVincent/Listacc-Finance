/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services.net;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.victorlaerte.asynctask.AsyncTask;
import controllers.MaiinUI;
import helpers.GsonHelper;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import services.data.ClientService;
import services.data.CostCategoryService;
import services.data.DepartmentService;
import services.data.ProjectService;
import services.data.ServiceService;
import services.data.UserService;
import services.net.view_model.SyncDownloadEntryViewModel;
import services.net.view_model.SyncInfo;

/**
 *
 * @author E-book
 */
public class SynchronizationDownloadService extends AsyncTask<Void, String, Void>  { // Params, Progress, Result
    
    private BooleanProperty connectionDisplayPro, syncDisplayPro;
    private MaiinUI mainUI;
    
    public SynchronizationDownloadService(){}
    
    public SynchronizationDownloadService(BooleanProperty connectionDisplayPro, 
            BooleanProperty syncDisplayPro, MaiinUI mainUI){
        this.connectionDisplayPro = connectionDisplayPro;
        this.syncDisplayPro = syncDisplayPro;
        this.mainUI = mainUI;
    }
    
    @Override
    public void onPreExecute() {
        //This method runs on UI Thread before background task has started
        // activitySp.set("Syncing is in process...");
        // color.set(Color.WHITE);
        // toggleLabel(true);
    }
    
    @Override
    public Void doInBackground(Void... params) {
        //This method runs on background thread
        if(Network.isConnected() && !Network.isSyncingDown){
            
            // signify that operation is in progress so no other sync begins
            Network.isSyncingDown = true;
            
            publishProgress("Syncing is in process...");
            
            // download updates from other tables
            downloadUpdates();
            
            // signify that operation has completed so another can begin
            Network.isSyncingDown = false;
        }
        return null;
    }
    
    @Override
    public void onPostExecute(Void result) {
        // update UI thread after success/failure
        if(!Network.isSyncingUp && !Network.isSyncingDown) {
            toggleLabel(false);
            // updateStatusLabel("");
        }
    }
    
    @Override
    public void progressCallback(String... params) {
        //This method update your UI Thread during the execution of background thread
     //   double progress = (double)params[0]
       // this.controller.updateProgress(progress);
       // updateStatusLabel(params[0]);
        toggleLabel(true);
    }
    
    private void toggleLabel(boolean show){
        if (show){
            connectionDisplayPro.set(false);
            syncDisplayPro.set(true);
        }
        else{
            connectionDisplayPro.set(true);
            syncDisplayPro.set(false);
        }
        // activitySp.set(content);
    }
    
    public boolean downloadUpdates(){
        try{
            Gson gson = GsonHelper.CUSTOM_GSON;

            
            boolean moreInfo = true;
            while(moreInfo){
                // get synchronization version
                SyncInfo syncInfo = SyncInfo.getLastSyncInfo();
                
                // download data
                String result = Network.downloadData(syncInfo);
                
                // process (create/edit data)
                if(result != null && !result.isEmpty()){
                    List<SyncDownloadEntryViewModel> changes = gson.fromJson(result, new TypeToken<List<SyncDownloadEntryViewModel>>(){}.getType());
                    if(changes.size() > 0)
                    {
                        for(SyncDownloadEntryViewModel entry: changes){
                            switch(entry.getTable()){
                                case "Departments":
                                    new DepartmentService().addDownloadedEntry(entry.getDepartment());
                                    syncInfo.setLastChangeId(entry.getDepartment().getChangeId());
                                    break;
                                // case "Persons":
                                    // new PersonService().addDownloadedEntry(entry.getPerson());
                                    // syncInfo.setLastChangeId(entry.getPerson().getChangeId());
                                    // break;
                                case "Users":
                                    new UserService().addDownloadedEntry(entry.getUser());
                                    syncInfo.setLastChangeId(entry.getUser().getChangeId());
                                    break;
                                case "Clients":
                                    new ClientService().addDownloadedEntry(entry.getClient());
                                    syncInfo.setLastChangeId(entry.getClient().getChangeId());
                                    break;
                                case "Projects":
                                    new ProjectService().addDownloadedEntry(entry.getProject());
                                    syncInfo.setLastChangeId(entry.getProject().getChangeId());
                                    break;
                                case "CostCategories":
                                    new CostCategoryService().addDownloadedEntry(entry.getCostCategory());
                                    syncInfo.setLastChangeId(entry.getCostCategory().getChangeId());
                                    break;
                                case "Services":
                                    new ServiceService().addDownloadedEntry(entry.getService());
                                    syncInfo.setLastChangeId(entry.getService().getChangeId());
                                    break;
                            }
                        }
                        
                        // Populate Tables
                        if(mainUI != null) mainUI.populateTables();
                    }
                    if(changes.size() < 10) moreInfo = false;
                }
                else
                    moreInfo = false;
                
                // save changes
                SyncInfo.saveLastChangeId(syncInfo.getLastChangeId());
            }
            
            return true;
        }
        catch(Exception ex){
            return false;
        }
    }
}
