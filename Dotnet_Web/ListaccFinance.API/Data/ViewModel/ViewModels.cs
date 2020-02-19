
using ListaccFinance.API.Data.Model;

namespace ListaccFinance.API.Data.ViewModel
{
    public class SyncViewModel
    {
        public DepartMentViewModel dept {get; set;}
        public PersonViewModel person{get; set;} 
        public UserViewModel user {get; set;} 
        public ClientViewModel client {get; set;}
        public ProjectViewModel project {get; set;}//= new ProjectViewModel();
        public CostCategoryViewModel costCategory{get; set;}// = new CostCategoryViewModel();
        public ServiceViewModel service{get; set;}// = new ServiceViewModel();
        public string Table {get; set;} 
    }

    public class DepartMentViewModel 
    {
        public DepartMentViewModel()
        {

        }
        public int Id { get; set; }
        public string Name { get; set; }
        public int OnlineEntryId {get; set;}
        public int ChangeId {get; set;}
    }

    public class PersonViewModel
    {
        public PersonViewModel()
        {}
        public int Id { get; set; }
        public string firstName { get; set; }
        public string LastName { get; set; }
        public string Gender { get; set; }
        public int OnlineEntryId { get; set; }
        public int ChangeId { get; set; }
    }
    
    public class UserViewModel 
    {
        public UserViewModel ()
        {}
        public string Phone { get; set; }
        public string Address { get; set; }
        public string Password { get; set; }
        public int DepartmentId { get; set; }
        public Person person {get; set;}
        public int PersonId { get; set; }
        public string salt { get; set; }

        public int OnlineEntryId { get; set; }
        public int ChangeId { get; set; }
        public string Email {get; set;}
    }

    public class ClientViewModel
    {
        public int Id { get; set; }
        public string BusinessName { get; set; }
        public string Phone { get; set; }
        public string Email { get; set; }
        public string Address { get; set; }
        public string UId { get; set; }
        public string UId2 { get; set; }
        public double AmountReceivable { get; set; }
        public int? PersonId { get; set; }

        public int OnlineEntryId { get; set; }
        public int ChangeId { get; set; }
    }

    public class ProjectViewModel 
    {
        public ProjectViewModel()
        {}
        public int Id { get; set; }
        public string Name { get; set; }
        public string Description { get; set; }

        public int DepartmentId { get; set; }

        public int OnlineEntryId { get; set; }
        public int ChangeId { get; set; }
    }

    public class CostCategoryViewModel
    {
        public CostCategoryViewModel()
        {}
        public int Id { get; set; }
        public string Name { get; set; }
        public string Type { get; set; }
        public string Description { get; set; }
        public int OnlineEntryId { get; set; }
        public int ChangeId { get; set; }

    }

    public class ServiceViewModel
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public string Description { get; set; }
        public double Amount { get; set; }
        public int ProjectId { get; set; }

        public int OnlineEntryId { get; set; }
        public int ChangeId { get; set; }
    }
}