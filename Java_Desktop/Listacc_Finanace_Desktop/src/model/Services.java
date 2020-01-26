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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "Services")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Services.findAll", query = "SELECT s FROM Services s"),
    @NamedQuery(name = "Services.findById", query = "SELECT s FROM Services s WHERE s.id = :id"),
    @NamedQuery(name = "Services.findByName", query = "SELECT s FROM Services s WHERE s.name = :name"),
    @NamedQuery(name = "Services.findByDescription", query = "SELECT s FROM Services s WHERE s.description = :description"),
    @NamedQuery(name = "Services.findByAmount", query = "SELECT s FROM Services s WHERE s.amount = :amount")})
public class Services implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    @Column(name = "Id")
    private Integer id;
    @Column(name = "Name")
    private String name;
    @Column(name = "Description")
    private String description;
    @Basic(optional = false)
    @Column(name = "Amount")
    private double amount;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "serviceId")
    private Collection<Incomes> incomesCollection;
    @JoinColumn(name = "ProjectId", referencedColumnName = "Id")
    @ManyToOne(optional = false)
    private Projects projectId;

    public Services() {
    }

    public Services(Integer id) {
        this.id = id;
    }

    public Services(Integer id, double amount) {
        this.id = id;
        this.amount = amount;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @XmlTransient
    public Collection<Incomes> getIncomesCollection() {
        return incomesCollection;
    }

    public void setIncomesCollection(Collection<Incomes> incomesCollection) {
        this.incomesCollection = incomesCollection;
    }

    public Projects getProjectId() {
        return projectId;
    }

    public void setProjectId(Projects projectId) {
        this.projectId = projectId;
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
        if (!(object instanceof Services)) {
            return false;
        }
        Services other = (Services) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Services[ id=" + id + " ]";
    }
    
}
