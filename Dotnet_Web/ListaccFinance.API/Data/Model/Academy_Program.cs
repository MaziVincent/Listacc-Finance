using System;

namespace ListaccFinance.API.Data.Model
{
    public class Academy_Program
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public int NoOfWeeks { get; set; }
        public string Description { get; set; }
        public double Fee { get; set; }
        public int? Academy_ProjectId { get; set; }
        public Academy_Project Academy_Project { get; set; }

    }
}