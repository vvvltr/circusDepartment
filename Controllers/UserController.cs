using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Text;
using hh.Bll;
using hh.Dal;
using hh.Domain;
using hh.Domain.Models;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Microsoft.IdentityModel.Tokens;
using JwtRegisteredClaimNames = Microsoft.IdentityModel.JsonWebTokens.JwtRegisteredClaimNames;

namespace hh.Controllers;

[ApiController]
[Route("[controller]")]
public class UserController : Controller
{
    private readonly IAuthService _auth;
    private readonly IConfiguration _configuration;

    public UserController(IAuthService auth,
        IConfiguration configuration)
    {
        _auth = auth ?? throw new ArgumentException(nameof(auth));
        _configuration = configuration ?? throw new ArgumentException(nameof(_configuration));
    }

    [HttpPost("register")]
    public async Task<IActionResult> Register(UserDto userDto)
    {
        if (await _auth.Register(userDto))
        {
            return Ok();
        }
        return Conflict(new { message = "User already exists" });
    }

    [HttpPost("login")]
    public IActionResult Login(UserDto userDto)
    {
        var token = _auth.Authenticate(userDto);
        if (token == null)
            return Unauthorized();

        return Ok(new { Token = token });
    }
}