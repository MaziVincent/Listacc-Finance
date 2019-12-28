
using System;

namespace Listacc.Models
{
    public class Expenditure
    {
        public int Id {get;set;}
        public string Recepient {get;set;}
        public DateTime Date {get;set;}
        public string Description {get;set;}
        public double Amount {get;set;}
        public int CostCategoryId {get;set;}
        public CostCategory CostCategory {get;set;}
        public int ProjectId {get;set;}
        public Project Project {get;set;}
        public int UserId {get;set;}
        public User Issuer {get;set;}

    }
}