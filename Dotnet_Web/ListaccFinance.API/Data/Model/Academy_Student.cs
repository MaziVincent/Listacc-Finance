using System;
using Microsoft.AspNetCore.Identity;

namespace ListaccFinance.API.Data.Model
{
    public class Academy_Student: IdentityUser<int>
    {
        public string Gender { get; set; }     
        public string LastName { get; set; }
        public string FirstName { get; set; }
        public string SearchParam { get; set; }
        public DateTime DateRegistered { get; set; }
        
    }
}