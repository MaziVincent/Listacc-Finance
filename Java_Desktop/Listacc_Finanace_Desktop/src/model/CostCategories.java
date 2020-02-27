/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Agozie
 */
@Entity
@Table(name = "CostCategories")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CostCategories.findAll", query = "SELECT c FROM CostCategories c"),
    @NamedQuery(name = "CostCategories.findById", query = "SELECT c FROM CostCategories c WHERE c.id = :id"),
    @NamedQuery(name = "CostCategories.findByName", query = "SELECT c FROM CostCategories c WHERE c.name = :name"),
    @NamedQuery(name = "CostCategories.findByType", query = "SELECT c FROM CostCategories c WHERE c.type = :type"),
    @NamedQuery(name = "CostCategories.findByDescription", query = "SELECT c FROM CostCategories c WHERE c.description = :description"),
    @NamedQuery(name = "CostCategories.findByOnlineEntryId", query = "SELECT c FROM CostCategories c WHERE c.onlineEntryId = :onlineEntryId")})
public class CostCategories implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    @Column(name = "Id")
    private Integer id;
    @Column(name = "Name")
    private String name;
    @Column(name = "Type")
    private String type;
    @Column(name = "Description")
    private String description;
    @Column(name = "OnlineEntryId")
    private Integer onlineEntryId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "costCategory")
    private Collection<Expenditures> expendituresCollection;

    public CostCategories() {
    }

    public CostCategories(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getOnlineEntryId() {
        return onlineEntryId;
    }

    public void setOnlineEntryId(Integer onlineEntryId) {
        this.onlineEntryId = onlineEntryId;
    }

    @XmlTransient
    public Collection<Expenditures> getExpendituresCollection() {
        return expendituresCollection;
    }

    public void setExpendituresCollection(Collection<Expenditures> expendituresCollection) {
        this.expendituresCollection = expendituresCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CostCategories)) {
            return false;
        }
        CostCategories other = (CostCategories) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.CostCategories[ id=" + id + " ]";
    }
    
}
