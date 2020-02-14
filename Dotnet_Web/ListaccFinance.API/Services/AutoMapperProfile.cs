using AutoMapper;
using ListaccFinance.API.Data.Model;
using ListaccFinance.API.Data.ViewModel;

public class AutoMapperProfile : Profile
{
    public AutoMapperProfile()
    {
        CreateMap<Department, DepartMentViewModel>();
        CreateMap<User, UserViewModel>();
        CreateMap<Person, PersonViewModel>();
        CreateMap<CostCategory, CostCategoryViewModel>();
        CreateMap<Project, ProjectViewModel>();
        CreateMap<Client, ClientViewModel>();
        CreateMap<Service, ServiceViewModel>();
    }
}