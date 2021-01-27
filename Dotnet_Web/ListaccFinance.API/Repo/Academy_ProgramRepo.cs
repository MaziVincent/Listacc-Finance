using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using ListaccFinance.Api.Data;
using ListaccFinance.API.Data.Model;
using ListaccFinance.API.Interfaces;
using Microsoft.EntityFrameworkCore;

namespace ListaccFinance.API.Repo
{
    public class Academy_ProgramRepo : IAcademy_ProgramRepo
    {
        private readonly DataContext _context;
        public Academy_ProgramRepo(DataContext _context)
        {
            this._context = _context;

        }

        public async Task<Academy_Program> GetAcademy_Program(int Id)
        {
            var program = await _context.Academy_Programs
                        .AsQueryable().Where(p => p.Id == Id)
                        .FirstOrDefaultAsync();
            return program;
        }

        public async Task<bool> StudentRegistered(int programId, string studentEmail)
        {
            var result = await _context.Academy_Registraions.AsQueryable()
                        .Where(p => p.Academy_ProgramId == programId 
                                    && p.Academy_Stuent.Email.CompareTo(studentEmail)== 0)
                        .AnyAsync();
            return result;
        }

        public async Task<bool> StudentRegisteredWithPhone(int programId, string studentPhone)
        {
            var result = await _context.Academy_Registraions.AsQueryable()
                        .Where(p => p.Academy_ProgramId == programId 
                                    && p.Academy_Stuent.PhoneNumber.CompareTo(studentPhone)== 0)
                        .AnyAsync();
            return result;
        }

        public void Delete<T>(T entity) where T : class
        {
            _context.Remove(entity);
        }

         public void Add<T>(T entity) where T : class
        {     
            _context.Add(entity);
        }

        public async Task<bool> SaveAll()
        {
            return await _context.SaveChangesAsync() > 0;
        }

        public async Task<ICollection<Academy_Project>> GetAllUpComingProjects()
        {
            var currentDate = DateTime.Now;
            var projects = await _context.Academy_Projects.AsQueryable()
                            .Where(p => p.StartDate >= currentDate)
                            .Include(p => p.Academy_Programs)
                            .ToListAsync();
            return projects;
        }

        public async Task<ICollection<Academy_Project>> GetMostRecentProject()
        {
           //var currentDate = DateTime.Now;
            var projects = await _context.Academy_Projects.AsQueryable()
                            .OrderByDescending(p => p.StartDate)
                            .Include(p => p.Academy_Programs)
                            .ToListAsync();
            return projects;
        }
    }
}