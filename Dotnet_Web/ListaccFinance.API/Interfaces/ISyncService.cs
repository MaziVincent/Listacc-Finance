
using System;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace ListaccFinance.API.Interfaces
{
public interface ISyncService<T> where T : class
{
        Task<List<T>> DownloadData(string MacAddress, DateTime lastUpdate);
}
}
