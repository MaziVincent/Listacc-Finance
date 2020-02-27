/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services.net.view_model;

/**
 *
 * @author E-book
 */
public interface SyncItem {
    public void setChange(String change);
    public void setChangeTimestamp(String timestamp);
    public void setChangeUserOnlineEntryId(Integer userId);
    
    public Integer getChangeId();
}
