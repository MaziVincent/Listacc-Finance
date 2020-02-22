
using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using ListaccFinance.API.Data.Model;
using ListaccFinance.API.Data.ViewModel;
using ListaccFinance.API.SendModel;

namespace ListaccFinance.API.Interfaces
{
    public interface ISyncService
    //<T> where T : class

    {

        //Downloads
        Task<DepartMentViewModel> DownloadDeptAsync(Change ch);
        Task<PersonViewModel> DownloadPersonAsync(Change ch);
        Task<UserViewModel> DownloadUserAsync(Change ch);
        Task<ClientViewModel> DownloadClientAsync(Change ch);
        Task<ProjectViewModel> DownloadProjectAsync(Change ch);
        Task<CostCategoryViewModel> DownloadCostAsync(Change ch);
        //Task<ExpenditureViewModel> DownloadExpenditureAsync(Change ch);
        Task<ServiceViewModel> DownloadServicesAsync(Change ch);
        //Task<IncomeViewModel> DownloadIncomesAsync(Change ch);


        //Uploads
        Task UploadDeptAsync(Department d);
        Task UploadPersonAsync(Person p);
        Task UploadUserAsync(RegisterModel u);
        Task UploadClientAsync(Client c);
        Task UploadProjectAsync(Project p);
        Task UploadCostAsync(CostCategory c);
        Task UploadExpenditureAsync(Expenditure e);
        Task UploadServiceAsync(Service s);
        Task UploadIncomeAsync(Income i);
        Task UploadChangesAsync(Change ch);
    }

}
