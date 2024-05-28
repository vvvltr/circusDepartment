using hh.Domain.Models;
using Microsoft.AspNetCore.Identity.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore;

namespace hh.Dal;

public class ApiDbContext: IdentityDbContext<User>
{
    public ApiDbContext(DbContextOptions<ApiDbContext> options) : base(options)
    {
        
    }
    
    public DbSet<User> Users {get;set;}
    
    protected override void OnModelCreating(ModelBuilder builder)
    {
        base.OnModelCreating(builder); 
    }
}