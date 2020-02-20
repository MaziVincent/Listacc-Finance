using System.Threading.Tasks;
using ListaccFinance.Api.Data;

namespace ListaccFinance.Api.Repo
{
    public class RepoAsync<T> : IRepoAsync<T>  where T : class 
    {
        private readonly DataContext _context;

        public RepoAsync(DataContext context)
        {
            _context = context;
        }

        public async Task AddRecord<T> ( T entity) where T : class
        {
            await _context.Set<T>().AddAsync(entity);
        }

        public void DeleteRecord<T> (T entity) where T : class
        {
             _context.Set<T>().Remove(entity);
        }

    }
}