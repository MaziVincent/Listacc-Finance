using System.Linq;
using System.Threading.Tasks;
using AutoMapper;
using ListaccFinance.Api.Data;
using ListaccFinance.API.Interfaces;
using ListaccFinance.API.SendModel;
using ListaccFinance.API.Services;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace ListaccFinance.API.Controllers
{
    [AllowAnonymous]
    [ApiController]
    [Route("api/[controller]")]
    public class AuthController : ControllerBase
    {
        private readonly DataContext _context;

        private readonly ITokenGenerator _tokGen;

        private readonly IDesktopService _dService;
        //private readonly ISyncService<> _sservice;

        private readonly ISyncService _sservice;
        private readonly IMapper _mapper;

        public AuthController(DataContext context,
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

        // This method pings the server at intervals
        [HttpGet("PingServer")]
        public IActionResult PingServer()
        {
            return Ok();
        }

        [HttpPost("DesktopLogin")]
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

            var token = await _tokGen.GenerateToken(d, currentUser.Id);


            return Ok(token);

        }

        [HttpPost("UserLogin")]
        public async Task<IActionResult> Login(UserLogin u)
        {

            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            // Password Hash Comparison
            var pmessage = u.Password;
            var currentUser = _context.Users.Where(x => x.Email.ToUpper().CompareTo(u.EmailAddress.ToUpper()) == 0).FirstOrDefault();

            if (currentUser is null)
            {
                return Unauthorized(new { message = "Not authorized" });
            }

            else
            {
                var PasswordHash = Hash.Create(pmessage, currentUser.salt);
                //var isCorrect = Hash.Validate(pmessage, salt, PasswordHash);

                if (currentUser.PasswordHash.CompareTo(PasswordHash) == 0)
                {
                    int myID = currentUser.Id;

                    var tokenString = await _tokGen.GenerateToken(u, myID);
                    currentUser.PasswordHash = null;
                    var token = new
                    {
                        tokenString = tokenString,
                        currentUser = currentUser
                    };
                    return Ok(token);
                }
                return BadRequest("Wrong password");
            }


            /*var thisUser = _context.Users.
                Where(x => x.Email.CompareTo(u.EmailAddress) == 0 &&
                 x.PasswordHash.CompareTo(PasswordHash) == 0).FirstOrDefault();*/


        }

    }
}