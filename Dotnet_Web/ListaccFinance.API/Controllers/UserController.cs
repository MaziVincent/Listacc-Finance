using System.Threading.Tasks;
using ListaccFinance.API.Data.Model;
using ListaccFinance.API.Interfaces;
using ListaccFinance.API.Services;
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

        public UserController(IUserService uservice)
        {
            _uService =uservice;
        }
        
        [HttpPost("createUser")]
        public async Task<IActionResult> CreateUser(RegisterModel me) 
        {
            var u = new User();

            var resp = await _uService.CreateUserAsync(me);
            return Ok("successful");
        }


    }
}