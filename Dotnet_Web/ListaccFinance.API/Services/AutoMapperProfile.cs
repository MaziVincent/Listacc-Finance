using AutoMapper;
using ListaccFinance.API.Data.Model;
using ListaccFinance.API.Data.ViewModel;
using ListaccFinance.API.SendModel;

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

        //Reverse Mapping
        CreateMap<DepartMentViewModel, Department>();
        CreateMap<UserViewModel, RegisterModel>();
        CreateMap<PersonViewModel, Person>();
        CreateMap<CostCategoryViewModel, CostCategory>();
        CreateMap<ProjectViewModel, Project>();
        CreateMap<ClientViewModel, Client>();
        CreateMap<ServiceViewModel, Service>();

    }
}