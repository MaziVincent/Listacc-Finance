namespace Listacc.Models
{
    public class User:Person
    
    {   
        public int Id {get;set;}
        public int Phone {get;set;}
        public string Email {get;set;}
        public string Address {get;set;}
        public string UserName {get;set;}
        public string Password {get;set;}
        public ICollection<Expenditure> Expenditures {get;set;}
        public ICollection<Income> Incomes {get;set;}
        
    }
}