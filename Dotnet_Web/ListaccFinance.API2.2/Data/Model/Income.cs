using System;

namespace ListaccFinance.API.Data.Model
{
    public class Income
    {
        public int Id {get;set;}
        public string Type {get;set;}
        public DateTime Date {get;set;}
        public int ServiceID {get;set;}
        public Service Service {get;set;}
        public int ClientID {get;set;}
        public Client Client {get;set;}
        public double AmountReceived {get;set;}
        public double Discount {get;set;}
        public int PaymentType {get;set;}
        public double AmountReceivable {get;set;}
        public DateTime DateDue {get;set;}
        public int ProjectID {get;set;}
        public Project Project {get;set;}
        public int UserID {get;set;}
        public User User {get;set;}

    }
}