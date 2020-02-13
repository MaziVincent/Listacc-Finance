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

        public async Task<List<Department>> DownloadDept(List<Change> ch)
        {
            var toDownload = new List<Department>{};
            foreach (var item in ch)
            {
                var td = await _context.Departments.Where(x => x.Id == item.EntryId).FirstOrDefaultAsync();
                toDownload.Add(td);
            }

            return toDownload;
        }

        public async Task<List<Person>> DownloadPerson(List<Change> ch)
        {
            var toDownload = new List<Person> { };
            foreach (var item in ch)
            {
                var td = await _context.Persons.Where(x => x.Id == item.EntryId).FirstOrDefaultAsync();
                toDownload.Add(td);
            }

            return toDownload;
            
        }

        public async Task<List<User>> DownloadUser(List<Change> ch)
        {
            var toDownload = new List<User> { };
            foreach (var item in ch)
            {
                var td = await _context.Users.Where(x => x.Id == item.EntryId).FirstOrDefaultAsync();
                toDownload.Add(td);
            }

            return toDownload;
        }

        public async Task<List<Client>> DownloadClient(List<Change> ch)
        {
            var toDownload = new List<Client> { };
            foreach (var item in ch)
            {
                var td = await _context.Clients.Where(x => x.Id == item.EntryId).FirstOrDefaultAsync();
                toDownload.Add(td);
            }

            return toDownload;
        }

        public async Task<List<Project>> DownloadProject (List<Change> ch)
        {
            var toDownload = new List<Project> { };
            foreach (var item in ch)
            {
                var td = await _context.Projects.Where(x => x.Id == item.EntryId).FirstOrDefaultAsync();
                toDownload.Add(td);
            }

            return toDownload;
        }

        public async Task<List<CostCategory>> DownloadCost(List<Change> ch)
        {
            var toDownload = new List<CostCategory> { };
            foreach (var item in ch)
            {
                var td = await _context.CostCategories.Where(x => x.Id == item.EntryId).FirstOrDefaultAsync();
                toDownload.Add(td);
            }

            return toDownload;
        }

        public async Task<List<Expenditure>> DownloadExpenditure(List<Change> ch)
        {
            var toDownload = new List<Expenditure> { };
            foreach (var item in ch)
            {
                var td = await _context.Expenditures.Where(x => x.Id == item.EntryId).FirstOrDefaultAsync();
                toDownload.Add(td);
            }

            return toDownload;
        }

        public async Task<List<Service>> DownloadServices(List<Change> ch)
        {
            var toDownload = new List<Service> { };
            foreach (var item in ch)
            {
                var td = await _context.Services.Where(x => x.Id == item.EntryId).FirstOrDefaultAsync();
                toDownload.Add(td);
            }

            return toDownload;
        }

        public async Task<List<Income>> DownloadIncomes(List<Change> ch)
        {
            var toDownload = new List<Income> { };
            foreach (var item in ch)
            {
                var td = await _context.Incomes.Where(x => x.Id == item.EntryId).FirstOrDefaultAsync();
                toDownload.Add(td);
            }

            return toDownload;
        }

    } 
}
