using System.Threading.Tasks;
using AutoMapper;
using ListaccFinance.Api.Data;
using ListaccFinance.API.Interfaces;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

namespace ListaccFinance.API.Controllers
{
    [ApiController]
    [Authorize]
    [Route("api/[controller]")]
    public class DeptController : ControllerBase
    {
        private readonly DataContext _context;
        private readonly IMapper _mapper;
        private readonly IOtherServices _oService;

        public DeptController(DataContext context,
                                IMapper mapper,
                                IOtherServices oService
                             )
        {
            _context = context;
            _mapper = mapper;
            _oService = oService;

        }

        [HttpGet("Departments")]
        public async Task<IActionResult> ReturnDepartments() => Ok(_oService.ReturnDepts());
    }
}