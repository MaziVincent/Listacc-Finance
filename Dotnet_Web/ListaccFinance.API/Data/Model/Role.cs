using Microsoft.AspNetCore.Identity;

namespace ListaccFinance.API.Data.Model
{
    public class Role: IdentityRole<int>
    {

        public Role() :base()
        {
        }
        /*public Role(string name) : base()
        {
            this.Name = name;
        }*/
        //public static string Admin = "Admin";
        //public static string Member = "Member";
    }
}