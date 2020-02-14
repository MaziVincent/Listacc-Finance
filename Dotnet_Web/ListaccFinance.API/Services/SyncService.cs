using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using AutoMapper;
using ListaccFinance.Api.Data;
using ListaccFinance.API.Data.Model;
using ListaccFinance.API.Data.ViewModel;
using ListaccFinance.API.Interfaces;
using ListaccFinance.API.SendModel;
using Microsoft.EntityFrameworkCore;

namespace ListaccFinance.API.Services
{

    public class SyncService : ISyncService
    {
        
        private readonly DataContext _context;
        private readonly IMapper _mapper;

        private readonly IUserService _uService;

        public SyncService (DataContext context, IMapper mapper, IUserService uService)
        {
            _context = context;
            _mapper = mapper;
            _uService = uService;
        }


       /* public async Task<string> UploadAsync<T> (T syncV, Department d) where T : class
        {

            var props = syncV.GetType().GetProperties();

            foreach (var prop in props)
            {
                switch (prop.GetType().ToString())
                {
                    case "Department":
                        await AddAsync<Department>(d);
                        break;
                }
            }
            
            return "successful";
        }*/

        public async Task UploadDeptAsync(Department d) 
        {
            try
            {
                await _context.Departments.AddAsync(d);
            }
            catch (System.Exception e)
            {
                
                throw e;
            }

        }
        public async Task UploadPersonAsync(Person p)
        {
            try
            {
                await _context.Persons.AddAsync(p);
            }
            catch (System.Exception e)
            {

                throw e;
            }
        }
            public async Task UploadUserAsync(RegisterModel u)
            {
                try
                {
                    await _uService.CreateUserUploadAsync(u);
                }
                catch (System.Exception e)
                {

                    throw e;
                }
            }

        public async Task UploadClientAsync(Client c)
        {
            try
            {
                await _context.Clients.AddAsync(c);
            }
            catch (System.Exception e)
            {

                throw e;
            }
        }

        public async Task UploadProjectAsync(Project p)
        {
            try
            {
                await _context.Projects.AddAsync(p);
            }
            catch (System.Exception e)
            {

                throw e;
            }
        }

        public async Task UploadCostAsync(CostCategory c)
        {
            try
            {
                await _context.CostCategories.AddAsync(c);
            }
            catch (System.Exception e)
            {

                throw e;
            }
        }
        public async Task UploadServiceAsync(Service s)
        {
            try
            {
                await _context.Services.AddAsync(s);
            }
            catch (System.Exception e)
            {

                throw e;
            }
        }





        public async Task<DepartMentViewModel> DownloadDeptAsync(Change ch)
        {
            var deptCh = await _context.Departments.Where(x => x.Id == ch.EntryId).FirstOrDefaultAsync();
            var dept = _mapper.Map<DepartMentViewModel>(deptCh);
            dept.ChangeId = ch.Id;
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
            var userCh = await _context.Users.Where(x => x.Id == ch.EntryId).Include(x => x.Person).FirstOrDefaultAsync();
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
