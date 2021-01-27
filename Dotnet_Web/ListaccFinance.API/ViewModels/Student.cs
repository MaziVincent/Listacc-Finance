using System;
using System.ComponentModel.DataAnnotations;

namespace ListaccFinance.API.ViewModels
{
    public class Student
    {
        public int Id { get; set; }
        [Required]
        public string Gender { get; set; }
        [Required]
        public string LastName { get; set; }
        [Required]
        public string FirstName { get; set; }
        [Required]
        [Phone]
        public string PhoneNumber { get; set; }
        [Required]
        [EmailAddress]
        public string Email { get; set; }
    }

    public class StudentRegistration: Student{
        public int ProgramId { get; set; }
        public StudentRegistration()
        {
            DateRegistered = DateTime.Now;
        }
        public DateTime DateRegistered { get; set; }
        
    }
}