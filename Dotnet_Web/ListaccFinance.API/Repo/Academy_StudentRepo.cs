using ListaccFinance.Api.Data;
using ListaccFinance.API.Interfaces;

namespace ListaccFinance.API.Repo
{
    public class Academy_StudentRepo : IAcademy_StudentRepo
    {
        public DataContext _Datacontext { get; }
        public Academy_StudentRepo(DataContext datacontext)
        {
            this._Datacontext = datacontext;

        }

    }
}