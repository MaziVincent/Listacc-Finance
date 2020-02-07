using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using ListaccFinance.API.Data.Model;

namespace ListaccFinance.API.ViewModels

{
    public class SyncLoginModel
    {
        [Required]
        public string ClientName { get; set; }
        [Required]
        public string ClientMacAddress { get; set; }
        [Required]
        public string ClientType { get; set; }
    }

    public class RegisterModel
    {
        public string firstName { get; set; }
        public string LastName { get; set; }
        public string Gender { get; set; }
        public DateTime? DateOfBirth { get; set; }


        public string Phone { get; set; }
        public string Address { get; set; }
        public string Password { get; set; }

        public string Department {get; set;}
        
    }


    public class DesktopCreateModel
    {

        [Required]
        public string ClientName { get; set; }
        [Required]
        public string ClientMacAddress { get; set; }
        [Required]
        public string ClientType { get; set; }
    }
}