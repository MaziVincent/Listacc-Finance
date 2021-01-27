namespace ListaccFinance.API.ViewModels
{
    public class AcademyProgramDto
    {
        public AacdemyProjectDto Academy_Project { get; set; }
        public double Fee { get; set; }
        public string Name { get; set; }
        public string Description { get; set; }
         public int NoOfWeeks { get; set; }
    }
}