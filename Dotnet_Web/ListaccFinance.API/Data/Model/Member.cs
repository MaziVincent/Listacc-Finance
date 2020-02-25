using ListaccFinance.API.Data.Model;

namespace ListaccFinance.API.Data.Model
{
    public class Member: User
    {

         public Member():base() {
        }

       /* public Role role
        {
            get { return role; }
            set
            {
                role.Name = "Member";
                role.NormalizedName = "Member".ToLower();
            }
        }*/
    }
}


