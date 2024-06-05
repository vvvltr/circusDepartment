using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Text;
using hh.Dal;
using hh.Domain;
using hh.Domain.Models;
using hh.Helper;
using Microsoft.EntityFrameworkCore;
using Microsoft.IdentityModel.Tokens;

namespace hh.Bll;

public class AuthService : IAuthService
{
    private readonly IConfiguration _configuration;
    private readonly ApiDbContext _context;

    public AuthService(IConfiguration configuration, ApiDbContext context)
    {
        _configuration = configuration;
        _context = context;
    }

    public string Authenticate(UserDto userDto)
    {
        var user = _context.Users.SingleOrDefault(u => u.Email == userDto.Email);
        if (user == null || !PasswordHasher.VerifyPassword(user.Password, userDto.Password, user.Salt))
        {
            return null;
        }

        var tokenHandler = new JwtSecurityTokenHandler();
        var key = Encoding.ASCII.GetBytes(_configuration["Jwt:Key"]);
        var tokenDescriptor = new SecurityTokenDescriptor
        {
            Subject = new ClaimsIdentity(new Claim[]
            {
                new Claim(ClaimTypes.Email, user.Email)
            }),
            Expires = DateTime.UtcNow.AddHours(1),
            SigningCredentials = new SigningCredentials(new SymmetricSecurityKey(key), SecurityAlgorithms.HmacSha256Signature)
        };
        var token = tokenHandler.CreateToken(tokenDescriptor);
        return tokenHandler.WriteToken(token);
    }

    public async Task<bool> Register(UserDto userDto)
    {
        if (_context.Users.Any(u => u.Email == userDto.Email))
        {
            return false;
        }

        var salt = PasswordHasher.GenerateSalt();
        var hashedPassword = PasswordHasher.HashPassword(userDto.Password, salt);

        var user = new User
        {
            Email = userDto.Email,
            Password = hashedPassword,
            Salt = salt
        };
        _context.Users.Add(user);
        _context.SaveChanges();
        return true;
    }

    public async Task<string> GetCompetences(string email)
    {
        var _user = await _context.Users.SingleOrDefaultAsync(u => u.Email == email);
        return _user.Competencies.ToString();
    }

    public async Task<bool> UpdateCompetences(string email, string newCompetences)
    {
        var user = await _context.Users.SingleOrDefaultAsync(u => u.Email == email);
        if (user == null)
        {
            return false;
        }

        user.Competencies = newCompetences;
        await _context.SaveChangesAsync();
        return true;
    }
}