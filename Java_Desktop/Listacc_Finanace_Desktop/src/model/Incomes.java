/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.Collection;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Agozie
 */
@Entity
@Table(name = "Incomes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Incomes.findAll", query = "SELECT i FROM Incomes i"),
    @NamedQuery(name = "Incomes.findById", query = "SELECT i FROM Incomes i WHERE i.id = :id"),
    @NamedQuery(name = "Incomes.findByType", query = "SELECT i FROM Incomes i WHERE i.type = :type"),
    @NamedQuery(name = "Incomes.findByDate", query = "SELECT i FROM Incomes i WHERE i.date = :date"),
    @NamedQuery(name = "Incomes.findByUnit", query = "SELECT i FROM Incomes i WHERE i.unit = :unit"),
    @NamedQuery(name = "Incomes.findByAmountReceived", query = "SELECT i FROM Incomes i WHERE i.amountReceived = :amountReceived"),
    @NamedQuery(name = "Incomes.findByDiscount", query = "SELECT i FROM Incomes i WHERE i.discount = :discount"),
    @NamedQuery(name = "Incomes.findByPaymentType", query = "SELECT i FROM Incomes i WHERE i.paymentType = :paymentType"),
    @NamedQuery(name = "Incomes.findByAmountReceivable", query = "SELECT i FROM Incomes i WHERE i.amountReceivable = :amountReceivable"),
    @NamedQuery(name = "Incomes.findByDateDue", query = "SELECT i FROM Incomes i WHERE i.dateDue = :dateDue"),
    @NamedQuery(name = "Incomes.findByOnlineEntryId", query = "SELECT i FROM Incomes i WHERE i.onlineEntryId = :onlineEntryId")})
public class Incomes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    @Column(name = "Id")
    private Integer id;
    @Column(name = "Type")
    private String type;
    @Basic(optional = false)
    @Column(name = "Date")
    private String date;
    @Basic(optional = false)
    @Column(name = "Unit")
    private int unit;
    @Basic(optional = false)
    @Column(name = "AmountReceived")
    private double amountReceived;
    @Basic(optional = false)
    @Column(name = "Discount")
    private double discount;
    @Column(name = "PaymentType")
    private String paymentType;
    @Basic(optional = false)
    @Column(name = "AmountReceivable")
    private double amountReceivable;
    @Basic(optional = false)
    @Column(name = "DateDue")
    private String dateDue;
    @Column(name = "OnlineEntryId")
    private Integer onlineEntryId;
    @JoinColumn(name = "ClientId", referencedColumnName = "Id")
    @ManyToOne(optional = false)
    private Clients client;
    @OneToMany(mappedBy = "income")
    private Collection<Incomes> incomesCollection;
    @JoinColumn(name = "IncomeId", referencedColumnName = "Id", insertable = false, updatable = false)
    @ManyToOne
    private Incomes income;
    @Column(name = "IncomeId")
    private Integer incomeId;
    @JoinColumn(name = "ServiceId", referencedColumnName = "Id")
    @ManyToOne(optional = false)
    private Services service;
    @JoinColumn(name = "UserId", referencedColumnName = "Id")
    @ManyToOne(optional = false)
    private Users user;

    public Incomes() {
    }

    public Incomes(Integer id) {
        this.id = id;
    }

    public Incomes(Integer id, String date, int unit, double amountReceived, double discount, double amountReceivable, String dateDue) {
        this.id = id;
        this.date = date;
        this.unit = unit;
        this.amountReceived = amountReceived;
        this.discount = discount;
        this.amountReceivable = amountReceivable;
        this.dateDue = dateDue;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public double getAmountReceived() {
        return amountReceived;
    }

    public void setAmountReceived(double amountReceived) {
        this.amountReceived = amountReceived;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public double getAmountReceivable() {
        return amountReceivable;
    }

    public void setAmountReceivable(double amountReceivable) {
        this.amountReceivable = amountReceivable;
    }

    public String getDateDue() {
        return dateDue;
    }

    public void setDateDue(String dateDue) {
        this.dateDue = dateDue;
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

    @XmlTransient
    public Collection<Incomes> getIncomesCollection() {
        return incomesCollection;
    }

    public void setIncomesCollection(Collection<Incomes> incomesCollection) {
        this.incomesCollection = incomesCollection;
    }

    public Incomes getIncome() {
        return income;
    }

    public void setIncome(Incomes income) {
        this.income = income;
    }

    public void setIncomeId(Integer incomeId) {
        this.incomeId = incomeId;
    }

    public Services getService() {
        return service;
    }

    public void setService(Services service) {
        this.service = service;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
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
        if (!(object instanceof Incomes)) {
            return false;
        }
        Incomes other = (Incomes) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Incomes[ id=" + id + " ]";
    }
    
}
