using System.Collections.Generic;
using Microsoft.AspNetCore.Identity;

namespace ListaccFinance.API.Data.Model
{
    public class User: IdentityUser<int>
    
    {   
        public int Phone {get;set;}
        public string Address {get;set;}
        public string Password {get;set;}
        public ICollection<Expenditure> Expenditures {get;set;}
        public ICollection<Income> Incomes {get;set;}
        
    }
}