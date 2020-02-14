using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using AutoMapper;
using ListaccFinance.Api.Data;
using ListaccFinance.API.Data.Model;
using ListaccFinance.API.Data.ViewModel;
using ListaccFinance.API.Interfaces;
using Microsoft.EntityFrameworkCore;

namespace ListaccFinance.API.Services
{

    public class SyncService : ISyncService
    {
        
        private readonly DataContext _context;
        private readonly IMapper _mapper;

        public SyncService (DataContext context, IMapper mapper)
        {
            _context = context;
            _mapper = mapper;
        }


        // Upload is ageneric method. It handles the upload of data
        /*public async Task<string> Upload<T> (List<T> changes) where T : class
        {
            _context.Set<T>().AddRange(changes);
        }*/



        public async Task<DepartMentViewModel> DownloadDeptAsync(Change ch)
        {
            var deptCh = await _context.Departments.Where(x => x.Id == ch.EntryId).FirstOrDefaultAsync();
            
            var dept = new DepartMentViewModel()
            {
                Id = deptCh.Id,
                Name = deptCh.Name,
                ChangeId = ch.Id,

            };
            /*var dept = _mapper.Map<DepartMentViewModel>(deptCh);
            dept.ChangeId = ch.Id;*/
            return dept;
        }

        public async Task<PersonViewModel> DownloadPersonAsync(Change ch)
        {
            var perCh = await _context.Persons.Where(x => x.Id == ch.EntryId).FirstOrDefaultAsync();
            var person = _mapper.Map<PersonViewModel>(perCh);
            person.ChangeId = ch.Id;
            return person;
        }

        public async Task<UserViewModel> DownloadUserAsync(Change ch)
        {
            var userCh = await _context.Users.Where(x => x.Id == ch.EntryId).FirstOrDefaultAsync();
            var user = _mapper.Map<UserViewModel>(userCh);
            user.ChangeId = ch.Id;
            return user;

        }

        public async Task<ClientViewModel> DownloadClientAsync(Change ch)
        {
            var clientCh = await _context.Clients.Where(x => x.Id == ch.EntryId).FirstOrDefaultAsync();
            var client = _mapper.Map<ClientViewModel>(clientCh);
            client.ChangeId = ch.Id;
            return client;
        }

        public async Task<ProjectViewModel> DownloadProjectAsync (Change ch)
        {
            var projectCh = await _context.Projects.Where(x => x.Id == ch.EntryId).FirstOrDefaultAsync();
            var project = _mapper.Map<ProjectViewModel>(projectCh);
            project.ChangeId = ch.Id;
            return project;
        }

        public async Task<CostCategoryViewModel> DownloadCostAsync(Change ch)
        {
            var costCh = await _context.CostCategories.Where(x => x.Id == ch.EntryId).FirstOrDefaultAsync();
            var cost =  _mapper.Map<CostCategoryViewModel>(costCh);
            cost.ChangeId = ch.Id;
            return cost;
        }

        /*public async Task<ExpenditureViewModel> DownloadExpenditureAsync(Change ch)
        {
            var expCh = _context.Expenditures.Where(x => x.Id == ch.EntryId).FirstOrDefaultAsync();
            var expenditure =  _mapper.Map<ExpenditureViewModel>(expCh);
            expenditure.ChangeId = cd.Id;
            return expenditure;
        }*/

        public async Task<ServiceViewModel> DownloadServicesAsync(Change ch)
        {
            var serviceCh = await _context.Services.Where(x => x.Id == ch.EntryId).FirstOrDefaultAsync();
            var service =  _mapper.Map<ServiceViewModel>(serviceCh);
            service.ChangeId = ch.Id;
            return service;
        }

        /*public async Task<IncomeViewModel> DownloadIncomesAsync(Change ch)
        {
            var incomeCh = await _context.Incomes.Where(x => x.Id == ch.EntryId).FirstOrDefaultAsync();
            var income = _mapper.Map<IncomeViewModel>(incomeCh);
            income.ChangeId = ch.Id;
            return income;
        }*/

    } 


}
