/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services.data;

import java.util.List;
import model.CostCategories;

/**
 *
 * @author Agozie
 */
public class CostCategoryService extends DataService {
    
     public List<CostCategories> getAllCostCategories()
    {
         return em.createQuery("SELECT a FROM CostCategories a", CostCategories.class).getResultList();
    }
}
