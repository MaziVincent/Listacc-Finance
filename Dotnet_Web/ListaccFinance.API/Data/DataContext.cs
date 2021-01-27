using ListaccFinance.API.Data.Model;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Identity.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore;

namespace ListaccFinance.Api.Data
{
    public class DataContext: IdentityDbContext<User, Role, int, 
    IdentityUserClaim<int>, UserRole, IdentityUserLogin<int>, IdentityRoleClaim<int>, 
    IdentityUserToken<int>>
    {
        public DataContext(DbContextOptions<DataContext> options): base(options)
        {
            
        }
        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {

             base.OnModelCreating(modelBuilder);
            modelBuilder.Entity<Expenditure>(exp => {
               
            modelBuilder.Entity<User>().ToTable("Users");

                exp.HasOne(a => a.Issuer).WithMany(e => e.Expenditures).HasForeignKey(f => f.IssuerId);
                exp.HasOne(a => a.Client).WithMany(v => v.Expenditures).HasForeignKey(f => f.ClientId);
            });

             modelBuilder.Entity<Project>(prj => {
                 prj.HasMany(p => p.Expenditures).WithOne(p => p.Project).OnDelete(DeleteBehavior.NoAction);
            });

            modelBuilder.Entity<User>(inc => {
                inc.HasMany(p => p.Incomes).WithOne(p => p.User)
                .HasForeignKey(u => u.UserId).OnDelete(DeleteBehavior.NoAction);
                 //inc.HasMany(p => p.Incomes).WithOne(p => p.Client).OnDelete(DeleteBehavior.NoAction);
            });

            modelBuilder.Entity<Client>(inc => {
                inc.HasMany(p => p.Incomes).WithOne(p => p.Client).OnDelete(DeleteBehavior.NoAction);
                 //inc.HasMany(p => p.Incomes).WithOne(p => p.Client).OnDelete(DeleteBehavior.NoAction);
            });

           
            
        }

            public DbSet<Client> Clients { get; set; }
            public DbSet<CostCategory> CostCategories { get; set; }
            public DbSet<Department> Departments { get; set; }
            public DbSet<Expenditure> Expenditures { get; set; }
            public DbSet<Income> Incomes { get; set; }
            public DbSet<Person> Persons { get; set; }
            public DbSet<Project> Projects { get; set; }
            public DbSet<Service> Services { get; set; }
            public DbSet<Admin> Admins { get; set; }
            public DbSet<Member> Members { get; set; }

            public DbSet<Change> Changes {get; set;} 
            public DbSet<DesktopClient> DesktopClients {get; set;}

            public DbSet<Academy_Student> Academy_Students { get; set; }
            public DbSet<Academy_Program> Academy_Programs { get; set; }
            public DbSet<Academy_Registraion> Academy_Registraions { get; set; }
            public DbSet<Academy_Project> Academy_Projects { get; set; }

            //public DbSet<RefreshToken> RefreshTokens {get; set;}

            // public DbSet<User> Users { get; set; }
    }
}