
using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using ListaccFinance.API.Data.Model;

namespace ListaccFinance.API.Interfaces
{
        public interface ISyncService
        //<T> where T : class

        {
                Task<List<Department>> DownloadDept(List<Change> ch);
                Task<List<Person>> DownloadPerson(List<Change> ch);
                Task<List<User>> DownloadUser(List<Change> ch);
                Task<List<Client>> DownloadClient(List<Change> ch);
                Task<List<Project>> DownloadProject(List<Change> ch);
                Task<List<CostCategory>> DownloadCost(List<Change> ch);
                Task<List<Expenditure>> DownloadExpenditure(List<Change> ch);
                Task<List<Service>> DownloadServices(List<Change> ch);
                Task<List<Income>> DownloadIncomes(List<Change> ch);
        }

}
