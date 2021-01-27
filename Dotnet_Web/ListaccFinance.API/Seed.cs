using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using ListaccFinance.Api.Data;
using ListaccFinance.API.Data.Model;
using Microsoft.EntityFrameworkCore;
using Newtonsoft.Json;

namespace ListaccFinance.API
{
    public class Seed
    {
        private readonly DataContext _context;
        public Seed(DataContext _context)
        {
            this._context = _context;

        }
        public async Task SeedUsers()
        {
                if(! await _context.Academy_Projects.AnyAsync())
                {
                     var userData = System.IO.File.ReadAllText("Repo/RandomAcadProject.json");
                    var projects = JsonConvert.DeserializeObject<List<Academy_Project>>(userData);
                    foreach(Academy_Project prj in projects)
                    {
                        _context.Add(prj);
                        await _context.SaveChangesAsync();

                    }
                }
        }
    }
}
