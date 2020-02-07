using System.Threading.Tasks;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using ListaccFinance.Api.Data;
using System.Linq;
using Microsoft.EntityFrameworkCore;
using ListaccFinance.API.Interfaces;
using ListaccFinance.API.ViewModels;

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

            var i = await _context.DesktopClients.Where(x => mod.ClientName.CompareTo(x.ClientName) == 0 
                                                        && mod.ClientMacAddress.CompareTo(x.ClientMacAddress) == 0 
                                                        && mod.ClientType.CompareTo(x.ClientType) == 0).FirstOrDefaultAsync();

            if (i == null)
            {
                //Redirect("api/controller/register");
                return BadRequest(new {message = "Your username and password is incorrect"});
                
            }

             var token =  await _tokGen.GenerateToken(i);

             return Ok(token);

        }

        [HttpPost("CreateDesktopClient")]
        public async Task<IActionResult> CreateDesktop(DesktopCreateModel m)
        {
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