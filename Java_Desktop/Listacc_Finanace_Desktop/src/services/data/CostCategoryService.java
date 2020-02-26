/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services.data;

import java.util.List;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import model.Changes;
import model.CostCategories;
import services.net.view_model.CostCategorySyncItem;
import services.net.view_model.UploadResponseViewModel;

/**
 *
 * @author Agozie
 */
public class CostCategoryService extends DataService {
    
    public List<CostCategories> getAllCostCategories()
    {
        return em.createQuery("SELECT a FROM CostCategories a", CostCategories.class).getResultList();
    }
     
    public boolean costCategoryNameExists(String name)
    {
        
        List<CostCategories> ccategory = (List<CostCategories>) em.createQuery("SELECT q FROM CostCategories q  where upper(q.name)=:name")
            .setParameter("name",name.toUpperCase()).getResultList();
        
        return ccategory.size() > 0;
    }
    
    private boolean createCostCategory(CostCategories costCategories){
       try{
            em.getTransaction().begin();
            em.persist(costCategories);
            em.getTransaction().commit();
            em.close();
        
            // insert chage
            new ChangeService().insertCreateChange(costCategories);
            
            // return
            return true;
        }catch(Exception exc){
            return false;
        }
    }
    
    public boolean createCategories( String name, String description, String type ){
        try{
            CostCategories costcat = new CostCategories();
            costcat.setDescription(description);
            costcat.setName(name);
            costcat.setType(type);
            return createCostCategory(costcat);
           
        }catch(NoResultException ex){
            return false;
        }catch(Exception exc){
            exc.printStackTrace();
        }
      return  false;   
    }
    
    public CostCategories getCostCategoryByOnlineEntryId(int onlineEntryId)
    {
        try{
            Query q = em.createNamedQuery("CostCategories.findByOnlineEntryId");
            q.setParameter("onlineEntryId", onlineEntryId);
            CostCategories a = (CostCategories)q.getSingleResult();
      
            return a;
        }catch(NoResultException ex){
            return null;
        }catch(Exception exc){
            exc.printStackTrace();
        }
        return null;
    }
    
    public CostCategorySyncItem getCostCategoryByChange(Changes ch)
    {
        try{
            Query q = em.createQuery("SELECT new services.net.view_model.CostCategorySyncItem(c.id, c.name, c.type, c.description, c.onlineEntryId) "
                    + "FROM CostCategories c WHERE c.id = :id");
            q.setParameter("id", ch.getEntryId());
            CostCategorySyncItem a = (CostCategorySyncItem)q.getSingleResult();
            a.setChange(ch.getChanges());
            a.setChange(ch.getChanges());
            a.setChangeTimestamp(ch.getTimeStamp());
            a.setChangeUserOnlineEntryId(ch.getUser() != null ? ch.getUser().getOnlineEntryId(): null);
            
            return a;
        }catch(NoResultException ex){
            return null;
        }catch(Exception exc){
            exc.printStackTrace();
        }
        return null;
    }
    
    public boolean addDownloadedEntry(CostCategorySyncItem item)
    {
        try{
            em.getTransaction().begin();
            
            CostCategories existingRecord = getCostCategoryByOnlineEntryId(item.getId());
            if(existingRecord == null){
                CostCategories newRecord = CostCategorySyncItem.map(item);
                em.persist(newRecord);
            }
            else{
                existingRecord = CostCategorySyncItem.map(existingRecord, item, item.getId());
                em.merge(existingRecord);
            }
            
            em.getTransaction().commit();
        
            return true;
        }catch(Exception exc){
            return false;
        }
    }
    
    public boolean updateEntryAsSynced(UploadResponseViewModel entry)
    {
        try{
            em.getTransaction().begin();
            Query q = em.createNamedQuery("CostCategories.findById");
            q.setParameter("id", entry.getId());
            CostCategories a = (CostCategories)q.getSingleResult();
                
            a.setOnlineEntryId(entry.getOnlineEntryId()); // true
            em.merge(a);
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
