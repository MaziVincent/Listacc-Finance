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
public class DepartmentUploadItem {
    private int Id;
    final private Integer OnlineEntryId;
    final private String Name;
    final private String Change;

    public DepartmentUploadItem(Integer Id, Integer OnlineEntryId, String Name, String Change){
        this.Id = Id;
        this.OnlineEntryId = OnlineEntryId;
        this.Change = Change;
        this.Name = Name;
    }
}
