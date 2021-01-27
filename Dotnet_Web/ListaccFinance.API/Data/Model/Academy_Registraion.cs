using System;

namespace ListaccFinance.API.Data.Model
{
    public class Academy_Registraion
    {
        public int Id { get; set; }
        public int Academy_StudentId { get; set; }
        public Academy_Student Academy_Stuent { get; set; }
        public Academy_Program Academy_Program { get; set; }
        public int Academy_ProgramId { get; set; }
        public DateTime DateRegistered { get; set; }
    }
}