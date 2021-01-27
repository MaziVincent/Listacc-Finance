using System;
using System.Collections.Generic;

namespace ListaccFinance.API.ViewModels
{
    public class AacdemyProjectDto
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public string Description { get; set; }
        public DateTime StartDate { get; set; }
        public ICollection<AcademyProgramDto> Academy_Programs { get; set; }
    }
}