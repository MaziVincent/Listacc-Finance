using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using ListaccFinance.Api.Data;
using ListaccFinance.API.Data.Model;
using ListaccFinance.API.Interfaces;

namespace ListaccFinance.API.Services
{
    public class SyncService
    {
        
        private readonly DataContext _context;

        public SyncService (DataContext context)
        {
            _context = context;
        }

        /*public async Task<List<T>> DownloadData(string MacAddress, DateTime lastUpdate)
        {
            var did =_context.DesktopClients.Where((x) => x.ClientMacAddress.CompareTo(MacAddress) == 0).FirstOrDefault().Id;

            
            List<Change> ch = _context.Changes
                                .Where(i => i.OnlineTimeStamp.CompareTo(lastUpdate) <= 0)
                                .Except(_context.Changes.Where(((x) => x.DesktopClientId != did)))
                                .ToList();
                                

        }*/
    } 
}
