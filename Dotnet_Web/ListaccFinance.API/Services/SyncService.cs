using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using ListaccFinance.Api.Data;
using ListaccFinance.API.Data.Model;
using ListaccFinance.API.Interfaces;
using Microsoft.EntityFrameworkCore;

namespace ListaccFinance.API.Services
{

    public class SyncService : ISyncService
    //<T>: ISyncService<T> where T : class
    {
        
        private readonly DataContext _context;

        public SyncService (DataContext context)
        {
            _context = context;
        }

        /*public async Task<List<T>> Download<T>(List<Change> ch) where T :  class
        {
            var toDownload = _context.Set<T>().
        } */

        public async Task<List<Department>> DownloadDeptAsync(List<Change> ch)
        {
            var IDs = new List<int>();
            foreach (var i in ch)
            {
                IDs.Add(i.EntryId.Value);
            }
                var toDownload = await _context.Departments.Where(x =>IDs.Contains(x.Id)).ToListAsync();
            
            return toDownload;
        }

        public async Task<List<Person>> DownloadPersonAsync(List<Change> ch)
        {
            var IDs = new List<int>();
            foreach (var i in ch)
            {
                IDs.Add(i.EntryId.Value);
            }
            var toDownload = await _context.Persons.Where(x => IDs.Contains(x.Id)).ToListAsync();

            return toDownload;
            
        }

        public async Task<List<User>> DownloadUserAsync(List<Change> ch)
        {
            var IDs = new List<int>();
            foreach (var i in ch)
            {
                IDs.Add(i.EntryId.Value);
            }
            var toDownload = await _context.Users.Where(x => IDs.Contains(x.Id)).ToListAsync();

            return toDownload;
        }

        public async Task<List<Client>> DownloadClientAsync(List<Change> ch)
        {
            var IDs = new List<int>();
            foreach (var i in ch)
            {
                IDs.Add(i.EntryId.Value);
            }
            var toDownload = await _context.Clients.Where(x => IDs.Contains(x.Id)).ToListAsync();

            return toDownload;
        }

        public async Task<List<Project>> DownloadProjectAsync (List<Change> ch)
        {
            var IDs = new List<int>();
            foreach (var i in ch)
            {
                IDs.Add(i.EntryId.Value);
            }
            var toDownload = await _context.Projects.Where(x => IDs.Contains(x.Id)).ToListAsync();

            return toDownload;
        }

        public async Task<List<CostCategory>> DownloadCostAsync(List<Change> ch)
        {
            var IDs = new List<int>();
            foreach (var i in ch)
            {
                IDs.Add(i.EntryId.Value);
            }
            var toDownload = await _context.CostCategories.Where(x => IDs.Contains(x.Id)).ToListAsync();

            return toDownload;
        }

        public async Task<List<Expenditure>> DownloadExpenditureAsync(List<Change> ch)
        {
            var IDs = new List<int>();
            foreach (var i in ch)
            {
                IDs.Add(i.EntryId.Value);
            }
            var toDownload = await _context.Expenditures.Where(x => IDs.Contains(x.Id)).ToListAsync();

            return toDownload;
        }

        public async Task<List<Service>> DownloadServicesAsync(List<Change> ch)
        {
            var IDs = new List<int>();
            foreach (var i in ch)
            {
                IDs.Add(i.EntryId.Value);
            }
            var toDownload = await _context.Services.Where(x => IDs.Contains(x.Id)).ToListAsync();

            return toDownload;
        }

        public async Task<List<Income>> DownloadIncomesAsync(List<Change> ch)
        {
            var IDs = new List<int>();
            foreach (var i in ch)
            {
                IDs.Add(i.EntryId.Value);
            }
            var toDownload = await _context.Incomes.Where(x => IDs.Contains(x.Id)).ToListAsync();

            return toDownload;
        }

    } 
}
