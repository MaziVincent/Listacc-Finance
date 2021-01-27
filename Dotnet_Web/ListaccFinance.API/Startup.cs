using System;
using System.Net;
using System.Text;
using AutoMapper;
using Learn.API.Helpers;
using ListaccFinance.Api.Data;
using ListaccFinance.Api.Repo;
using ListaccFinance.API.Data.Model;
using ListaccFinance.API.Interfaces;
using ListaccFinance.API.Repo;
using ListaccFinance.API.Services;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Diagnostics;
using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Authorization;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using Microsoft.IdentityModel.Tokens;
using static Learn.API.Helpers.Extensions;

namespace ListaccFinance.API
{
    public class Startup
    {
        public Startup(IConfiguration configuration)
        {
            Configuration = configuration;
        }
        // Local DB connection string: "Server=OLUWAMUYIWA\\SQLSERVER;Database=ListaccFinance;user id=sa;password=listacc1"
        // Better Online Db Conn string : "Server=tcp:s19.winhost.com;Database=DB_135236_listaccfin;user id=DB_135236_listaccfin_user;password=Oghuan6789"
        // Online Db connection strinG :  // "Data Source=tcp:s19.winhost.com;Initial Catalog=DB_135236_listaccfin;User ID=DB_135236_listaccfin_user;Password=******;Integrated Security=False;"
        public IConfiguration Configuration { get; }

        // This method gets called by the runtime. Use this method to add services to the container.
        public void ConfigureServices(IServiceCollection services)
        {
            
           services.AddControllers().AddNewtonsoftJson(opt => {
                opt.SerializerSettings.ReferenceLoopHandling = 
                Newtonsoft.Json.ReferenceLoopHandling.Ignore;
                opt.SerializerSettings.Converters.Add(new TrimmingConverter());
            });
            //DI
            services.AddSingleton<IConfiguration>(Configuration);

            services.AddScoped<ITokenGenerator, TokenGenerator>();
            services.AddScoped<IUserService, UserService>();
            services.AddScoped<IDesktopService, DesktopService>();
            services.AddScoped<ISyncService, SyncService>();
            services.AddScoped<IOtherServices, OtherServices>();
            services.AddScoped<IUserRepo, UserRepo>();
            services.AddScoped<IDeptService, DeptService>();
            services.AddScoped<IAcademy_StudentRepo, Academy_StudentRepo>();
            services.AddScoped<IAcademy_ProgramRepo, Academy_ProgramRepo>();
            services.AddTransient<Seed>();
            //services.AddScoped(typeof(ISyncService<>),typeof(SyncService<>));
            //DBContext
            services.AddDbContext<DataContext>(con => con.UseSqlServer(
                Configuration.GetConnectionString("DefaultConnection")));


            services.AddAutoMapper(AppDomain.CurrentDomain.GetAssemblies());

            services.AddMvc(opt => {
                opt.EnableEndpointRouting = false;
                var policy = new AuthorizationPolicyBuilder().RequireAuthenticatedUser().Build();
                opt.Filters.Add(new AuthorizeFilter(policy));
            })
                .SetCompatibilityVersion(CompatibilityVersion.Latest).AddJsonOptions(Options =>
                            {

                            });


            // Identity Builder
           

            IdentityBuilder builder2 = services.AddIdentityCore<Academy_Student>(opt => {
                    opt.Password.RequireDigit = false;
                    opt.Password.RequiredLength = 6;
                    opt.Password.RequireUppercase = false;
                    opt.Password.RequireNonAlphanumeric = false;
                });
            builder2 = new IdentityBuilder(builder2.UserType, typeof(Role), builder2.Services);
            builder2.AddEntityFrameworkStores<DataContext>();
            builder2.AddRoleValidator<RoleValidator<Role>>();

             IdentityBuilder builder = services.AddIdentityCore<User>(opt => {
                    opt.Password.RequireDigit = false;
                    opt.Password.RequiredLength = 6;
                    opt.Password.RequireUppercase = false;
                    opt.Password.RequireNonAlphanumeric = false;
                });
            builder = new IdentityBuilder(builder.UserType, typeof(Role), builder.Services);
            builder.AddEntityFrameworkStores<DataContext>();
            builder.AddRoleValidator<RoleValidator<Role>>();
            
            services.AddDbContext<DataContext>(x => x.UseSqlServer(
            Configuration.GetConnectionString("DefaultConnection"))
            );


            // Token Verification 
            services.AddAuthentication(JwtBearerDefaults.AuthenticationScheme).AddJwtBearer(
            Options =>
            {
                Options.TokenValidationParameters = new TokenValidationParameters
                {
                    ValidateIssuerSigningKey = true,
                    IssuerSigningKey = new SymmetricSecurityKey(
                        Encoding.ASCII.GetBytes(Configuration.GetSection("LoginSettings:Key").Value)),
                    ValidateIssuer = false,
                    ValidateAudience = false
                };
            });

            services.AddCors();




        }

        // This method gets called by the runtime. Use this method to configure the HTTP request pipeline.
        public void Configure(IApplicationBuilder app, IWebHostEnvironment env, Seed Seeder)
        {
            if (env.IsDevelopment())
            {
                app.UseDeveloperExceptionPage();
            }
            {
                app.UseExceptionHandler(builder => {
                    builder.Run(async context => {
                        context.Response.StatusCode = (int)HttpStatusCode.InternalServerError;

                        var error = context.Features.Get<IExceptionHandlerFeature>(); 
                        if(error != null)
                        {
                            context.Response.AddApplicationError(error.Error.Message);
                            await context.Response.WriteAsync(error.Error.Message);
                        } 
                    });
                });
                // The default HSTS value is 30 days. You may want to change this for production scenarios, see https://aka.ms/aspnetcore-hsts.
                //app.UseHsts();
            }
           // app.UseHttpsRedirection();
             Seeder.SeedUsers().Wait();
             app.UseDefaultFiles();
            app.UseRouting();

            app.UseCors(c => c.AllowAnyHeader().AllowAnyMethod().AllowAnyOrigin());


            app.UseAuthorization();
            app.UseAuthentication();
            app.UseMvc();

            /*app.UseEndpoints(endpoints =>
            {
                endpoints.MapControllers();
            });*/


        }
    }
}
