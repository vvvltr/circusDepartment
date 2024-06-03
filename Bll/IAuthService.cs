using hh.Domain;

namespace hh.Bll;

public interface IAuthService
{
    public string Authenticate(UserDto userDto);
    public Task<bool> Register(UserDto userDto);
}