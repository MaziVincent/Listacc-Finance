
using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using ListaccFinance.API.Data.Model;

namespace ListaccFinance.API.Interfaces
{
        public interface ISyncService
        //<T> where T : class

        {
                Task<List<Department>> DownloadDeptAsync(List<Change> ch);
                Task<List<Person>> DownloadPersonAsync(List<Change> ch);
                Task<List<User>> DownloadUserAsync(List<Change> ch);
                Task<List<Client>> DownloadClientAsync(List<Change> ch);
                Task<List<Project>> DownloadProjectAsync(List<Change> ch);
                Task<List<CostCategory>> DownloadCostAsync(List<Change> ch);
                Task<List<Expenditure>> DownloadExpenditureAsync(List<Change> ch);
                Task<List<Service>> DownloadServicesAsync(List<Change> ch);
                Task<List<Income>> DownloadIncomesAsync(List<Change> ch);
        }

}
