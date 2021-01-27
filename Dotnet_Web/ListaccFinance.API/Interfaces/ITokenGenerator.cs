using System.Threading.Tasks;
using ListaccFinance.API.Data.Model;
using ListaccFinance.API.SendModel;
using ListaccFinance.API.ViewModels;

namespace ListaccFinance.API.Interfaces
{

    public interface ITokenGenerator
    {
        Task<string> GenerateToken(DesktopClient i, int userId, string type);

        Task<string> GenerateToken(UserLogin u, int ID, string type);
        Task<string> GenerateStudentRegistrationToken(StudentRegistration stdReg);
        //Task<string> GenerateRefresh(string RefreshToken);
    }
}