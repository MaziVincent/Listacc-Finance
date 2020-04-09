using System.Collections.Generic;
using System.Threading.Tasks;
using ListaccFinance.API.Data.Model;
using ListaccFinance.API.Data.ViewModel;
using ListaccFinance.API.Services;

namespace ListaccFinance.API.Interfaces
{
    public interface IDeptService
    {
        //Task<List<DeptView>> ReturnAllDeptViews();
        Task<List<Department>> ReturnAllDepts();
        Task EditDepartment(int Id, string newName);
        Task CreateDepartment(string name);
        Task<bool> IsDeptExist(string name);
        Task<PagedList<Department>> ReturnPagedUserList(SearchDept props);
    }
}
