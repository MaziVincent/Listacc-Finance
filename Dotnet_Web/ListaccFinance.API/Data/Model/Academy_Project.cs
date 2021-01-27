using System;
using System.Collections.Generic;

namespace ListaccFinance.API.Data.Model
{
    public class Academy_Project
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public string Description { get; set; }
        public DateTime StartDate { get; set; }
        public ICollection<Academy_Program> Academy_Programs { get; set; }
    }
}