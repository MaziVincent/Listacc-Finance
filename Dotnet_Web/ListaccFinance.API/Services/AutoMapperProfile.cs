using AutoMapper;
using ListaccFinance.API.Data.Model;
using ListaccFinance.API.Data.ViewModel;
using ListaccFinance.API.SendModel;
using Microsoft.AspNetCore.Identity;

public class AutoMapperProfile : Profile
{
    public AutoMapperProfile()
    {
        // Mapping for downloads
        CreateMap<Department, DepartMentViewModel>();
        CreateMap<IdentityUser<int>, IdentityU>();

        CreateMap<User, UserViewModel>().IncludeBase<IdentityUser<int>, IdentityU>();
        CreateMap<Person, PersonViewModel>();
        CreateMap<CostCategory, CostCategoryViewModel>();
        CreateMap<Project, ProjectViewModel>();
        CreateMap<Client, ClientViewModel>();
        CreateMap<Service, ServiceViewModel>();

        //Reverse Mapping for uploads
        CreateMap<DepartMentViewModel, Department>();
        CreateMap<UserViewModel, RegisterModel>();
        CreateMap<PersonViewModel, Person>();
        CreateMap<CostCategoryViewModel, CostCategory>();
        CreateMap<ProjectViewModel, Project>();
        CreateMap<ClientViewModel, Client>();
        CreateMap<ServiceViewModel, Service>();

        // I removed this automapper for User Upload because mapping it won't just work.  
        //CreateMap<UserViewModel, RegisterModel>();

    }
}