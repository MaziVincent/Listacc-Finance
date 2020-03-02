using System.Collections.Generic;
using System.Threading.Tasks;
using ListaccFinance.API.Data.Model;
using ListaccFinance.API.Data.ViewModel;
using ListaccFinance.API.SendModel;

namespace ListaccFinance.API.Interfaces
{
    public interface IUserService
    {
        Task<string> CreateUserAsync(RegisterModel reg);
        Task<string> CreateUserAsync(RegisterModel reg, int userId);
        bool IsUserExist();
        Task<bool> IsThisUserExist(string UserEmail);
        Task<User> CreateUserUploadAsync(RegisterModel reg);
        Task CreateAdmin(RegisterModel reg, int userId);
        Task Deactivate(int Id, int MyId);
        Task Activate(int Id, int MyId);
        Task<IEnumerable<User>>  ReturnUsers (SearchPaging props);
        Task<IEnumerable<User>> ReturnAllUsers(SearchPaging props);
    }
}