using System.Linq;
using System.Threading.Tasks;
using ListaccFinance.Api.Data;
using ListaccFinance.API.Data.Model;
using ListaccFinance.API.Interfaces;
using ListaccFinance.API.SendModel;
using ListaccFinance.API.Services;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

namespace ListaccFinance.API.Controllers
{
    [ApiController]
    [Authorize]
    [Route("api/[controller]")]
    public class UserController: ControllerBase
    {

        private readonly IUserService _uService;
        private readonly DataContext _context;
        private readonly ITokenGenerator _generator;

        public UserController(IUserService uservice, DataContext context, ITokenGenerator generator)
        {
            _uService =uservice;
            _context = context;
            _generator = generator;
        }





        [Authorize(Roles = "Admin")]
        [HttpPost("FirstCreateUser")]
        public async Task<IActionResult> FirstCreateUser(RegisterModel me)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (!_uService.IsUserExist())
            {
                var resp = await _uService.CreateUserAsync(me);
                return Ok("successful");
            }


            return BadRequest(new {message = "Could not create"});
            //Redirect("http://localhost:5000/api/user/createuser");

        }

        [Authorize(Roles = "Admin")]
        [HttpPost("CreateAdmin")]
        public async Task<IActionResult> CreateAdmin(RegisterModel reg)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (!await _uService.IsThisUserExist(reg.EmailAddress))
            {
                string userIdString = this.User.Claims.First(i => i.Type == "UserID").Value;
                int userId = int.Parse(userIdString);
                var u = new Admin();
                await _uService.CreateAdmin(reg, userId);
                
                return Ok("successful");
            }

            return BadRequest(new { message = " User already exists" });
        }


        [Authorize(Roles = "Admin")]
        [HttpPost("CreateMember")]

        public async Task<IActionResult> CreateMember(RegisterModel me) 
        {

            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (!await _uService.IsThisUserExist(me.EmailAddress))
            {
                string userIdString = this.User.Claims.First(i => i.Type == "UserID").Value;
                int userId = int.Parse(userIdString);
                var u = new User();
                var resp = await _uService.CreateUserAsync(me, userId);
                return Ok("successful");
            }

            return BadRequest(new {message = " User already exists"});

            //return RedirectToAction(string actionName, string controllerName, object routeValues);

        }

        


    }
}