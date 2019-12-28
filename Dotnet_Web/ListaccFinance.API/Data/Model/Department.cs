
using System;

namespace Listacc.Models
{
    public class Department
    {
        public int Id {get;set;}
        [Required]
        public string Name {get;set;}
        public ICollection<Project> Projects {get;set;}
    }
}