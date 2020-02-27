using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using ListaccFinance.Api.Data;
using ListaccFinance.API.Data.Model;
using ListaccFinance.API.Data.ViewModel;
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
            var newUser = new Member();

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
            newUser.Status = true;

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
            newUser.SearchString =(newUser.Id + " " + newUser.Person.LastName + " " + newUser.Person.firstName + " " + newUser.Person.Gender + " " + newUser.Email + " " + newUser.Phone + " Member").ToUpper();
            await _context.SaveChangesAsync();
            var thisUser = _context.Members.
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

        //Subsequent User(member) Creation
        public  async Task<string> CreateUserAsync( RegisterModel reg, int userId)
        {
            
            var newUser = new Member();
            
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
            newUser.Status = true;


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
            newUser.SearchString = (newUser.Id + " " + newUser.Person.LastName + " " + newUser.Person.firstName + " " + newUser.Person.Gender + " " + newUser.Email + " " + newUser.Phone + " " + newUser.Status + " Member").ToUpper();
            await _context.SaveChangesAsync();

            var thisUser = _context.Members.
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
        public async Task<User> CreateUserUploadAsync(RegisterModel reg)
        {
            var newUser = new Member();


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
            newUser.Status = true;

            // Password Hash
            var message = reg.Password;
            var salt = Salt.Create();
            var hash = Hash.Create(message, salt);
            newUser.PasswordHash = hash;
            newUser.salt = salt;


            newUser.Person = per;

            await _context.Members.AddAsync(newUser);
            await _context.SaveChangesAsync();
            newUser.SearchString = (newUser.Id + " " + newUser.Person.LastName + " " + newUser.Person.firstName + " " + newUser.Person.Gender + " " + newUser.Email + " " + newUser.Phone + " " + newUser.Status + " Member").ToUpper();
            await _context.SaveChangesAsync();

            return newUser;


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

        public async Task CreateAdmin(RegisterModel reg, int userId)
        {
            var newUser = new Admin();

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
            newUser.Status = true;


            newUser.Person = per;
            newUser.Department = dept;

            await _context.Admins.AddAsync(newUser);
            await _context.SaveChangesAsync();
            newUser.SearchString = (newUser.Id + " " + newUser.Person.LastName + " " + newUser.Person.firstName + " " + newUser.Person.Gender + " " + newUser.Email + " " + newUser.Phone + " " + newUser.Status + " Admin").ToUpper();
            await _context.SaveChangesAsync();

            var thisUser = _context.Admins.
                            Where(x => x.Email.CompareTo(reg.EmailAddress) == 0 &&
                             x.PasswordHash.CompareTo(hash) == 0).FirstOrDefault();

            int thisUserID = thisUser.Id;

            var change = new Change()
            {
                Table = "Users",
                EntryId = thisUserID,
                ChangeType = "Create",
                OnlineTimeStamp = DateTime.Now,
                OfflineTimeStamp = DateTime.Now,
                UserId = userId,
            };
            await _context.Changes.AddAsync(change); ;
            await _context.SaveChangesAsync();

        }

        public async Task Deactivate (int Id)
        {
            User u = await _context.Users.FindAsync(Id);
            u.Status = false;
            await _context.SaveChangesAsync();
             
        }
   

        // Search when Search String is not null
        public async Task<IEnumerable<User>> ReturnUsers(SearchPaging props)
        {
            return await  SearchUser(props).OrderBy(x => x.Id)
                                        .Skip((props.PageNumber-1) * props.PageSize)
                                        .Take(props.PageSize)
                                        .ToListAsync();
        }

        private IQueryable<User> SearchUser(SearchPaging props)
        {
            var u = _context.Users.Where(x => x.SearchString.Contains(props.SearchString.ToUpper()));
            return u;
        }
        
        // Search when search string is null
        public async Task<IEnumerable<User>> ReturnAllUsers(SearchPaging props)
        {
            return await _context.Users.OrderBy(x => x.Id)
                                        .Skip((props.PageNumber - 1) * props.PageSize)
                                        .Take(props.PageSize)
                                        .ToListAsync();
        }
    }

}