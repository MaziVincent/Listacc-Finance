using System.Threading.Tasks;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using ListaccFinance.Api.Data;
using System.Linq;
using Microsoft.EntityFrameworkCore;
using ListaccFinance.API.Interfaces;
using ListaccFinance.API.ViewModels;
using ListaccFinance.API.Data.Model;

namespace ListaccFinance.API.Controllers
{
    [AllowAnonymous]
    [ApiController]
    [Route("api/[controller]")]
    public class SyncController : ControllerBase 
    {
        private readonly DataContext _context;

        private readonly ITokenGenerator _tokGen;

        private readonly IDesktopService _dService;

        public SyncController(DataContext context, ITokenGenerator tokGen, IDesktopService dService) 
        {
            _context = context;
            _tokGen = tokGen;
            _dService = dService;
        }

        [HttpPost("Login")]
        public async Task<IActionResult> Login(SyncLoginModel mod)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

 
            
            var u = await _context.Users.Where
                                        (x=> x.UserName.ToUpper().CompareTo(mod.UserName.ToUpper()) == 0 && 
                                        x.Password.CompareTo(mod.Password) == 0).FirstOrDefaultAsync();


            var d = await _context.DesktopClients.Where(x => mod.ClientName.ToUpper().CompareTo(x.ClientName.ToUpper()) == 0
                                             && mod.ClientMacAddress.ToUpper().CompareTo(x.ClientMacAddress.ToUpper()) == 0
                                             && mod.ClientType.ToUpper().CompareTo(x.ClientType.ToUpper()) == 0).FirstOrDefaultAsync();


            if (u == null)
            {
                //Redirect("api/controller/register");
                return Unauthorized(new { message = "Your login imput is incorrect" });

            }


            if (d == null)
            {
                var dc = new DesktopCreateModel()
                {
                    ClientMacAddress = mod.ClientMacAddress,
                    ClientName = mod.ClientName,
                    ClientType = mod.ClientType,
                };
                await _dService.CreateDesktopClientAsync(dc);
            }



             var token =  await _tokGen.GenerateToken(d);

             return Ok(token);

        }


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


        // This method pings the server at intervals
        public async Task<IActionResult> PingServer()
        {
            return Ok();
        }

        public async Task<IActionResult> UploadData()
        {
            return Ok();
        }

        public async Task<IActionResult> DownloadData()
        {
            return Ok();
        }

    }
}