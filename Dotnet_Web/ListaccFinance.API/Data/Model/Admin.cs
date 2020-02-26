using ListaccFinance.API.Data.Model;

namespace ListaccFinance.API.Data.Model
{
    public class Admin : User
    {
         public Admin():base(){
            
        }

       /* public Role role 
        {
            get{return role;}
            set
                {
                    role.Name = "Admin";
                    role.NormalizedName = "Admin".ToLower();
                }
        }*/
    }
}