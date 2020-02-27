using ListaccFinance.API.Interfaces;


namespace ListaccFinance.API.Services
{
    public class OtherServices : IOtherServices
    {
        public string Strip(string type)
        {
            var reType =  type.Substring(type.LastIndexOf('.')+1);
            return reType;
        }
    }
}