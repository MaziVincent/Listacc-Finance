
using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using ListaccFinance.API.Data.Model;
using ListaccFinance.API.Data.ViewModel;

namespace ListaccFinance.API.Interfaces
{
    public interface ISyncService
    //<T> where T : class

    {
        Task<DepartMentViewModel> DownloadDeptAsync(Change ch);
        Task<PersonViewModel> DownloadPersonAsync(Change ch);
        Task<UserViewModel> DownloadUserAsync(Change ch);
        Task<ClientViewModel> DownloadClientAsync(Change ch);
        Task<ProjectViewModel> DownloadProjectAsync(Change ch);
        Task<CostCategoryViewModel> DownloadCostAsync(Change ch);
        //Task<ExpenditureViewModel> DownloadExpenditureAsync(Change ch);
        Task<ServiceViewModel> DownloadServicesAsync(Change ch);
        //Task<IncomeViewModel> DownloadIncomesAsync(Change ch);
    }

}
