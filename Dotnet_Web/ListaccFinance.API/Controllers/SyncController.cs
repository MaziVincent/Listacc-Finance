using System.Threading.Tasks;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using ListaccFinance.Api.Data;
using System.Linq;
using Microsoft.EntityFrameworkCore;
using ListaccFinance.API.Interfaces;
using ListaccFinance.API.Data.Model;
using System;
using ListaccFinance.API.Services;
using System.Net;
using System.Net.Http;
using System.Collections.Generic;
using ListaccFinance.API.SendModel;
using ListaccFinance.API.Data.ViewModel;
using AutoMapper;

namespace ListaccFinance.API.Controllers
{

    [ApiController]
    [Authorize]
    [Route("api/[controller]")]
    public class SyncController : ControllerBase
    {
        private readonly DataContext _context;

        private readonly ITokenGenerator _tokGen;

        private readonly IDesktopService _dService;
        //private readonly ISyncService<> _sservice;

        private readonly ISyncService _sservice;
        private readonly IMapper _mapper;

        public SyncController(DataContext context,
                                ITokenGenerator tokGen,
                                IDesktopService dService,
                                ISyncService sservice,
                                IMapper mapper
                             )
        {
            _context = context;
            _tokGen = tokGen;
            _dService = dService;
            _sservice = sservice;
            _mapper = mapper;

        }

        private int deptId;


        [Authorize]
        [HttpPost("CreateDesktopClient")]
        public async Task<IActionResult> CreateDesktop(DesktopCreateModel m)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }


