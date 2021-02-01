using AutoMapper;
using ListaccFinance.API.Data.Model;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Identity;
using System.Threading.Tasks;
using ListaccFinance.API.ViewModels;
using ListaccFinance.API.Interfaces;
using Microsoft.Extensions.Configuration;
using Microsoft.AspNetCore.Authorization;
using System.Collections.Generic;

namespace ListaccFinance.API.Controllers.Academy
{
    [ApiController]
     [AllowAnonymous]
    [Route("api/academy/[controller]")]
    public class AuthController : ControllerBase
    {
        private readonly IMapper _imapper;
        private readonly UserManager<Academy_Student> _userManager;
        private readonly IAcademy_ProgramRepo _iProgramRepo;
         private readonly IAcademy_StudentRepo _istudentRepo;
         private readonly ITokenGenerator _tokGen;
         private readonly IConfiguration _config;
        public AuthController(IMapper _imapper,
        UserManager<Academy_Student> _userManager,
        IAcademy_StudentRepo _istudentRepo,
        IAcademy_ProgramRepo _iProgramRepo,
        ITokenGenerator tokGen,
        IConfiguration config
         )
        {
            this._istudentRepo = _istudentRepo;
            this._iProgramRepo = _iProgramRepo;
            this._userManager = _userManager;
            this._imapper = _imapper;
            this._tokGen = tokGen;
            this._config = config;
        }

    [HttpPost("register")]
    public async Task<IActionResult> Register(StudentRegistration stdReg)
    {
       
      var student =  _imapper.Map<Academy_Student>(stdReg);
      var program = await _iProgramRepo.GetAcademy_Program(stdReg.ProgramId);

        if (await _iProgramRepo.StudentRegistered(stdReg.ProgramId, stdReg.Email))
        {
            return BadRequest(new { Error = "This email has already been used to Register for this program!" });
        }

        if (await _iProgramRepo.StudentRegisteredWithPhone(stdReg.ProgramId, stdReg.PhoneNumber))
        {
            return BadRequest(new { Error = "This phone number has already been used to Register for this program!" });
        }

        if(null == program)
        return BadRequest(new { Error = "Invalid program selected!" });
        if(await _iProgramRepo.StudentAleardyExists(student.Email))
        {
             var result = await _userManager.CreateAsync(student);
             if(!result.Succeeded)
             return BadRequest(new { Error = "Sorry, We had a challenge registering you!" });
        }

            Academy_Registraion reg = new Academy_Registraion{
                Academy_Stuent = student,
                Academy_Program = program
            };

            _iProgramRepo.Add(reg);

            if(await _iProgramRepo.SaveAll())
            {
                return NoContent();
            }


        return BadRequest(new { Error = "Sorry Your registration was not successful" });
        // var token  = await _tokGen.GenerateStudentRegistrationToken(stdReg);
        // //if (null != userCreated)
        // {
        //     //var confirmationLink = Url.Action(nameof(ConfirmEmail), "Auth", new { token, email = userCreated.Email }, Request.Scheme);
        //     var confirmationLink =
        //     Url.Content(_config.GetSection("Redirects:email_confirmation").Value + "?email=" + stdReg.Email + "&token=" + token);
        //     await _userService.SendClientRegisterationEmail(userCreated, confirmationLink);
        //     return Ok(new { Id = userCreated.Id }) ;
        // }


    }

    [HttpGet("projects")]
    public async Task<IActionResult> GetUpComingProjects(){

        var projects = await _iProgramRepo.GetAllUpComingProjects();

        if(projects.Count < 1)
        {
            projects = await _iProgramRepo.GetMostRecentProject();
        }
        //returns future projects or returns the past ones in a descending order of their date.

         var objToreturn = _imapper.Map<ICollection<AacdemyProjectDto>>(projects);

         return Ok(projects);
    }

   


}
}