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
    [Route("api/[controller]")]
    public class SyncController : ControllerBase 
    {
        private readonly DataContext _context;

        private readonly ITokenGenerator _tokGen;

        private readonly IDesktopService _dService;
        //private readonly ISyncService<> _sservice;

        private readonly ISyncService _sservice;
        private readonly IMapper _mapper;

        public SyncController(  DataContext context, 
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

        [AllowAnonymous]
        [HttpPost("Login")]
        public async Task<IActionResult> Login(SyncLoginModel mod)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }


            // Password Hash
            var currentUser = _context.Users.Where(x => x.Email.ToUpper().CompareTo(mod.EmailAddress.ToUpper()) == 0).FirstOrDefault();
            if (currentUser == null || !Hash.Validate(mod.Password, currentUser.salt, currentUser.PasswordHash))
            {
                return Unauthorized(new { message = "Your login input is incorrect" });
            }
            
            var d = await _context.DesktopClients.Where(x => mod.ClientName.ToUpper().CompareTo(x.ClientName.ToUpper()) == 0
                                             && mod.ClientMacAddress.ToUpper().CompareTo(x.ClientMacAddress.ToUpper()) == 0
                                             && mod.ClientType.ToUpper().CompareTo(x.ClientType.ToUpper()) == 0).FirstOrDefaultAsync();


            if (d == null)
            {
                var dc = new DesktopCreateModel()
                {
                    ClientMacAddress = mod.ClientMacAddress,
                    ClientName = mod.ClientName,
                    ClientType = mod.ClientType,
                };
                d = await _dService.CreateDesktopClientAsync(dc);
            }

             var token =  await _tokGen.GenerateToken(d);


             return Ok(token);

        }

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



        [AllowAnonymous]
        // This method pings the server at intervals
        [HttpGet("PingServer")]
        public IActionResult PingServer()
        {
            return Ok();
        }

        [Authorize]
        [HttpPost("Upload")]
        public async Task<IActionResult> UploadData(List<SyncViewModel> lsync)
        {
            try
            {
                foreach (SyncViewModel sync in lsync)
                {
                    switch (sync.Table)
                    {
                        case "Departments":
                            var dChange = _mapper.Map<Department>(sync.dept);
                            await _sservice.UploadDeptAsync(dChange);
                            break;

                        case "Persons":
                            var pChange = _mapper.Map<Person>(sync.person);
                            await _sservice.UploadPersonAsync(pChange);
                            break;

                        case "Users":
                            //var uChange = _mapper.Map<RegisterModel>(sync.user);
                            var regUser = new RegisterModel{
                                firstName = sync.user.person.firstName,
                                LastName = sync.user.person.LastName,
                                Gender = sync.user.person.Gender,
                                DateOfBirth = sync.user.person.DateOfBirth,
                                Phone = sync.user.Phone, 
                                Address = sync.user.Address,
                                EmailAddress = sync.user.Email,
                                Password = sync.user.Password,
                                DepartmentId = sync.user.DepartmentId,
                                
                            };

                            
                            await _sservice.UploadUserAsync(regUser);
                            break;

                        case "Clients":
                            var cChange = _mapper.Map<Client>(sync.client);
                            await _sservice.UploadClientAsync(cChange);
                            break;

                        case "Projects":
                            var prChange = _mapper.Map<Project>(sync.project);
                            await _sservice.UploadProjectAsync(prChange);
                            break;

                        case "CostCategories":
                            var ccChange = _mapper.Map<CostCategory>(sync.costCategory);
                            await _sservice.UploadCostAsync(ccChange);
                            break;
                        case "Services":
                            var sChange = _mapper.Map<Service>(sync.service);
                            await _sservice.UploadServiceAsync(sChange);
                            break;
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
                                .OrderBy(x =>x.Id).Take(numnerOfItems).ToListAsync();

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