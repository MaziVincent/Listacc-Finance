namespace Listacc.Models
{
    public class Service
    {
        public int Id {get;set;}
        public string Name {get;set;}
        public string Description {get;set;}
        public double Amount {get;set;}
        public int ProjectId {get;set;}
        public Project Project {get;set;}
        public ICollection<Income> Incomes {get;set;}
        
        
    }
}