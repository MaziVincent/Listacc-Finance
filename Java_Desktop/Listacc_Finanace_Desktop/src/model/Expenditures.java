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
@Table(name = "Expenditures", catalog = "", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Expenditures.findAll", query = "SELECT e FROM Expenditures e"),
    @NamedQuery(name = "Expenditures.findById", query = "SELECT e FROM Expenditures e WHERE e.id = :id"),
    @NamedQuery(name = "Expenditures.findByDate", query = "SELECT e FROM Expenditures e WHERE e.date = :date"),
    @NamedQuery(name = "Expenditures.findByDescription", query = "SELECT e FROM Expenditures e WHERE e.description = :description"),
    @NamedQuery(name = "Expenditures.findByAmount", query = "SELECT e FROM Expenditures e WHERE e.amount = :amount")})
public class Expenditures implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "Id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "Date")
    private String date;
    @Column(name = "Description")
    private String description;
    @Basic(optional = false)
    @Column(name = "Amount")
    private double amount;
    @JoinColumn(name = "RecepientId", referencedColumnName = "Id")
    @ManyToOne(optional = false)
    private Clients recepientId;
    @JoinColumn(name = "CostCategoryId", referencedColumnName = "Id")
    @ManyToOne(optional = false)
    private CostCategories costCategoryId;
    @JoinColumn(name = "ProjectId", referencedColumnName = "Id")
    @ManyToOne(optional = false)
    private Projects projectId;
    @JoinColumn(name = "IssuerId", referencedColumnName = "Id")
    @ManyToOne(optional = false)
    private Users issuerId;

    public Expenditures() {
    }

    public Expenditures(Integer id) {
        this.id = id;
    }

    public Expenditures(Integer id, String date, double amount) {
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
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

    public Clients getRecepientId() {
        return recepientId;
    }

    public void setRecepientId(Clients recepientId) {
        this.recepientId = recepientId;
    }

    public CostCategories getCostCategoryId() {
        return costCategoryId;
    }

    public void setCostCategoryId(CostCategories costCategoryId) {
        this.costCategoryId = costCategoryId;
    }

    public Projects getProjectId() {
        return projectId;
    }

    public void setProjectId(Projects projectId) {
        this.projectId = projectId;
    }

    public Users getIssuerId() {
        return issuerId;
    }

    public void setIssuerId(Users issuerId) {
        this.issuerId = issuerId;
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
