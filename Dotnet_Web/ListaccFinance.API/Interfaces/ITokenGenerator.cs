using System.Threading.Tasks;
using ListaccFinance.API.Data.Model;

namespace ListaccFinance.API.Interfaces
{

    public interface ITokenGenerator
    {
        Task<string> GenerateToken(DesktopClient i);
    }
}