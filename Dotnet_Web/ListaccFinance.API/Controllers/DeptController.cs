using System.Collections;
using System.Collections.Generic;
using System.Threading.Tasks;
using AutoMapper;
using ListaccFinance.Api.Data;
using ListaccFinance.API.Data.ViewModel;
using ListaccFinance.API.Interfaces;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

namespace ListaccFinance.API.Controllers
{
    [ApiController]
    [Authorize(Roles = "Admin")]
    [Route("api/[controller]")]
    public class DeptController : ControllerBase
    {
        private readonly DataContext _context;
        private readonly IMapper _mapper;
        private readonly IDeptService _dService;

        public DeptController
                             (  
                                DataContext context,
                                IMapper mapper,
                                IDeptService dService
                             )
        {
            _context = context;
            _mapper = mapper;
            _dService = dService;

        }

        [HttpGet]
        public async Task<IActionResult> ReturnDepartments() => Ok(await _dService.ReturnAllDepts());


        [HttpGet("Search")]
        public async Task<IActionResult> ReturnPaginatedSearchDepartments([FromQuery]SearchDept props)
        {
            if (ModelState.IsValid)
            {
                var deptList = await _dService.ReturnPagedUserList(props);
                var retDept = new List<DeptView> { };
                retDept = _mapper.Map<List<DeptView>>(deptList);

                var metaData =new {
                    deptList.TotalCount,
                    deptList.PageSize,
                    deptList.CurrentPage,
                    deptList.TotalPages,
                    deptList.HasNext,
                    deptList.HasPrevious
                };
                var searchResult = new {
                    retDept, metaData
                };
                return Ok(searchResult);
            }
            return BadRequest("empty string");

        }

        [HttpPost("Create/{name}")]
        public async Task<IActionResult> CreateDepartment(string name)
        {
            if (!string.IsNullOrEmpty(name))
            {
                if (await _dService.IsDeptExist(name))
                {
                    return BadRequest("dept already exists");
                }
                await _dService.CreateDepartment(name);
                return Ok();
            }
            return BadRequest();

        }

        [HttpPut("Edit")]
        public async Task<IActionResult> EditDepartment([FromQuery]int Id,[FromQuery]string newName)
        {
            await _dService.EditDepartment(Id, newName);
            return Ok("done");
        }


    }
}