using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using ListaccFinance.API.Data.Model;
using Microsoft.AspNetCore.Identity;

namespace ListaccFinance.API.Data.Model
{
    public class User: IdentityUser<int>
    
    {   
        public User() 
        {
            
        }

        public string Phone {get;set;}
        public string Address {get;set;}
        
        [NotMapped]
        public string Password {get;set;}
        public ICollection<Expenditure> Expenditures {get;set;}
        public ICollection<Income> Incomes {get;set;}
        public Department Department { get; set; }
        public int DepartmentId { get; set; }
        public Person Person { get; set; }
        public int PersonId { get; set; }
        public string salt {get; set;}
    }
}