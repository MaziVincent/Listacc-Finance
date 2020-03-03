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
using AutoMapper;

namespace ListaccFinance.API.Services

{
    public class UserService : IUserService
    {
        private readonly DataContext _context;
        private readonly IOtherServices _oService;
        private readonly IMapper _mapper;
        private readonly IUserRepo _urepo;

        public UserService(DataContext context, IOtherServices oService, IMapper mapper, IUserRepo uRepo)
        {
            _oService = oService;
            _context = context;
            _mapper = mapper;
            _urepo = uRepo;
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
            newUser.SearchString = (newUser.Id + " " + newUser.Person.LastName + " " + newUser.Person.firstName + " " + newUser.Person.Gender + " " + newUser.Email + " " + newUser.Phone + " Member").ToUpper();
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
        public async Task<string> CreateUserAsync(RegisterModel reg, int userId)
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

    

        public bool IsUserExist()
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

        public async Task EditUserAsync(int Id, RegisterModel reg, int MyId)
        {
            var u = await _context.Users.Include(x => x.Person).Where(x => x.Id == Id).FirstOrDefaultAsync();
            u.Address = reg.Address;
            u.DepartmentId = _context.Departments.Where(x => x.Name.CompareTo(reg.Department) ==0).FirstOrDefaultAsync().Id;
            u.Email = reg.EmailAddress;
            u.Person.firstName = reg.firstName;
            u.Person.DateOfBirth = reg.DateOfBirth;
            u.Person.DateOfBirth = reg.DateOfBirth;
            u.Person.LastName = reg.LastName;
            u.Person.Gender = reg.Gender;
            // Do password Change later

            await _context.SaveChangesAsync();
            var change = new Change
            {
                Table = u.GetType().Name,
                EntryId = u.Id,
                ChangeType = "Edit",
                OfflineTimeStamp = DateTime.Now,
                OnlineTimeStamp = DateTime.Now,
                UserId = MyId
            };
            await _context.Changes.AddAsync(change);
            await _context.SaveChangesAsync();

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

        public async Task Deactivate(int Id, int MyId)
        {
            User u = await _context.Users.FindAsync(Id);
            u.Status = false;
            await _context.SaveChangesAsync();
            var change = new Change
            {
                Table = u.GetType().Name,
                EntryId = u.Id,
                ChangeType = "Edit",
                OfflineTimeStamp = DateTime.Now,
                OnlineTimeStamp = DateTime.Now,
                UserId = MyId
            };
            await _context.Changes.AddAsync(change);
            await _context.SaveChangesAsync();
        }

        
        public async Task Activate(int Id, int MyId)
        {
            User u = await _context.Users.FindAsync(Id);
            u.Status = true;
            await _context.SaveChangesAsync();
            var change = new Change{
                Table = u.GetType().Name,
                EntryId = u.Id,
                ChangeType = "Edit",
                OfflineTimeStamp = DateTime.Now,
                OnlineTimeStamp =DateTime.Now,
                UserId = MyId
            };
            await _context.Changes.AddAsync(change);
            await _context.SaveChangesAsync();
        }

        // Search when Search String is not null
        public async Task<IEnumerable<User>> ReturnUsers(SearchPaging props)
        {
            var returned =  await SearchUser(props).OrderBy(x => x.Id)
                                        .Skip((props.PageNumber - 1) * props.PageSize)
                                        .Take(props.PageSize)
                                        .ToListAsync();
            int i = returned.Count;
            return returned;
        }

        private IQueryable<User> SearchUser(SearchPaging props)
        {
            var u = _context.Users.Include(x => x.Person).Where(x =>
                            x.Status == props.Status
                            &&
                            (x.SearchString.Contains(props.SearchString.ToUpper())));

            int TotalPages = u.Count();
            int PageCount = TotalPages / props._pageSize;
            return u;
        }

        // Search when search string is null
        public async Task<IEnumerable<User>> ReturnAllUsers(SearchPaging props)
        {
            var returned = await _context.Users.Include(x => x.Person).Where(
                                        (x) =>
                                        x.Status.CompareTo(props.Status) == 0)
                                        .OrderBy(x => x.Id)
                                        .Skip((props.PageNumber - 1) * props.PageSize)
                                        .Take(props.PageSize)
                                        .ToListAsync();
            int i = returned.Count;
            return returned;
        }

        public async Task<RegisterModel> ReturnUser(int Id)
        {
            User u = await _urepo.GertUserById(Id);
            var User = _mapper.Map<RegisterModel>(u);
            return User;
    }

}
}