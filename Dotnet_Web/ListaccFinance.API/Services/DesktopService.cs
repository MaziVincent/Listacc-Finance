using System.Threading.Tasks;
using ListaccFinance.Api.Data;
using ListaccFinance.API.Data.Model;
using ListaccFinance.API.Interfaces;
using ListaccFinance.API.ViewModels;

namespace ListaccFinance.API.Services

{
    public class DesktopService : IDesktopService
    {

        private readonly DataContext _context;

        public DesktopService(DataContext context)
        {
            _context = context;
        }
        public async Task<string> CreateDesktopClientAsync(DesktopCreateModel d)
        {
            var newD = new DesktopClient(){
                ClientName = d.ClientName,
                ClientMacAddress = d.ClientMacAddress,
                ClientType = d.ClientType,
            };
            await _context.DesktopClients.AddAsync(newD);
            await _context.SaveChangesAsync();
            return "done";
        }
    }
}