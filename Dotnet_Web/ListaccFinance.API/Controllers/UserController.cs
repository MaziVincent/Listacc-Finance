using System.Linq;
using System.Threading.Tasks;
using ListaccFinance.Api.Data;
using ListaccFinance.API.Data.Model;
using ListaccFinance.API.Interfaces;
using ListaccFinance.API.ViewModels;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

namespace ListaccFinance.API.Controllers
{
    [AllowAnonymous]
    [ApiController]
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


        [HttpPost("login")]
        public async Task<IActionResult> Login (UserLogin u)
        {

            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }


            var thisUser = _context.Users.
                Where(x => x.UserName.CompareTo(u.UserName) == 0 &&
                 x.Password.CompareTo(u.Password) == 0).FirstOrDefault();

            int myID = thisUser.Id;

            if (thisUser is null)
            {
                return Unauthorized(new {message = "Not authorized"});
            }



            var message = await _generator.GenerateToken(u, myID);
            return Ok(message);
        }



        [HttpPost("firstcreateuser")]
        public async Task<IActionResult> FirstCreateUser(RegisterModel me)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            var aUser = new UserLogin
            {
                Password = me.Password,
                UserName = me.UserName,
            };
            if (!_uService.IsUserExist(aUser))
            {

                var resp = await _uService.CreateUserAsync(me);
                return Ok("successful");
            }


            return BadRequest(new {message = "Could not create"});
            //Redirect("http://localhost:5000/api/user/createuser");

        }



        [Authorize]
        [HttpPost("createUser")]
        public async Task<IActionResult> CreateUser(RegisterModel me) 
        {

            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }
            
            string userIdString = this.User.Claims.First(i => i.Type == "UserID").Value;
            int userId = int.Parse(userIdString);
            var u = new User();
            var resp = await _uService.CreateUserAsync(me, userId);
            return Ok("successful");
        }

        


    }
}