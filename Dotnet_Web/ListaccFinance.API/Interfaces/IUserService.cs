using System.Threading.Tasks;
using ListaccFinance.API.Data.Model;
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
    }
}