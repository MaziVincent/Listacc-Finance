/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services.data;

import java.util.List;
import javax.persistence.NoResultException;
import model.CostCategories;
import model.Projects;

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
    
     public boolean costCategoryNameExists(String name, int id)
    {
        
        List<CostCategories> department = (List<CostCategories>) em.createQuery("SELECT q FROM CostCategories q  where upper(q.name)=:name AND q.id !="+id)
            .setParameter("name",name.toUpperCase())
            //.setParameter(id, id)
                .getResultList();
        
        return department.size() > 0;
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
    
    public boolean updateCategories( String name, String description, String type, int id){
        try{
            em.getTransaction().begin();
            CostCategories costcat = (CostCategories) em.createNamedQuery("CostCategories.findById").setParameter("id", id).getSingleResult();
            costcat.setDescription(description);
            costcat.setName(name);
            costcat.setType(type);
            em.persist(costcat);
            em.getTransaction().commit();
            em.close();
            return true;
           
        }catch(NoResultException ex){
            return false;
        }catch(Exception exc){
            exc.printStackTrace();
        }
      return  false;   
     }
}
