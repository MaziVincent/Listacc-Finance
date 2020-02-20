using System;
using System.Linq;
using System.Threading.Tasks;
using ListaccFinance.Api.Data;
using ListaccFinance.API.Data.Model;
using ListaccFinance.API.Interfaces;
using ListaccFinance.API.SendModel;
using Microsoft.EntityFrameworkCore;

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

            newUser.Email = reg.EmailAddress;
            newUser.Address = reg.Address;
            newUser.Phone = reg.Phone;

            // Password Hash
            var message = reg.Password;
            var salt = Salt.Create();
            var hash = Hash.Create(message, salt);
            newUser.PasswordHash = hash;
            newUser.salt = salt;


            newUser.Person = per;
            newUser.Department = dept;


            await _context.Users.AddAsync(newUser);
            await _context.SaveChangesAsync();

            var thisUser = _context.Users.
                Where(x => x.Email.CompareTo(reg.EmailAddress) == 0 &&
                 x.PasswordHash.CompareTo(hash) == 0).FirstOrDefault();

            int thisUserID = thisUser.Id;

            var change = new Change()
            {
                Table = "Users",
                ChangeType = "Create",
                EntryId = thisUserID,
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

            newUser.Email = reg.EmailAddress;
            newUser.Address = reg.Address;
            newUser.Phone = reg.Phone;


            // Password Hash
            var message = reg.Password;
            var salt = Salt.Create();
            var hash = Hash.Create(message, salt);
            newUser.PasswordHash = hash;
            newUser.salt = salt;


            newUser.Person = per;
            newUser.Department = dept;

            await _context.Users.AddAsync(newUser);
            await _context.SaveChangesAsync();

            var thisUser = _context.Users.
                            Where(x => x.Email.CompareTo(reg.EmailAddress) == 0 &&
                             x.PasswordHash.CompareTo(hash) == 0).FirstOrDefault();

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



        //Create User for uploads
        public async Task<string> CreateUserUploadAsync(RegisterModel reg)
        {
            var newUser = new User();


            var per = new Person()
            {
                firstName = reg.firstName,
                LastName = reg.LastName,
                Gender = reg.Gender,
                DateOfBirth = reg.DateOfBirth,
            };

            newUser.Email = reg.EmailAddress;
            newUser.Address = reg.Address;
            newUser.Phone = reg.Phone;
            newUser.DepartmentId = reg.DepartmentId.Value;

            // Password Hash
            var message = reg.Password;
            var salt = Salt.Create();
            var hash = Hash.Create(message, salt);
            newUser.PasswordHash = hash;
            newUser.salt = salt;


            newUser.Person = per;

            await _context.Users.AddAsync(newUser);
            await _context.SaveChangesAsync();

            return "done";


        }

        public bool IsUserExist ()
        {
            var nr = _context.Users.Count();
            if (nr == 0)
            {
                return false;
            }

            return true;

            /*
            public bool IsUserExist (UserLogin u)
            {
            var i = _context.Users.Where(x => x.UserName.CompareTo(u.UserName) == 0 && x.Password.CompareTo(u.Password) == 0).FirstOrDefault();
            if (i is null)
            {
                return false;
            }
            }

            return true; */
        }
    
        public async Task<bool> IsThisUserExist(string UserEmail)
        {
            var thisU = await _context.Users.Where(x => x.Email.CompareTo(UserEmail) == 0).FirstOrDefaultAsync();

            if (thisU is null)
            {
                return false;
            }
            
            return true;
        }
    }

}