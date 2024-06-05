using hh.Domain.Models;
using Microsoft.EntityFrameworkCore;

namespace hh.Dal;

public class ApiDbContext: DbContext
{
    public ApiDbContext(DbContextOptions<ApiDbContext> options) : base(options) { }

    public DbSet<User> Users { get; set; }
}