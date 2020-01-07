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
@Table(name = "Clients", catalog = "", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Clients.findAll", query = "SELECT c FROM Clients c"),
    @NamedQuery(name = "Clients.findById", query = "SELECT c FROM Clients c WHERE c.id = :id"),
    @NamedQuery(name = "Clients.findByBusinessName", query = "SELECT c FROM Clients c WHERE c.businessName = :businessName"),
    @NamedQuery(name = "Clients.findByPhone", query = "SELECT c FROM Clients c WHERE c.phone = :phone"),
    @NamedQuery(name = "Clients.findByEmail", query = "SELECT c FROM Clients c WHERE c.email = :email"),
    @NamedQuery(name = "Clients.findByAddress", query = "SELECT c FROM Clients c WHERE c.address = :address"),
    @NamedQuery(name = "Clients.findByUId", query = "SELECT c FROM Clients c WHERE c.uId = :uId"),
    @NamedQuery(name = "Clients.findByUId2", query = "SELECT c FROM Clients c WHERE c.uId2 = :uId2"),
    @NamedQuery(name = "Clients.findByAmountReceivable", query = "SELECT c FROM Clients c WHERE c.amountReceivable = :amountReceivable")})
public class Clients implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "Id")
    private Integer id;
    @Column(name = "BusinessName")
    private String businessName;
    @Basic(optional = false)
    @Column(name = "Phone")
    private int phone;
    @Column(name = "Email")
    private String email;
    @Column(name = "Address")
    private String address;
    @Column(name = "UId")
    private String uId;
    @Column(name = "UId2")
    private String uId2;
    @Basic(optional = false)
    @Column(name = "AmountReceivable")
    private double amountReceivable;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "clientID")
    private Collection<Incomes> incomesCollection;
    @JoinColumn(name = "PersonId", referencedColumnName = "Id")
    @ManyToOne
    private Persons personId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "recepientId")
    private Collection<Expenditures> expendituresCollection;

    public Clients() {
    }

    public Clients(Integer id) {
        this.id = id;
    }

    public Clients(Integer id, int phone, double amountReceivable) {
        this.id = id;
        this.phone = phone;
        this.amountReceivable = amountReceivable;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUId() {
        return uId;
    }

    public void setUId(String uId) {
        this.uId = uId;
    }

    public String getUId2() {
        return uId2;
    }

    public void setUId2(String uId2) {
        this.uId2 = uId2;
    }

    public double getAmountReceivable() {
        return amountReceivable;
    }

    public void setAmountReceivable(double amountReceivable) {
        this.amountReceivable = amountReceivable;
    }

    @XmlTransient
    public Collection<Incomes> getIncomesCollection() {
        return incomesCollection;
    }

    public void setIncomesCollection(Collection<Incomes> incomesCollection) {
        this.incomesCollection = incomesCollection;
    }

    public Persons getPersonId() {
        return personId;
    }

    public void setPersonId(Persons personId) {
        this.personId = personId;
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
        if (!(object instanceof Clients)) {
            return false;
        }
        Clients other = (Clients) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Clients[ id=" + id + " ]";
    }
    
}
