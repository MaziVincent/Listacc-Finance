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
@Table(name = "Expenditures")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Expenditures.findAll", query = "SELECT e FROM Expenditures e"),
    @NamedQuery(name = "Expenditures.findById", query = "SELECT e FROM Expenditures e WHERE e.id = :id"),
    @NamedQuery(name = "Expenditures.findByDate", query = "SELECT e FROM Expenditures e WHERE e.date = :date"),
    @NamedQuery(name = "Expenditures.findByDescription", query = "SELECT e FROM Expenditures e WHERE e.description = :description"),
    @NamedQuery(name = "Expenditures.findByAmount", query = "SELECT e FROM Expenditures e WHERE e.amount = :amount"),
    @NamedQuery(name = "Expenditures.findByOnlineEntryId", query = "SELECT e FROM Expenditures e WHERE e.onlineEntryId = :onlineEntryId")})
public class Expenditures implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    @Column(name = "Id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "Date")
    private Long date;
    @Column(name = "Description")
    private String description;
    @Basic(optional = false)
    @Column(name = "Amount")
    private double amount;
    @Column(name = "OnlineEntryId")
    private Integer onlineEntryId;
    @JoinColumn(name = "ClientId", referencedColumnName = "Id")
    @ManyToOne(optional = false)
    private Clients client;
    @JoinColumn(name = "CostCategoryId", referencedColumnName = "Id")
    @ManyToOne(optional = false)
    private CostCategories costCategory;
    @JoinColumn(name = "ProjectId", referencedColumnName = "Id")
    @ManyToOne(optional = false)
    private Projects project;
    @JoinColumn(name = "IssuerId", referencedColumnName = "Id")
    @ManyToOne(optional = false)
    private Users issuer;

    public Expenditures() {
    }

    public Expenditures(Integer id) {
        this.id = id;
    }

    public Expenditures(Integer id, Long date, double amount) {
        this.id = id;
        this.date = date;
        this.amount = amount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
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

    public Integer getOnlineEntryId() {
        return onlineEntryId;
    }

    public void setOnlineEntryId(Integer onlineEntryId) {
        this.onlineEntryId = onlineEntryId;
    }

    public Clients getClient() {
        return client;
    }

    public void setClient(Clients client) {
        this.client = client;
    }

    public CostCategories getCostCategory() {
        return costCategory;
    }

    public void setCostCategory(CostCategories costCategory) {
        this.costCategory = costCategory;
    }

    public Projects getProject() {
        return project;
    }

    public void setProject(Projects project) {
        this.project = project;
    }

    public Users getIssuer() {
        return issuer;
    }

    public void setIssuer(Users issuer) {
        this.issuer = issuer;
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
        if (!(object instanceof Expenditures)) {
            return false;
        }
        Expenditures other = (Expenditures) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Expenditures[ id=" + id + " ]";
    }
    
}
