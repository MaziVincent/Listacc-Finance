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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import model.Changes;
import services.data.ChangeService;
import services.data.ClientService;
import services.data.CostCategoryService;
import services.data.DepartmentService;
import services.data.ExpenditureService;
import services.data.IncomeService;
import services.data.ProjectService;
import services.data.ServiceService;
import services.net.view_model.SyncDownloadEntryViewModel;
import services.net.view_model.SyncInfo;
import services.net.view_model.UploadResponseViewModel;

/**
 *
 * @author E-book
 */
public class SynchronizationUploadService extends AsyncTask<Void, String, Void>  { // Params, Progress, Result
    
    private BooleanProperty connectionDisplayPro, syncDisplayPro;
    private ChangeService changeService;
    
    public SynchronizationUploadService(BooleanProperty connectionDisplayPro, BooleanProperty syncDisplayPro){
        this.connectionDisplayPro = connectionDisplayPro;
        this.syncDisplayPro = syncDisplayPro;
    }
    
    @Override
    public void onPreExecute() {
        changeService = new ChangeService();
        //This method runs on UI Thread before background task has started
        
        //activitySp.set("Syncing is in process...");
        // connectionStatus.setTextFill(Color.GREEN);
        // toggleLabel(true);
    }
    
    @Override
    public Void doInBackground(Void... params) {
        //This method runs on background thread
        if(Network.isConnected() && !Network.isSyncingUp){
            
            // signify that operation is in progress so no other sync begins
            Network.isSyncingUp = true;
            
            publishProgress("Syncing is in process...");
            
            // get token
            // Network.token = Network.getTokenUsingHash(Network.login);
            
            // upload according to data hierarchy
            uploadData();
            
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
        if(!Network.isSyncingDown && !Network.isSyncingUp) 
            toggleLabel(false);
            //updateStatusLabel(""); // clear label if download is not on the way
    }
    
    @Override
    public void progressCallback(String... params) {
        //This method update your UI Thread during the execution of background thread
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
    
    private void uploadData(){
        boolean continueUpload;
        
        do {
            // get full info for each unpushed change
            List<SyncDownloadEntryViewModel> syncValues = new ArrayList<>();
        
            // get unpushed changes from changes table
            List<Changes> changes = changeService.getUnpushedChanges();
            continueUpload = changes.size() == 10;

            // process and separate into correct objects
            for(Changes ch: changes){
                SyncDownloadEntryViewModel obj = new SyncDownloadEntryViewModel();
                switch(ch.getTableName())
                {
                    case "Departments":
                        DepartmentService dService = new DepartmentService();
                        obj.setDepartment(dService.getDepartmentByChange(ch));
                        dService.close();
                        break;
                    // case "Persons":
                        // obj.person = await _sservice.DownloadPersonAsync(ch);
                        // break;
                    case "Clients":
                        ClientService cService = new ClientService();
                        obj.setClient(cService.getClientByChange(ch));
                        cService.close();
                        break;
                    case "Projects":
                        ProjectService pService = new ProjectService();
                        obj.setProject(pService.getProjectByChange(ch));
                        pService.close();
                        break;
                    case "CostCategories":
                        CostCategoryService ccService = new CostCategoryService();
                        obj.setCostCategory(ccService.getCostCategoryByChange(ch));
                        ccService.close();
                        break;
                    case "Expenditures":
                        ExpenditureService eService = new ExpenditureService();
                        obj.setExpenditure(eService.getExpenditureByChange(ch));
                        eService.close();
                        break;
                    case "Services":
                        ServiceService sService = new ServiceService();
                        obj.setService(sService.getServiceByChange(ch));
                        sService.close();
                        break;
                    case "Incomes":
                        IncomeService iService = new IncomeService();
                        obj.setIncome(iService.getIncomeByChange(ch));
                        iService.close();
                        break;
                }
                obj.setTable(ch.getTableName());
                syncValues.add(obj);
            }
            
            if(syncValues.size() > 0)
            {
                // get synchronization version
                SyncInfo syncInfo = SyncInfo.getLastSyncInfo();

                // upload information
                Pair<Boolean, String> result = Network.uploadData(syncInfo, syncValues);

                // set online entry id for newly created items
                if(result.first){
                    Gson g = new Gson();

                    List<UploadResponseViewModel> response = g.fromJson(result.second, new TypeToken<List<UploadResponseViewModel>>(){}.getType());
                    processUploadResponse(response);

                    // mark change as pushed
                    changeService.updateChangesAsPushed(changes);
                }
                else {
                    continueUpload = false;
                }
            }
        }
        while(continueUpload);
        
    }
    
    private void processUploadResponse(List<UploadResponseViewModel> response){
        for(UploadResponseViewModel ch: response){
            switch(ch.getTable())
            {
                case "Departments":
                    DepartmentService dService = new DepartmentService();
                    dService.updateEntryAsSynced(ch);
                    dService.close();
                    break;
                /*case "Persons":
                    obj.person = await _sservice.DownloadPersonAsync(ch);
                    break;*/
                case "Clients":
                    ClientService cService = new ClientService();
                    cService.updateEntryAsSynced(ch);
                    cService.close();
                    break;
                case "Projects":
                    ProjectService pService = new ProjectService();
                    pService.updateEntryAsSynced(ch);
                    pService.close();
                    break;
                case "CostCategories":
                    CostCategoryService ccService = new CostCategoryService();
                    ccService.updateEntryAsSynced(ch);
                    ccService.close();
                    break;
                case "Expenditures":
                    ExpenditureService eService = new ExpenditureService();
                    eService.updateEntryAsSynced(ch);
                    eService.close();
                    break;
                case "Services":
                    ServiceService sService = new ServiceService();
                    sService.updateEntryAsSynced(ch);
                    sService.close();
                    break;
                case "Incomes":
                    IncomeService iService = new IncomeService();
                    iService.updateEntryAsSynced(ch);
                    iService.close();
                    break;
            }
        }
    }
    
    private void cleanUpUploadOperations(){
        // close database connection
        changeService.close();
        
        // set time of sync
        SyncInfo.saveLastUpdateTimestamp(new Date().getTime() + "");
        
    }
}
