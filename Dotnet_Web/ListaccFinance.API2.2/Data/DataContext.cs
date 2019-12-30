using ListaccFinance.API.Data.Model;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Identity.EntityFrameworkCore;

namespace ListaccFinance.API.Data
{
    public class DataContext: IdentityDbContext<User, Role, int, 
    IdentityUserClaim<int>, UserRole, IdentityUserLogin<int>, IdentityRoleClaim<int>, 
    IdentityUserToken<int>>
    {
        
    }
}