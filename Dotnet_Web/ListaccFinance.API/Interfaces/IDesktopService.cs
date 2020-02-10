using System.Threading.Tasks;
using ListaccFinance.API.ViewModels;

namespace ListaccFinance.API.Interfaces
{
    public interface IDesktopService
    {
        Task<string> CreateDesktopClientAsync(DesktopCreateModel d);
    }
}