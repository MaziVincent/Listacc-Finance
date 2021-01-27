using System.Collections.Generic;
using System.Threading.Tasks;
using ListaccFinance.API.Data.Model;

namespace ListaccFinance.API.Interfaces
{
    public interface IAcademy_ProgramRepo
    {
         Task<bool> StudentRegistered(int programId, string studentEmail);
         Task<bool> StudentRegisteredWithPhone(int programId, string studentPhone);
         Task<Academy_Program> GetAcademy_Program(int Id);
         Task<ICollection<Academy_Project>> GetAllUpComingProjects();
         Task<ICollection<Academy_Project>> GetMostRecentProject();

        void Add<T>(T entity) where T: class;
        void Delete<T>(T entity) where T: class;

        Task<bool> SaveAll();
    }
}