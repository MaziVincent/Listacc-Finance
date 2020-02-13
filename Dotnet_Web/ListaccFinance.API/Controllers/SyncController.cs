using System.Threading.Tasks;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using ListaccFinance.Api.Data;
using System.Linq;
using Microsoft.EntityFrameworkCore;
using ListaccFinance.API.Interfaces;
using ListaccFinance.API.ViewModels;
using ListaccFinance.API.Data.Model;
using System;
using ListaccFinance.API.Services;
using System.Net;
using System.Net.Http;
using System.Collections.Generic;
using Newtonsoft.Json;

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

        public SyncController(  DataContext context, 
                                ITokenGenerator tokGen, 
                                IDesktopService dService, 
                                ISyncService sservice
                             ) 
        {
            _context = context;
            _tokGen = tokGen;
            _dService = dService;
            _sservice = sservice;
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
        public async Task<IActionResult> UploadData()
        {
            return Ok();
        }



        [Authorize]
        [HttpPost("Download")]
        public async Task<HttpResponseMessage> DownloadData(int lastSyncID)
        {
            string MacAddress = this.User.Claims.First(i => i.Type == "macAddr").Value;

            var dc = await _context.DesktopClients.Where((x) => x.ClientMacAddress.CompareTo(MacAddress) == 0).FirstOrDefaultAsync();

            var lastChanges = await _context.Changes
                                .Where(i => i.Id > lastSyncID)
                                .Except(_context.Changes.Where(((x) => x.DesktopClientId == dc.Id))).ToListAsync();

            var dChange = new List<Change> {};
            var pChange = new List<Change> {};
            var uChange = new List<Change> {};
            var cChange = new List<Change> {};
            var prChange = new List<Change> {};
            var ccChange = new List<Change> {};
            var eChange = new List<Change> {};
            var sChange = new List<Change> { };
            var iChange = new List<Change> { };

            foreach (var change in lastChanges)
            {
                switch (change.Table)
                {
                    case "Departments":
                        dChange.Add(change);
                        break;
                    case "Persons":
                        pChange.Add(change);
                        break;
                    case "Users":
                        uChange.Add(change);
                        break;
                    case "Clients":
                        cChange.Add(change);
                        break;

                    case "Projects":
                        prChange.Add(change);
                        break;

                    case "Cost Categories":
                        ccChange.Add(change);
                        break;

                    case "Expenditures":
                        eChange.Add(change);
                        break;

                    case "Services":
                        sChange.Add(change);
                        break;
                    case "Incomes":
                        iChange.Add(change);
                        break;


                    //default: 
                }
            }

            var deptDown = await  _sservice.DownloadDeptAsync(dChange);
            var perDown = await _sservice.DownloadPersonAsync(pChange);
            var usDown = await  _sservice.DownloadUserAsync(uChange);
            var cliDown = await  _sservice.DownloadClientAsync(cChange);
            var proDown = await _sservice.DownloadProjectAsync(prChange);
            var cocDown = await _sservice.DownloadCostAsync(ccChange);
            var expDown = await  _sservice.DownloadExpenditureAsync(eChange);
            var serDown = await  _sservice.DownloadServicesAsync(sChange);
            var incDown = await _sservice.DownloadIncomesAsync(iChange);


            var Response = new HttpResponseMessage(HttpStatusCode.OK);
            var respList = new DownloadContent()
            {
                deptList = deptDown,
                pertList = perDown,
                userList = usDown,
                cList = cliDown,
                proList = proDown,
                costList = cocDown,
                expList = expDown,
                serList = serDown,
                incList = incDown,
                lastId = lastChanges.Count > 0 ? lastChanges.Last().Id : 0,
            };
            Response.Content = respList;

            return Response;

    }
    }
}