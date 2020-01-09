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
@Table(name = "Incomes", catalog = "", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Incomes.findAll", query = "SELECT i FROM Incomes i"),
    @NamedQuery(name = "Incomes.findById", query = "SELECT i FROM Incomes i WHERE i.id = :id"),
    @NamedQuery(name = "Incomes.findByType", query = "SELECT i FROM Incomes i WHERE i.type = :type"),
    @NamedQuery(name = "Incomes.findByDate", query = "SELECT i FROM Incomes i WHERE i.date = :date"),
    @NamedQuery(name = "Incomes.findByAmountReceived", query = "SELECT i FROM Incomes i WHERE i.amountReceived = :amountReceived"),
    @NamedQuery(name = "Incomes.findByDiscount", query = "SELECT i FROM Incomes i WHERE i.discount = :discount"),
    @NamedQuery(name = "Incomes.findByPaymentType", query = "SELECT i FROM Incomes i WHERE i.paymentType = :paymentType"),
    @NamedQuery(name = "Incomes.findByAmountReceivable", query = "SELECT i FROM Incomes i WHERE i.amountReceivable = :amountReceivable"),
    @NamedQuery(name = "Incomes.findByDateDue", query = "SELECT i FROM Incomes i WHERE i.dateDue = :dateDue")})
public class Incomes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "Id")
    private Integer id;
    @Column(name = "Type")
    private String type;
    @Basic(optional = false)
    @Column(name = "Date")
    private String date;
    @Basic(optional = false)
    @Column(name = "AmountReceived")
    private double amountReceived;
    @Basic(optional = false)
    @Column(name = "Discount")
    private double discount;
    @Basic(optional = false)
    @Column(name = "PaymentType")
    private int paymentType;
    @Basic(optional = false)
    @Column(name = "AmountReceivable")
    private double amountReceivable;
    @Basic(optional = false)
    @Column(name = "DateDue")
    private String dateDue;
    @JoinColumn(name = "ClientID", referencedColumnName = "Id")
    @ManyToOne(optional = false)
    private Clients clientID;
    @JoinColumn(name = "ProjectID", referencedColumnName = "Id")
    @ManyToOne(optional = false)
    private Projects projectID;
    @JoinColumn(name = "ServiceID", referencedColumnName = "Id")
    @ManyToOne(optional = false)
    private Services serviceID;
    @JoinColumn(name = "UserID", referencedColumnName = "Id")
    @ManyToOne(optional = false)
    private Users userID;

    public Incomes() {
    }

    public Incomes(Integer id) {
        this.id = id;
    }

    public Incomes(Integer id, String date, double amountReceived, double discount, int paymentType, double amountReceivable, String dateDue) {
        this.id = id;
        this.date = date;
        this.amountReceived = amountReceived;
        this.discount = discount;
        this.paymentType = paymentType;
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

    public int getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(int paymentType) {
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

    public Clients getClientID() {
        return clientID;
    }

    public void setClientID(Clients clientID) {
        this.clientID = clientID;
    }

    public Projects getProjectID() {
        return projectID;
    }

    public void setProjectID(Projects projectID) {
        this.projectID = projectID;
    }

    public Services getServiceID() {
        return serviceID;
    }

    public void setServiceID(Services serviceID) {
        this.serviceID = serviceID;
    }

    public Users getUserID() {
        return userID;
    }

    public void setUserID(Users userID) {
        this.userID = userID;
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