using Microsoft.AspNetCore.Identity;

namespace hh.Domain.Models;

public class User : IdentityUser
{
    public string? Competencies { get; set; }
    public string Username { get; set; }
    private string Password { get; set; }
}