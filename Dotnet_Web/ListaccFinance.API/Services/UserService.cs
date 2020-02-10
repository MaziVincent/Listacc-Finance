using System;
using System.Threading.Tasks;
using ListaccFinance.Api.Data;
using ListaccFinance.API.Data.Model;
using ListaccFinance.API.Interfaces;
using ListaccFinance.API.ViewModels;

namespace ListaccFinance.API.Services

{
    public class UserService: IUserService
    {
        private readonly  DataContext _context;

        public UserService(DataContext context) 
        {
            _context = context;
        }
        public  async Task<string> CreateUserAsync( RegisterModel reg)
        {
            var newUser = new User();
            var dept = new Department()
            {
                Name = reg.Department
            };

            var per = new Person()
            {
                firstName = reg.firstName,
                LastName = reg.LastName,
                Gender = reg.Gender,
                DateOfBirth = reg.DateOfBirth,
            };

            newUser.Address = reg.Address;
            newUser.Phone = reg.Phone;
            newUser.Password = reg.Password;

            newUser.Person = per;
            newUser.Department = dept;


               
                await _context.Users.AddAsync(newUser);
                await _context.SaveChangesAsync();

                return "done";
   

        }
    }

}