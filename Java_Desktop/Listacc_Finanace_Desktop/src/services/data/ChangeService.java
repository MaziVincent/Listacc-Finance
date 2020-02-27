/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services.data;

import controllers.MaiinUI;
import java.util.List;
import javax.persistence.Query;
import model.Changes;
import model.Clients;
import model.CostCategories;
import model.Departments;
import model.Expenditures;
import model.Incomes;
import model.Persons;
import model.Projects;
import model.Services;
import model.Users;
import services.net.view_model.PushedStatus;

/**
 *
 * @author E-book
 */
public class ChangeService extends DataService {
    enum ChangeType{
        CREATE, EDIT, DELETE
    };
    
    private final static int NUMBER_OF_ITEMS_TO_PUSH = 10;
    
    // Create Operations
    public boolean insertCreateChange(Clients client){
        return insertCreateChange(new Changes(Clients.class.getSimpleName(), client.getId()));
    }
    
    public boolean insertCreateChange(CostCategories costCategories){
        return insertCreateChange(new Changes(CostCategories.class.getSimpleName(), costCategories.getId()));
    }
    
    public boolean insertCreateChange(Departments departments){
        return insertCreateChange(new Changes(Departments.class.getSimpleName(), departments.getId()));
    }
    
    public boolean insertCreateChange(Expenditures expenditures){
        return insertCreateChange(new Changes(Expenditures.class.getSimpleName(), expenditures.getId()));
    }
    
    public boolean insertCreateChange(Incomes incomes){
        return insertCreateChange(new Changes(Incomes.class.getSimpleName(), incomes.getId()));
    }
    
    /*public boolean insertCreateChange(Persons persons){
        return insertCreateChange(new Changes(Persons.class.getSimpleName(), persons.getId()));
    }*/
    
    public boolean insertCreateChange(Projects projects){
        return insertCreateChange(new Changes(Projects.class.getSimpleName(), projects.getId()));
    }
    
    public boolean insertCreateChange(Services service){
        return insertCreateChange(new Changes(Services.class.getSimpleName(), service.getId()));
    }
    
    public boolean insertCreateChange(Users userCreated){
        return insertCreateChange(new Changes(Users.class.getSimpleName(), userCreated.getId()));
    }
    
    private boolean insertCreateChange(Changes change){
        change.setChanges(ChangeType.CREATE.name());
        change.setTimeStamp(System.currentTimeMillis() + "");
        return insertChange(change);
    }
    
    
    
    // Edit Operations
    public boolean insertUpdateChange(Clients client){
        return insertUpdateChange(new Changes(Clients.class.getSimpleName(), client.getId()));
    }
    
    public boolean insertUpdateChange(CostCategories costCategories){
        return insertUpdateChange(new Changes(CostCategories.class.getSimpleName(), costCategories.getId()));
    }
    
    public boolean insertUpdateChange(Departments departments){
        return insertUpdateChange(new Changes(Departments.class.getSimpleName(), departments.getId()));
    }
    
    public boolean insertUpdateChange(Expenditures expenditures){
        return insertUpdateChange(new Changes(Expenditures.class.getSimpleName(), expenditures.getId()));
    }
    
    public boolean insertUpdateChange(Incomes incomes){
        return insertUpdateChange(new Changes(Incomes.class.getSimpleName(), incomes.getId()));
    }
    
    public boolean insertUpdateChange(Persons persons){
        return insertUpdateChange(new Changes(Persons.class.getSimpleName(), persons.getId()));
    }
    
    public boolean insertUpdateChange(Projects projects){
        return insertUpdateChange(new Changes(Projects.class.getSimpleName(), projects.getId()));
    }
    
    public boolean insertUpdateChange(Services service){
        return insertUpdateChange(new Changes(Services.class.getSimpleName(), service.getId()));
    }
    
    public boolean insertUpdateChange(Users userCreated){
        return insertUpdateChange(new Changes(Users.class.getSimpleName(), userCreated.getId()));
    }
    
    private boolean insertUpdateChange(Changes change){
        change.setChanges(ChangeType.EDIT.name());
        change.setTimeStamp(System.currentTimeMillis() + "");
        return insertChange(change);
    }
    
    private boolean insertChange(Changes change){
       try{
            // get currently logged in user
            Users user = MaiinUI.loginUser;
            change.setUser(user);
           
            // save change entry
            em.getTransaction().begin();
            em.persist(change);
            em.getTransaction().commit();
            em.close();
        
            return true;
        }catch(Exception exc){
            return false;
        }
    }
    
    
    // Retrieve pending changes
    public List<Changes> getUnpushedChanges(){    
        try{
            Query query = em.createQuery("SELECT new model.Changes(c.id, c.tableName, c.entryId, c.changes, c.timeStamp, c.user.onlineEntryId) FROM Changes c "
                    + "WHERE c.pushed = :pushedState").setMaxResults(NUMBER_OF_ITEMS_TO_PUSH);
            query.setParameter("pushedState", PushedStatus.False.getValue());
            List<Changes> changeList = (List<Changes>)query.getResultList();

            return changeList;
        }
        catch(Exception exc){
            exc.getMessage();
        }
        return null;
    }
    
    /*public List<Changes> getUnpushedChangeEntries(){    
        try{
            Query query = em.createQuery( "SELECT new model.Changes(c.table, c.entryId, c.changes) FROM Changes c "
                    + "WHERE c.table = " + Departments.class.getSimpleName() + " AND c.pushed = :pushedState" );
            query.setParameter("pushedState", PushedStatus.False.getValue());
            List<Changes> chnageList = (List<Changes>)query.getResultList( );

            return chnageList;
        }
        catch(Exception exc){exc.getMessage();}
        return null;
    }*/
    
    
    // Handle Pushed Data
    public boolean updateChangesAsPushed(List<Changes> changes)
    {
        try{
            em.getTransaction().begin();
            Query q = em.createNamedQuery("Changes.findById");
            for(Changes entry: changes){
                // get entry
                q.setParameter("id", entry.getId());
                List<Changes> a = (List<Changes>)q.getResultList();
                
                // modify entry
                if(a.size() > 0 ){
                    a.get(0).setPushed(PushedStatus.True.getValue()); // true
                    em.merge(a.get(0));
                }
            }
            
            em.getTransaction().commit();
        
            return true;
        }
        catch(Exception exc){
            return false;
        }
    }
    
    
    public void close(){
        em.close();
    }
}