            var response = await _dService.CreateDesktopClientAsync(m);
            return Ok(response);
        }





        public async Task SaveChangesAsync(DateTime ChangeTimestamp, string Change, string Table, int EntryId)
        {
            int DesktopClientId = int.Parse(this.User.Claims.First(x => x.Type == "Desktopid").Value);
            int UserId = int.Parse(this.User.Claims.First(x => x.Type == "userId").Value);
            DateTime OnlineTimeStamp = DateTime.Now;
            var newChange = new Change
            {
                DesktopClientId = DesktopClientId,
                UserId = UserId,
                OnlineTimeStamp = OnlineTimeStamp,
                OfflineTimeStamp = ChangeTimestamp,
                ChangeType = Change,
                Table = Table,
                EntryId = EntryId
            };
            await _context.Changes.AddAsync(newChange);
            await _context.SaveChangesAsync();

        }

        List<SavedList> mapList = new List<SavedList>();
        SavedList save;
        [Authorize]
        [HttpPost("Upload")]
        public async Task<IActionResult> UploadData(List<UploadSyncViewModel> lsync)
        {

            try
            {

                foreach (UploadSyncViewModel sync in lsync)
                {
                    switch (sync.Table)
                    {

                        case "Departments":
                            var dChange = _mapper.Map<Department>(sync.dept);
                            int dId = sync.dept.Id;

                            save = sync.dept.OnlineEntryId is null ? await _sservice.UploadDeptAsync(dChange, dId) : await _sservice.UploadOldDeptAsync(dChange, sync.dept.OnlineEntryId.Value);
                            if (save != null) mapList.Add(save);
                            await SaveChangesAsync(sync.dept.ChangeTimeStamp, sync.dept.Change, sync.Table, dId);
                            break;


                        case "Persons":
                            var pChange = _mapper.Map<Person>(sync.person);
                            int pId = sync.person.Id;
                            save = sync.person.OnlineEntryId is null ? await _sservice.UploadPersonAsync(pChange, pId) : await _sservice.UploadOldPersonAsync(pChange, sync.person.OnlineEntryId.Value);
                            if (save != null) mapList.Add(save);
                            await SaveChangesAsync(sync.person.ChangeTimeStamp, sync.person.Change, sync.Table, pId);
                            break;

                        case "Users":

                            if (sync.user.DepartmentOnlineEntryId is null)
                            {
                                foreach (var mapped in mapList)
                                {
                                    if (mapped.Id == sync.user.DepartmentId && mapped.Table == "Departments")
                                    {
                                        deptId = mapped.OnlineEntryId;
                                        break;
                                    }
                                }
                            }
                            else
                            {
                                deptId = sync.user.DepartmentId;
                            }
                            var regUser = new RegisterModel
                            {
                                firstName = sync.user.person.firstName,
                                LastName = sync.user.person.LastName,
                                Gender = sync.user.person.Gender,
                                DateOfBirth = sync.user.person.DateOfBirth,
                                Phone = sync.user.Phone,
                                Address = sync.user.Address,
                                EmailAddress = sync.user.Email,
                                Password = sync.user.Password,
                                DepartmentId = deptId
                            };
                            int uId = sync.user.Id;
                            await _sservice.UploadUserAsync(regUser, sync.user.Id);
                            await SaveChangesAsync(sync.user.ChangeTimeStamp, sync.user.Change, sync.Table, uId);
                            break;


                        case "Clients":
                            var cChange = _mapper.Map<Client>(sync.client);
                            int cId = sync.client.Id;
                            if (sync.client.PersonOnlineEntryId is null)
                            {
                                foreach (var mapped in mapList)
                                {
                                    if (mapped.Id == sync.client.PersonId && mapped.Table.CompareTo("Clients") == 0)
                                    {
                                        cChange.PersonId = mapped.OnlineEntryId;
                                        break;
                                    }
                                }
                            }
                            save = sync.client.OnlineEntryId is null ? await _sservice.UploadClientAsync(cChange, cId) : await _sservice.UploadOldClientAsync(cChange, sync.client.OnlineEntryId.Value);
                            if (save != null) mapList.Add(save);
                            await SaveChangesAsync(sync.client.ChangeTimeStamp, sync.client.Change, sync.Table, cId);
                            break;


                        case "Projects":
                            var prChange = _mapper.Map<Project>(sync.project);
                            int prId = sync.project.Id;
                            if (sync.project.DepartmentOnlineEntryId is null)
                            {
                                foreach (var mapped in mapList)
                                {
                                    if (mapped.Id == sync.project.DepartmentId && mapped.Table == "Departments")
                                    {
                                        prChange.DepartmentId = mapped.OnlineEntryId;
                                        break;
                                    }
                                }
                            }

                            save = sync.project.OnlineEntryId is null ? await _sservice.UploadProjectAsync(prChange, prId) : await _sservice.UploadOldProjectAsync(prChange, sync.project.OnlineEntryId.Value);
                            if (save != null) mapList.Add(save);
                            await SaveChangesAsync(sync.project.ChangeTimeStamp, sync.project.Change, sync.Table, prId);
                            break;

                        case "CostCategories":
                            var ccChange = _mapper.Map<CostCategory>(sync.costCategory);
                            int ccId = sync.costCategory.Id;
                            save = sync.costCategory.OnlineEntryId is null ? await _sservice.UploadCostAsync(ccChange, ccId) : await _sservice.UploadOldCostAsync(ccChange, sync.costCategory.OnlineEntryId.Value);
                            if (save != null) mapList.Add(save);
                            await SaveChangesAsync(sync.costCategory.ChangeTimeStamp, sync.costCategory.Change, sync.Table, ccId);
                            break;

                        case "Expenditures":
                            var eChange = _mapper.Map<Expenditure>(sync.expenditure);
                            int eId = sync.expenditure.Id;
                            if (sync.expenditure.ClientOnlineEntryId is null)
                            {
                                foreach (var mapped in mapList)
                                {
                                    if (mapped.Id == sync.expenditure.ClientId && mapped.Table.CompareTo("Clients") == 0)
                                    {
                                        eChange.ClientId = mapped.OnlineEntryId;
                                        break;
                                    }
                                }
                            }
                            if (sync.expenditure.CostCategoryOnlineEntryId is null)
                            {
                                foreach (var mapped in mapList)
                                {
                                    if (mapped.Id == sync.expenditure.CostCategoryId && mapped.Table.CompareTo("CostCategories") == 0)
                                    {
                                        eChange.CostCategoryId = mapped.OnlineEntryId;
                                        break;
                                    }
                                }
                            }
                            if (sync.expenditure.ProjectOnlineEntryId is null)
                            {
                                foreach (var mapped in mapList)
                                {
                                    if (mapped.Id == sync.expenditure.ProjectId && mapped.Table.CompareTo("Projects") == 0)
                                    {
                                        eChange.ProjectId = mapped.OnlineEntryId;
                                        break;
                                    }
                                }
                            }
                            if (sync.expenditure.IssuerOnlineEntryId is null)
                            {
                                foreach (var mapped in mapList)
                                {
                                    if (mapped.Id == sync.expenditure.IssuerId && mapped.Table.CompareTo("Users") == 0)
                                    {
                                        eChange.IssuerId = mapped.OnlineEntryId;
                                        break;
                                    }
                                }
                            }
                            save = sync.expenditure.OnlineEntryId is null ? await _sservice.UploadExpenditureAsync(eChange, eId) : await _sservice.UploadOldExpenditureAsync(eChange, sync.expenditure.OnlineEntryId.Value);
                            if (save != null) mapList.Add(save);
                            await SaveChangesAsync(sync.expenditure.ChangeTimeStamp, sync.expenditure.Change, sync.Table, eId);
                            break;


                        case "Services":
                            var sChange = _mapper.Map<Service>(sync.service);
                            int sId = sync.service.Id;
                            if (sync.service.ProjectOnlineEntryId is null)
                            {
                                foreach (var mapped in mapList)
                                {
                                    if (mapped.Id == sync.service.ProjectId && mapped.Table.CompareTo("Projects") == 0)
                                    {
                                        sChange.ProjectId = mapped.OnlineEntryId;
                                        break;
                                    }
                                }
                            }
                            sChange.FixedAmount = sync.service.FixedAmount == 1? true: false;
                            save = sync.service.OnlineEntryId is null ? await _sservice.UploadServiceAsync(sChange, sId) : await _sservice.UploadOldServiceAsync(sChange, sync.service.OnlineEntryId.Value);
                            if (save != null) mapList.Add(save);
                            await SaveChangesAsync(sync.service.ChangeTimeStamp, sync.service.Change, sync.Table, sId);
                            break;

                        case "Incomes":
                            var iChange = _mapper.Map<Income>(sync.income);
                            if (sync.income.ClientOnlineEntryId == null)
                            {
                                foreach (var saved in mapList)
                                {
                                    if (saved.Id == sync.income.ServiceId && saved.Table.CompareTo("Clients") == 0)
                                    {
                                        iChange.ClientId = saved.OnlineEntryId;
                                        break;
                                    }

                                }
                            }
                            if (sync.income.ServiceOnlineEntryId == null)
                            {
                                foreach (var saved in mapList)
                                {
                                    if (saved.Id == sync.income.ServiceId && saved.Table.CompareTo("Services") == 0)
                                    {
                                        iChange.ServiceId = saved.OnlineEntryId;
                                        break;
                                    }
                                }
                            }

                            if (sync.income.IncomeOnlineEntryId == null)
                            {
                                foreach (var saved in mapList)
                                {
                                    if (saved.Id == sync.income.IncomeId && saved.Table.CompareTo("Incomes") == 0)
                                    {
                                        iChange.IncomeId = saved.OnlineEntryId;
                                        break;
                                    }
                                }
                            }

                            if (sync.income.UserOnlineEntryId == null)
                            {
                                foreach (var saved in mapList)
                                {
                                    if (saved.Id == sync.income.UserId && saved.Table.CompareTo("Users") == 0)
                                    {
                                        iChange.UserId = saved.OnlineEntryId;
                                        break;
                                    }
                                }
                            }


                            int iId = sync.income.Id;
                            save = sync.income.OnlineEntryId is null ? await _sservice.UploadIncomeAsync(iChange, iId) : await _sservice.UploadOldIncomeAsync(iChange, sync.income.OnlineEntryId.Value);
                            if (save != null) mapList.Add(save);
                            await SaveChangesAsync(sync.income.ChangeTimeStamp, sync.income.Change, sync.Table, iId);
                            break;

                            /*case "Changes" :
                                int DesktopClientId = int.Parse(this.User.Claims.First(x =>x.Type == "Desktopid").Value);

                                await _context.Changes.AddAsync(sync.change);
                                break;*/
                    }
                }
                return Ok();
            }
            catch (System.Exception e)
            {
                throw e;
            }

        }



        [Authorize]
        [HttpGet("Download/{lastSyncID}")]
        public async Task<IActionResult> DownloadData([FromRoute]int lastSyncID)
        {
            const int numnerOfItems = 10;
            string MacAddress = this.User.Claims.First(i => i.Type == "macAddr").Value;

            var dc = await _context.DesktopClients.Where((x) => x.ClientMacAddress.CompareTo(MacAddress) == 0).FirstOrDefaultAsync();

            var lastChanges = await _context.Changes
                                .Where(i => i.Id > lastSyncID)
                                .Except(_context.Changes.Where(((x) => x.DesktopClientId == dc.Id)))
                                .OrderBy(x => x.Id).Take(numnerOfItems).ToListAsync();

            List<SyncViewModel> syncValues = new List<SyncViewModel>();

            foreach (var ch in lastChanges)
            {
                SyncViewModel obj = new SyncViewModel();
                switch (ch.Table)
                {
                    case "Departments":
                        obj.dept = await _sservice.DownloadDeptAsync(ch);
                        break;

                    case "Persons":
                        obj.person = await _sservice.DownloadPersonAsync(ch);
                        break;

                    case "Users":
                        obj.user = await _sservice.DownloadUserAsync(ch);
                        break;
                    case "Clients":
                        obj.client = await _sservice.DownloadClientAsync(ch);
                        break;
                    case "Projects":
                        obj.project = await _sservice.DownloadProjectAsync(ch);
                        break;
                    case "CostCategories":
                        obj.costCategory = await _sservice.DownloadCostAsync(ch);
                        break;
                    case "Services":
                        obj.service = await _sservice.DownloadServicesAsync(ch);
                        break;
                }
                obj.Table = ch.Table;
                syncValues.Add(obj);
            }

            return Ok(syncValues);
        }
    }
}