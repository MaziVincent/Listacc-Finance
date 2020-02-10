/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Agozie
 */
@Entity
@Table(name = "Changes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Changes.findAll", query = "SELECT c FROM Changes c"),
    @NamedQuery(name = "Changes.findById", query = "SELECT c FROM Changes c WHERE c.id = :id"),
    @NamedQuery(name = "Changes.findByTable", query = "SELECT c FROM Changes c WHERE c.table = :table"),
    @NamedQuery(name = "Changes.findByEntryId", query = "SELECT c FROM Changes c WHERE c.entryId = :entryId"),
    @NamedQuery(name = "Changes.findByChanges", query = "SELECT c FROM Changes c WHERE c.changes = :changes"),
    @NamedQuery(name = "Changes.findByTimeStamp", query = "SELECT c FROM Changes c WHERE c.timeStamp = :timeStamp"),
    @NamedQuery(name = "Changes.findByPushed", query = "SELECT c FROM Changes c WHERE c.pushed = :pushed"),
    @NamedQuery(name = "Changes.findByOnlineEntryId", query = "SELECT c FROM Changes c WHERE c.onlineEntryId = :onlineEntryId")})
public class Changes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    @Column(name = "Id")
    private Integer id;
    @Column(name = "Table")
    private String table;
    @Basic(optional = false)
    @Column(name = "EntryId")
    private int entryId;
    @Column(name = "Changes")
    private String changes;
    @Basic(optional = false)
    @Column(name = "TimeStamp")
    private String timeStamp;
    @Basic(optional = false)
    @Column(name = "Pushed")
    private int pushed;
    @Basic(optional = false)
    @Column(name = "OnlineEntryId")
    private int onlineEntryId;
    @JoinColumn(name = "UserId", referencedColumnName = "Id")
    @ManyToOne(optional = false)
    private Users userId;
    /*@JoinColumn(name = "UserId", referencedColumnName = "Id", insertable = false, updatable = false)
    @ManyToOne
    private Users user;
    @Column(name = "UserId")
    private Integer userId;*/

    public Changes() {
    }

    public Changes(Integer id) {
        this.id = id;
    }

    public Changes(Integer id, int entryId, String timeStamp, int pushed, int onlineEntryId) {
        this.id = id;
        this.entryId = entryId;
        this.timeStamp = timeStamp;
        this.pushed = pushed;
        this.onlineEntryId = onlineEntryId;
    }
    
    public Changes(String table, int entryId) {
        this.table = table;
        this.entryId = entryId;
        this.pushed = 0;
    }
    
    public Changes(String table, int entryId, String change) {
        this.table = table;
        this.entryId = entryId;
        this.changes = change;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public int getEntryId() {
        return entryId;
    }

    public void setEntryId(int entryId) {
        this.entryId = entryId;
    }

    public String getChanges() {
        return changes;
    }

    public void setChanges(String changes) {
        this.changes = changes;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getPushed() {
        return pushed;
    }

    public void setPushed(int pushed) {
        this.pushed = pushed;
    }

    public int getOnlineEntryId() {
        return onlineEntryId;
    }

    public void setOnlineEntryId(int onlineEntryId) {
        this.onlineEntryId = onlineEntryId;
    }

    public Users getUserId() {
        return userId;
    }

    public void setUserId(Users userId) {
        this.userId = userId;
    }
    
    /*public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }*/

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Changes)) {
            return false;
        }
        Changes other = (Changes) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Changes[ id=" + id + " ]";
    }
    
}
