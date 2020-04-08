using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using AutoMapper;
using ListaccFinance.Api.Data;
using ListaccFinance.API.Data.Model;
using ListaccFinance.API.Data.ViewModel;
using ListaccFinance.API.Interfaces;
using ListaccFinance.API.SendModel;
using Microsoft.EntityFrameworkCore;

namespace ListaccFinance.API.Services

{
    public class DeptService : IDeptService
    {
        private readonly DataContext _context;
        private readonly IMapper _mapper;

        public DeptService(DataContext context, IMapper mapper)
        {
            _context = context;
            _mapper = mapper;
        }

        /*public async Task<List<DeptView>> ReturnAllDeptViews() 
        {
            try
            {
                var x = await _context.Set<Department>().ToListAsync();
                var depts = _mapper.Map<List<DeptView>>(x);
                return depts;
            }
            catch (System.Exception)
            {
                throw;
            }

        }*/

        public async Task<List<Department>> ReturnAllDepts()
        {
            try
            {
                var x = await _context.Set<Department>().ToListAsync();
                return x;
            }
            catch (System.Exception)
            {
                
                throw;
            }
        }


        public async Task CreateDepartment(string name)
        {
            try
            {
                var dept = new Department()
                {
                    Name = name
                };
                await _context.AddAsync<Department>(dept);
                await _context.SaveChangesAsync();
            }
            catch (Exception e)
            {
                
                throw e;
            }

        }
        public async Task<bool> IsDeptExist(string name)
        {
            var thisDept =await  _context.Departments.Where(x => x.Name.ToLower().CompareTo(name.ToLower()) == 0).FirstOrDefaultAsync();
            if (thisDept is null)
            {
                return false;
            }
            else
            {
                return true;
            }
        }

        public async Task EditDepartment(int Id, string newName)
        {
            try
            {
                var thisDept = await _context.Departments.FindAsync(Id);
                thisDept.Name = newName;
                await _context.SaveChangesAsync();
            }
            catch (Exception e)
            {
                
                throw e;
            }

        }

        public async Task<PagedList<Department>> ReturnPagedUserList(SearchDept props)
        {

            // s.key = dept name
            IQueryable<Department> depts = Enumerable.Empty<Department>().AsQueryable();
            if (string.IsNullOrEmpty(props.key) || string.IsNullOrWhiteSpace(props.key))
            {
                depts = _context.Set<Department>();
            }
            else
             {
                depts = _context.Departments.Where(x => x.Name.ToLower().CompareTo(props.key.ToLower()) == 0).OrderBy(x => x.Id);
             }
            
            var pagedDept = PagedList<Department>.ToPagedList(depts, props.PageNumber, props.PageSize);
            return pagedDept;
            
        }
    }
}
