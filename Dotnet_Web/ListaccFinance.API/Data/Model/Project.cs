namespace Listacc.Models
{
    public class Project
    {
        public int Id {get;set;}
        [Required]
        public string Name {get;set;}
        public string Description {get;set;}
        
        public int DepartmentId {get;set;}
        public Department Department {get;set;}
        public ICollection<Service> Services {get;set;}
        public Icollection<Expenditure> Expenditures {get;set;}
    }
}