using System.Threading.Tasks;
using ListaccFinance.API.Data.Model;
using ListaccFinance.API.ViewModels;

namespace ListaccFinance.API.Interfaces
{

    public interface ITokenGenerator
    {
        Task<string> GenerateToken(DesktopClient i);

        Task<string> GenerateToken(UserLogin u, int ID);
    }
}