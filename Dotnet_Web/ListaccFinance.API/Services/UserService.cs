using System;
using System.Linq;
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


        //First User Creation
        public async Task<string> CreateUserAsync(RegisterModel reg)
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

            newUser.UserName = reg.UserName;
            newUser.Address = reg.Address;
            newUser.Phone = reg.Phone;
            newUser.Password = reg.Password;

            newUser.Person = per;
            newUser.Department = dept;


            var thisUser = _context.Users.
                            Where(x => x.UserName.CompareTo(reg.UserName) == 0 &&
                             x.Password.CompareTo(reg.Password) == 0).FirstOrDefault();
            await _context.Users.AddAsync(newUser);
            await _context.SaveChangesAsync();

            var change = new Change()
            {
                Table = "Users",
                ChangeType = "Create",
                OnlineTimeStamp = DateTime.Now,
                OfflineTimeStamp = DateTime.Now,
            };
            await _context.Changes.AddAsync(change); ;
            await _context.SaveChangesAsync();


            return "done";


        }

        //Subsequent User Creation
        public  async Task<string> CreateUserAsync( RegisterModel reg, int userId)
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

            newUser.UserName = reg.UserName;
            newUser.Address = reg.Address;
            newUser.Phone = reg.Phone;
            newUser.Password = reg.Password;

            newUser.Person = per;
            newUser.Department = dept;

            await _context.Users.AddAsync(newUser);
            await _context.SaveChangesAsync();

            var thisUser = _context.Users.
                            Where(x => x.UserName.CompareTo(reg.UserName) == 0 &&
                             x.Password.CompareTo(reg.Password) == 0).FirstOrDefault();

            int thisUserID = thisUser.Id;

            var change =  new Change()
            {
                Table = "Users",
                EntryId = thisUserID,
                ChangeType = "Create",
                OnlineTimeStamp = DateTime.Now,
                OfflineTimeStamp = DateTime.Now,
                UserId = userId,
            };
            await _context.Changes.AddAsync(change);;
            await _context.SaveChangesAsync();


             return "done";
   

        }


        public bool IsUserExist (UserLogin u)
        {
            var i = _context.Users.Where(x => x.UserName.CompareTo(u.UserName) == 0 && x.Password.CompareTo(u.Password) == 0).FirstOrDefault();
            if (i is null)
            {
                return false;
            }

            return true;
        }
    }

}