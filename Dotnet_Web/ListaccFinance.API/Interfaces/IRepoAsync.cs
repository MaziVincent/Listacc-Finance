using System.Threading.Tasks;

public interface IRepoAsync<T> where T : class
{
    Task AddRecord<T>(T entity) where T : class;
    void DeleteRecord<T>(T entity) where T : class;
}