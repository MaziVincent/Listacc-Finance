using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using AutoMapper;
using ListaccFinance.Api.Data;
using ListaccFinance.API.Data.Model;
using ListaccFinance.API.Data.ViewModel;
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
        private readonly IMapper _mapper;

        public UserController(IUserService uservice, DataContext context, ITokenGenerator generator, IMapper mapper )
        {
            _uService =uservice;
            _context = context;
            _generator = generator;
            _mapper = mapper;
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

        
        [Authorize(Roles = "Admin")]
        [HttpPost("DeactivateUser")]
        public async Task<IActionResult> Deactivate (int Id)
        {
            await _uService.Deactivate(Id);
            return Ok();
        }


        [HttpGet("ReturnUsers")]
        public async Task<IActionResult> ReturnUsers ([FromQuery] SearchPaging props)
        {
            List<SearchProps> finalReturn = new List<SearchProps>();
            
            if (!string.IsNullOrEmpty(props.SearchString))
            {
                var userList = await _uService.ReturnUsers(props);
                var returnList = _mapper.Map<List<SearchProps>>(userList);
                
                for (int i =0; i < returnList.Count; i++)
                {
                        returnList.ElementAt(i).Role = userList.ElementAt(i).GetType().Name;

                }

                for (int j = 0; j < props.Role.Length; j++)
                {
                    for (int i = 0; i < returnList.Count; i++)
                    {
                        if (returnList.ElementAt(i).Role.CompareTo(props.Role[j]) == 0)
                        {
                            finalReturn.Add(returnList.ElementAt(i));
                        }
                    }

                }
                return Ok(finalReturn);
            }
            else
            {
                var userList = await  _uService.ReturnAllUsers(props);
                var returnList = _mapper.Map<List<SearchProps>>(userList);

                for (int i = 0; i < returnList.Count; i++)
                {
                        returnList.ElementAt(i).Role = userList.ElementAt(i).GetType().Name.ToString();
    
                }

                for (int j = 0; j < props.Role.Length; j++)
                {
                    //
                    for (int i = 0; i < returnList.Count; i++)
                    {
                        if (returnList.ElementAt(i).Role.CompareTo(props.Role[j]) == 0)
                        {
                            finalReturn.Add(returnList.ElementAt(i));
                        }
                    }
                }
                return Ok(finalReturn);
            }


        }
    }
}